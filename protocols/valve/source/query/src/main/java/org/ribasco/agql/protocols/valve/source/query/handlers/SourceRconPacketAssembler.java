/*
 * MIT License
 *
 * Copyright (c) 2016 Asynchronous Game Query Library
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.ribasco.agql.protocols.valve.source.query.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.ribasco.agql.core.utils.ByteUtils;
import org.ribasco.agql.protocols.valve.source.query.SourceRconPacketBuilder;
import org.ribasco.agql.protocols.valve.source.query.SourceRconResponsePacket;
import org.ribasco.agql.protocols.valve.source.query.enums.SourceRconResponseType;
import org.ribasco.agql.protocols.valve.source.query.packets.response.SourceRconCmdResponsePacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Handle split-packet responses here
 * <p>
 * Rcon Packet Structure:
 * <pre>
 * ----------------------------------------------------------------------------
 * Field           Type                                    Value
 * ----------------------------------------------------------------------------
 * Size            32-bit little-endian Signed Integer     Varies, see below.
 * ID              32-bit little-endian Signed Integer     Varies, see below.
 * Type            32-bit little-endian Signed Integer     Varies, see below.
 * Body            Null-terminated ASCII String            Varies, see below.
 * Empty String    Null-terminated ASCII String            0x00
 * ----------------------------------------------------------------------------
 * </pre>
 */
public class SourceRconPacketAssembler extends SimpleChannelInboundHandler<ByteBuf> {
    private static final Logger log = LoggerFactory.getLogger(SourceRconPacketAssembler.class);

    private LinkedList<RconSplitPacketBuilder> responseQueue = new LinkedList<>();

    private AtomicBoolean isSplitPacket = new AtomicBoolean(false);
    private AtomicInteger totalBytesRead = new AtomicInteger();

    private static final int HEADER_SIZE = 12; //4 bytes * 3
    private static final int TRAILER_SIZE = 2; //2 null bytes

    private SourceRconPacketBuilder builder;

    private final class RconSplitPacketBuilder {
        private int requestId;
        private int size;
        private int type;
        private InetSocketAddress sender;
        private ByteArrayOutputStream body;

        public RconSplitPacketBuilder(int requestId, int size, int type, InetSocketAddress sender, ByteArrayOutputStream body) {
            this.requestId = requestId;
            this.size = size;
            this.type = type;
            this.sender = sender;
            //TODO: Maybe we should replace this with a bytebuf instance instead? To avoid re-allocating
            this.body = body;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RconSplitPacketBuilder that = (RconSplitPacketBuilder) o;
            return new EqualsBuilder()
                    .append(requestId, ((RconSplitPacketBuilder) o).requestId)
                    .append(sender.getAddress().getHostAddress(), ((RconSplitPacketBuilder) o).sender.getAddress().getHostAddress())
                    .append(sender.getPort(), ((RconSplitPacketBuilder) o).sender.getPort())
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(51, 83)
                    .append(requestId)
                    .append(sender.getAddress().getHostAddress())
                    .append(sender.getPort())
                    .hashCode();
        }

