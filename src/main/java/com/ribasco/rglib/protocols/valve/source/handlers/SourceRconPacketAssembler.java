/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Ibasco
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **************************************************************************************************/

package com.ribasco.rglib.protocols.valve.source.handlers;

import com.ribasco.rglib.core.utils.ByteUtils;
import com.ribasco.rglib.protocols.valve.source.SourceRconPacketBuilder;
import com.ribasco.rglib.protocols.valve.source.SourceRconResponsePacket;
import com.ribasco.rglib.protocols.valve.source.enums.SourceRconResponseType;
import com.ribasco.rglib.protocols.valve.source.packets.response.SourceRconCmdResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    private LinkedList<ResponseContainer> responseQueue = new LinkedList<>();

    private static final int HEADER_SIZE = 4;
    private static final int TRAILER_SIZE = 2;

    private SourceRconPacketBuilder builder;

    private final class ResponseContainer {
        private int requestId;
        private int size;
        private int type;
        private InetSocketAddress sender;
        private ByteArrayOutputStream body;

        public ResponseContainer(int requestId, int size, int type, InetSocketAddress sender, ByteArrayOutputStream body) {
            this.requestId = requestId;
            this.size = size;
            this.type = type;
            this.sender = sender;
            this.body = body;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ResponseContainer that = (ResponseContainer) o;
            return new EqualsBuilder()
                    .append(requestId, ((ResponseContainer) o).requestId)
                    .append(sender.getAddress().getHostAddress(), ((ResponseContainer) o).sender.getAddress().getHostAddress())
                    .append(sender.getPort(), ((ResponseContainer) o).sender.getPort())
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
            return "ResponseContainer{" +
                    "requestId=" + requestId +
                    ", size=" + size +
                    ", type=" + type +
                    ", sender=" + sender;
        }
    }

    private AtomicBoolean isSplitPacket = new AtomicBoolean(false);
    private AtomicInteger totalBytesRead = new AtomicInteger();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        if (builder == null) {
            builder = new SourceRconPacketBuilder(ctx.channel().alloc());
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        log.info("READ: START (Total Readable Bytes: {}, From Handler Instance: {})", msg.readableBytes(), this);

        final InetSocketAddress sender = (InetSocketAddress) ctx.channel().remoteAddress();

        //Verify that we have enough bytes we could process
        if (msg.readableBytes() < 10) {
            log.warn("Not enough bytes to process. Skipping");
            return;
        }

        //Remember the initial reader position before we start processing
        msg.markReaderIndex();

        //Try and read the packet fields
        int bodySize = msg.readIntLE();
        int requestId = msg.readIntLE();
        int responseHeader = msg.readIntLE();

        //Did we get an auth response?
        if (responseHeader == 2) {
            log.info("Found auth");
            msg.resetReaderIndex();
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

        if (isValidResponseHeader) {
            log.info("Declared Body Size: {}, Request Id: {}, Header: {}, Readable Bytes: {}, Reader Index: {}, Total Packet Size: {}", bodySize, requestId, responseHeader, msg.readableBytes(), msg.readerIndex(), packetSize);

            //Do we have a body to process?
            if (msg.readableBytes() > 2) {
                //We have the acceptable number of bytes we could process and pass instantly
                //for the next handler to process.
                if (hasNullTerminator) {
                    //Reconstruct the packet and pass to the next handler
                    log.info("Passing data to the next handler");

                    //No need to reset the index at this point
                    SourceRconCmdResponsePacket responsePacket = builder.construct(msg);

                    //Decon
                    byte[] pData = builder.deconstruct(responsePacket);
                    ByteBuf newMsg = ctx.channel().alloc().buffer(pData.length);
                    newMsg.writeBytes(pData);

                    log.info("Completed Packet: \n{}", ByteBufUtil.prettyHexDump(newMsg));

                    //Pass the message to the next handler
                    ctx.fireChannelRead(newMsg);
                }
                //No null terminator so it must be a split-packet response
                else {
                    //If we reach this point and the body does not yet have a null terminator and size is > 500.
                    // Then its safe to assume that we might have a split-packet response here.

                    //Mark this as a split packet
                    isSplitPacket.set(true);

                    String hexDump = ByteBufUtil.prettyHexDump(msg);
                    log.info("Partial Body: {}", hexDump);
                    //StringBuffer partialBody = new StringBuffer(msg.readCharSequence(msg.readableBytes(), StandardCharsets.UTF_8).toString());

                    //Create our new body container, this will be processed once we have received all packets
                    ByteArrayOutputStream body = new ByteArrayOutputStream();

                    //Include the header fields (Total of 12 bytes)
                    body.write(ByteUtils.byteArrayFromInteger(Integer.reverseBytes(bodySize)));
                    body.write(ByteUtils.byteArrayFromInteger(Integer.reverseBytes(requestId)));
                    body.write(ByteUtils.byteArrayFromInteger(Integer.reverseBytes(responseHeader)));

                    //Write the body content to the byte output stream
                    readToOs(msg, body, msg.readableBytes());

                    log.info("Writing initial response body to container: \n{}", ByteBufUtil.prettyHexDump(Unpooled.copiedBuffer(body.toByteArray())));

                    responseQueue.add(new ResponseContainer(requestId, bodySize, responseHeader, sender, body));

                    log.info("Found a possible split packet! Readable Bytes : {}, Has Null Terminator: {}, Byte #1: {}, Byte #2: {} \n {}", msg.readableBytes(), hasNullTerminator, msg.getByte(packetSize - 2), msg.getByte(packetSize - 1), hexDump);
                }
            }
            //No readable body
            else {
                //If we reach this point, then we received an empty response.
                log.info("RECEIVED AN EMPTY VALID RESPONSE. Skipping for now : ");
                //TODO: Skip for now until we have an actual implementation. Safe to ignore?
                msg.skipBytes(msg.readableBytes());
            }
        }
        //Response does not have a valid header
        else {
            //If we reach this point and we have readable bytes > 0, then this is most likely
            // a continuation of a series of split-packets
            if (isSplitPacket.get()) {
                //reset the index since this is not a valid header
                msg.resetReaderIndex();

                //Just read everything for now
                String hexDump = ByteBufUtil.prettyHexDump(msg);

                //Keep track of the number of bytes we have read for the response body
                totalBytesRead.addAndGet(msg.readableBytes());

                if (!hasNullTerminator) {
                    final ResponseContainer container = responseQueue.peek();
                    if (container == null) {
                        throw new IllegalStateException("Unable to find the response container for this packet");
                    }
                    log.info("Appending body to the response container (Current Size: {}) : \n{}", msg.readableBytes(), hexDump);

                    //Append the split-packet to the container
                    readToOs(msg, container.body, msg.readableBytes());
                } else {
                    log.info("FOUND THE END OF THE SPLIT PACKET!!!! Last Two Bytes ({}, {}), Total Bytes Read: {}", msg.getByte(packetSize - 2), msg.getByte(packetSize - 1), totalBytesRead.get());
                    isSplitPacket.set(false);

                    // Remove the container from the head of the queue
                    final ResponseContainer container = responseQueue.remove();

                    log.info("Adding the last response body of the split packet : \n{}", ByteBufUtil.prettyHexDump(msg));

                    //Read the last partial body to the output stream
                    readToOs(msg, container.body, msg.readableBytes());

                    //Perform post-processing of the response
                    if (container != null) {
                        log.info("START : Re-assembling Packet. Total Body Size: {}", container.body.size());

                        //Transfer the data from the byte output stream into the allocated buffer
                        final ByteBuf packetBuffer = ctx.channel().alloc().buffer(container.body.size());
                        packetBuffer.writeBytes(container.body.toByteArray());

                        final StringBuffer reassembledResponseBody = new StringBuffer();

                        //Process all packets within the container, make sure we remove all the headers
                        packetBuffer.markReaderIndex();

                        //Now that we have received a complete packet, we can start assembling
                        while (packetBuffer.readableBytes() > 0) {
                            //Read the header (12 bytes total)
                            int size = packetBuffer.readIntLE(); //Declared body size
                            int id = packetBuffer.readIntLE();
                            int type = packetBuffer.readIntLE();

                            //Retrieve the length until the first null terminator
                            int bodyLength = packetBuffer.bytesBefore((byte) 0);

                            //Make sure that the length of the response body is > 0
                            assert bodyLength > 0;

                            log.info(" * Appending Response Body (Length until Null Byte: {}, Size: {}, Id: {}, Type: {})", bodyLength, size, id, type);

                            //Append the partial response to the string buffer
                            reassembledResponseBody.append(packetBuffer.readCharSequence(bodyLength, StandardCharsets.UTF_8));

                            //Make sure that the next two bytes are NULL terminators
                            assert (packetBuffer.getByte(packetBuffer.readerIndex()) == 0) && (packetBuffer.getByte(packetBuffer.readerIndex() + 1) == 0);

                            //Skip the next two bytes
                            packetBuffer.skipBytes(2);
                        }

                        log.info("END : Re-assembling Packet");

                        //Process the completed body
                        SourceRconResponsePacket packet = new SourceRconCmdResponsePacket();
                        packet.setSize(reassembledResponseBody.length());
                        packet.setId(container.requestId);
                        packet.setType(container.type);
                        packet.setBody(reassembledResponseBody.toString());

                        byte[] pData = builder.deconstruct(packet);
                        ByteBuf assembledPacket = ctx.channel().alloc().buffer(pData.length);
                        assembledPacket.writeBytes(pData);

                        //Pass the assembled packet to the next handler in the chain
                        log.info("Passing assembled packet to the next handler. Queue size is now {}", responseQueue.size());
                        ctx.fireChannelRead(assembledPacket);
                    } else
                        throw new IllegalStateException("Did not find a container for this packet");
                }
            } else {
                throw new IllegalStateException("Response is neither valid or handled. Not marked as a split packet");
            }
        }
        log.info("READ: DONE");
    }

    private ByteBuf processBodyBuffer(ByteBuf body) {

        return null;
    }

    private void readToOs(ByteBuf buf, ByteArrayOutputStream bos, int length) throws IOException {
        buf.readBytes(bos, length);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        super.channelWritabilityChanged(ctx);
    }
}