        @Override
        public String toString() {
            return "RconSplitPacketBuilder{" +
                    "requestId=" + requestId +
                    ", size=" + size +
                    ", type=" + type +
                    ", sender=" + sender;
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        if (builder == null) {
            builder = new SourceRconPacketBuilder(ctx.channel().alloc());
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        log.debug("READ: START (Total Readable Bytes: {}, From Handler Instance: {})", msg.readableBytes(), this);

        final InetSocketAddress sender = (InetSocketAddress) ctx.channel().remoteAddress();

        //Verify that we have enough bytes we could process
        if (msg.readableBytes() < 10) {
            log.warn("Not enough bytes to process. Skipping");
            return;
        }

        //Remember the initial reader position before we listen processing
        msg.markReaderIndex();

        //Try and read the packet fields
        int bodySize = msg.readIntLE();
        int requestId = msg.readIntLE();
        int responseHeader = msg.readIntLE();

        //Did we get an auth response?
        if (responseHeader == 2) {
            log.debug("Found authentication response");
            msg.resetReaderIndex();
            //Instantly pass this to the next handler
            ctx.fireChannelRead(msg.retain());
            return;
        }

        int packetSize = msg.readableBytes() + msg.readerIndex();

        //Check if we have a valid response header
        boolean isValidResponseHeader = SourceRconResponseType.isValid(responseHeader);

        //Check if the last two bytes are null bytes.
        // This indicates that we have reached the end of the message/packet
        boolean hasNullTerminator = msg.readableBytes() >= 2 &&
                msg.getByte(packetSize - 2) == 0 &&
                msg.getByte(packetSize - 1) == 0;

        //Do we have a valid response header?
        if (isValidResponseHeader) {
            log.debug("Declared Body Size: {}, Request Id: {}, Header: {}, Readable Bytes: {}, Reader Index: {}, Total Packet Size: {}", bodySize, requestId, responseHeader, msg.readableBytes(), msg.readerIndex(), packetSize);

            //Do we have a body to process?
            if (msg.readableBytes() > 2) {
                //Do we have a null terminator in the body?
                if (hasNullTerminator) {
                    //Reconstruct the packet and pass to the next handler
                    log.debug("Passing data to the next handler");

                    //No need to reset the index at this point
                    SourceRconCmdResponsePacket responsePacket = builder.construct(msg);

                    //Decon
                    byte[] pData = builder.deconstruct(responsePacket);
                    ByteBuf newMsg = ctx.channel().alloc().buffer(pData.length);
                    newMsg.writeBytes(pData);

                    log.debug("Completed Packet: \n{}", ByteBufUtil.prettyHexDump(newMsg));

                    //Pass the message to the next handler
                    ctx.fireChannelRead(newMsg);
                }
                //No null terminator so it must be a split-packet response
                //At this point, we need to mark this as the listen of a split-packet response
                else {
                    //If we reach this point and the body does not yet have a null terminator and size is > 500.
                    // Then its safe to assume that we might have a split-packet response here.

                    //Mark the listen of a split packet
                    isSplitPacket.set(true);

                    String hexDump = ByteBufUtil.prettyHexDump(msg);
                    log.debug("Partial Body: {}", hexDump);

                    //Create our new body container,
                    // Note: This body contains multiple rcon command responses so we need to breakdown and
                    // process each response after we have received everything
                    final ByteArrayOutputStream body = new ByteArrayOutputStream();

                    //Include the header fields (Total of 12 bytes)
                    body.write(ByteUtils.byteArrayFromInteger(Integer.reverseBytes(bodySize)));
                    body.write(ByteUtils.byteArrayFromInteger(Integer.reverseBytes(requestId)));
                    body.write(ByteUtils.byteArrayFromInteger(Integer.reverseBytes(responseHeader)));

                    //Write the body content to the byte output stream
                    //readToOs(msg, body, msg.readableBytes());
                    msg.readBytes(body, msg.readableBytes());

                    log.debug("Writing initial response body to container: \n{}", ByteBufUtil.prettyHexDump(Unpooled.copiedBuffer(body.toByteArray())));

                    responseQueue.add(new RconSplitPacketBuilder(requestId, bodySize, responseHeader, sender, body));

                    log.debug("Found a possible split packet! Readable Bytes : {}, Byte #1: {}, Byte #2: {} \n {}", msg.readableBytes(), msg.getByte(packetSize - 2), msg.getByte(packetSize - 1), hexDump);
                }
            }
            //No readable body
            else {
                //If we reach this point, then we have received an empty response from the game logger.
                log.debug("Received a valid empty response from the logger. Skipping");
                msg.skipBytes(msg.readableBytes());
            }
        }
        //Response does not have a valid header
        else {
            //If we reach this point and we have readable bytes > 0, then this is most likely
            // a continuation of a series of split-packets

            //Make sure this is marked-as a split packet response before we proceed
            if (isSplitPacket.get()) {
                //reset the index since this is not a valid header
                msg.resetReaderIndex();

                //Just read everything for now
                String hexDump = ByteBufUtil.prettyHexDump(msg);

                //Keep track of the number of bytes we have read for the response body
                totalBytesRead.addAndGet(msg.readableBytes());

                if (!hasNullTerminator) {
                    final RconSplitPacketBuilder container = responseQueue.peek();
                    if (container == null) {
                        throw new IllegalStateException("Unable to find the response container for this packet");
                    }
                    log.debug("Appending body to the response container (Current Size: {}) : \n{}", msg.readableBytes(), hexDump);

                    //Append the split-packet to the container
                    //readToOs(msg, container.body, msg.readableBytes());
                    msg.readBytes(container.body, msg.readableBytes());
                } else {
                    log.debug("Found the end of the split-packet response. Last Two Bytes ({}, {}), Total Bytes Read: {}", msg.getByte(packetSize - 2), msg.getByte(packetSize - 1), totalBytesRead.get());
                    isSplitPacket.set(false);

                    // Remove the container from the head of the queue
                    final RconSplitPacketBuilder container = responseQueue.poll();

                    log.debug("Adding the last response body of the split packet : \n{}", ByteBufUtil.prettyHexDump(msg));

                    //Read the last partial body to the output stream
                    msg.readBytes(container.body, msg.readableBytes());

                    //Perform post-processing of the response body
                    if (container != null) {
                        log.debug("START : Re-assembling Packet. Total Body Size: {}", container.body.size());

                        //Transfer the data from the byte output stream into the allocated buffer
                        final ByteBuf packetBuffer = ctx.channel().alloc().buffer(container.body.size());
                        packetBuffer.writeBytes(container.body.toByteArray());

                        final StringBuffer reassembledResponseBody = new StringBuffer();

                        //Process all packets within the container, make sure we remove all the headers
                        packetBuffer.markReaderIndex();

                        //Now that we have received a complete packet, we can listen the re-assembly process
                        while (packetBuffer.readableBytes() > 0) {
                            //Read the header (12 bytes total)
                            int size = packetBuffer.readIntLE(); //Declared body size
                            int id = packetBuffer.readIntLE();
                            int type = packetBuffer.readIntLE();

                            //Retrieve the length until the first null terminator
                            int bodyLength = packetBuffer.bytesBefore((byte) 0);

                            //Make sure that the length of the response body is > 0
                            assert bodyLength > 0;

                            log.debug(" * Appending Response Body (Length until Null Byte: {}, Size: {}, Id: {}, Type: {})", bodyLength, size, id, type);

                            //Append the partial response to the string buffer
                            reassembledResponseBody.append(packetBuffer.readCharSequence(bodyLength, StandardCharsets.UTF_8));

                            //Make sure that the next two bytes are NULL terminators
                            assert (packetBuffer.getByte(packetBuffer.readerIndex()) == 0) && (packetBuffer.getByte(packetBuffer.readerIndex() + 1) == 0);

                            //Skip the next two bytes
                            packetBuffer.skipBytes(2);
                        }

                        log.debug("END : Re-assembling Packet");

                        //Process the completed body
                        SourceRconResponsePacket packet = new SourceRconCmdResponsePacket();
                        packet.setSize(reassembledResponseBody.length());
                        packet.setId(container.requestId);
                        packet.setType(container.type);
                        packet.setBody(reassembledResponseBody.toString());

                        byte[] pData = builder.deconstruct(packet);

                        //Allocate our new buffer for the re-assembled packet
                        final ByteBuf reassembledPacket = ctx.channel().alloc().buffer(pData.length);
                        reassembledPacket.writeBytes(pData);

                        //Pass the assembled packet to the next handler in the chain
                        log.debug("Passing assembled packet to the next handler. Queue size is now {}", responseQueue.size());
                        ctx.fireChannelRead(reassembledPacket);
                    } else
                        throw new IllegalStateException("Did not find a container for this packet");
                }
            } else {
                throw new IllegalStateException("Response is neither valid or handled. Not marked as a split packet");
            }
        }
        log.debug("READ: DONE");
    }
}
