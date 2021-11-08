/*
 * MIT License
 *
 * Copyright (c) 2018 Asynchronous Game Query Library
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

package com.ibasco.agql.protocols.valve.source.query.handlers;

import com.ibasco.agql.core.SplitPacketContainer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Responsible for verifying and assembling datagram packets</p>
 */
public class SourceQueryPacketAssembler extends ChannelInboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(SourceQueryPacketAssembler.class);

    /**
     * A container for split packets. Since we are dealing with multiple responses here,
     * we need to create a map for each type of response to avoid conflicts.
     */
    //FIX: Different servers can return the same request id, so using the request id as the sole key can cause conflicts
    private final Map<SplitPacketKey, SplitPacketContainer> requestMap = new ConcurrentHashMap<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.trace("channelRead() : START");

        try {
            //Make sure we are only receiving an instance of DatagramPacket
            if (!(msg instanceof DatagramPacket)) {
                ctx.fireChannelRead(msg);
                return;
            }

            DatagramPacket packet = (DatagramPacket) msg;
            ByteBuf data = packet.content();

            //Verify size
            if (data.readableBytes() <= 5) {
                log.debug("Not a valid datagram for processing. Size getTotalRequests needs to be at least more than or equal to 5 bytes. Discarding. (Readable Bytes: {})", data.readableBytes());
                return;
            }

            //Try to read protocol header, determine if its a single packet or a split-packet
            int protocolHeader = data.readIntLE();

            //If the packet arrived is single type, we can already forward it to the next handler
            if (protocolHeader == 0xFFFFFFFF) {
                //Pass the message to the succeeding handlers
                ctx.fireChannelRead(packet.retain());
                return;
            }
            //If the packet is a split type...we need to process each succeeding read until we have a complete packet
            else if (protocolHeader == 0xFFFFFFFE) {
                ByteBuf reassembledPacket = processSplitPackets(data, ctx.channel().alloc(), packet.sender());
                //Check if we already have a reassembled packet
                if (reassembledPacket != null) {
                    ctx.fireChannelRead(packet.replace(reassembledPacket));
                    return;
                }
            }
            //Packet is not being handled by any of our processors, discard
            else {
                log.debug("Not a valid protocol header. Discarding. (Header Received: Dec = {}, Hex = {})", protocolHeader, Integer.toHexString(protocolHeader));
                ctx.fireChannelRead(msg);
                return;
            }
        }  finally {
            //Release the message
            ReferenceCountUtil.release(msg);
        }
        log.trace("channelRead() : END");
    }

    /**
     * Process split-packet data
     *
     * @param data
     *         The {@link ByteBuf} containing the split-packet data
     * @param allocator
     *         The {@link ByteBufAllocator} used to create/allocate pooled buffers
     *
     * @return Returns a non-null {@link ByteBuf} if the split-packets have been assembled. Null indicating that we still don't have a complete packet.
     *
     */
    private ByteBuf processSplitPackets(ByteBuf data, ByteBufAllocator allocator, InetSocketAddress senderAddress) throws Exception {
        int packetCount, packetNumber, requestId, splitSize, packetChecksum = 0;
        boolean isCompressed;

        //Start processing
        requestId = data.readIntLE();
        //read the most significant bit is set
        isCompressed = ((requestId & 0x80000000) != 0);
        //The total number of packets in the response.
        packetCount = data.readByte();
        //The number of the packet. Starts at 0.
        packetNumber = data.readByte();

        //Create our key for this request (request id + sender ip)
        SplitPacketKey key = new SplitPacketKey(requestId, senderAddress);

        log.debug("Processing split packet {}", key);

        log.debug("Split Packet Received = (AbstractRequest {}, Packet Number {}, Packet Count {}, Is Compressed: {})", requestId, packetNumber, packetCount, isCompressed);

        //Try to retrieve the split packet container for this request (if existing)
        //If request is not yet on the map, create and retrieve
        SplitPacketContainer splitPackets = this.requestMap.computeIfAbsent(key, k -> new SplitPacketContainer(packetCount));

        //As per protocol specs, the size is only present in the first packet of the response and only if the response is being compressed.
        //split size = Maximum size of packet before packet switching occurs. The default value is 1248 bytes (0x04E0
        if (isCompressed) {
            splitSize = data.readIntLE();
            packetChecksum = data.readIntLE();
        } else {
            splitSize = data.readShortLE();
        }

        //TODO: Handle compressed split packets
        int bufferSize = Math.min(splitSize, data.readableBytes());
        byte[] splitPacket = new byte[bufferSize];
        data.readBytes(splitPacket); //transfer the split data into this buffer

        //Add the split packet to the container
        splitPackets.addPacket(packetNumber, splitPacket);

        //Have we received all packets for this request?
        if (splitPackets.isComplete()) {
            log.debug("Split Packets have all been successfully received from AbstractRequest {}. Re-assembling packets.", requestId);

            //Retrieve total split packets received based on their length
            int packetSize = splitPackets.getPacketSize();
            //Allocate a new buffer to store the re-assembled packets
            ByteBuf packetBuffer = allocator.buffer(packetSize);
            boolean done = false;
            try {
                //Start re-assembling split-packets from the container
                done = reassembleSplitPackets(splitPackets, packetBuffer, isCompressed, splitSize, packetChecksum);
            } catch (Exception e) {
                //If an error occurs during re-assembly, make sure we release the allocated buffer
                packetBuffer.release();
                throw e;
            } finally {
                if (done)
                    requestMap.remove(key);
            }
            return packetBuffer;
        }

        //Return null, indicating that we still don't have a complete packet
        return null;
    }

    /**
     * Re-assemble's the packets from the container.
     *
     * @param splitPackets The {@link SplitPacketContainer} to be re-assembled
     *
     * @return Returns true if the re-assembly process has completed successfully
     */
    private boolean reassembleSplitPackets(SplitPacketContainer splitPackets, ByteBuf packetBuffer, boolean isCompressed, int decompressedSize, int packetChecksum) {
        log.trace("reassembleSplitPackets : START");

        if (packetBuffer == null)
            throw new IllegalArgumentException("Packet Buffer is not initialized");

        splitPackets.forEachEntry(packetEntry -> {
            log.debug("--> Packet #{} : {}", packetEntry.getKey(), packetEntry.getValue());

            //Throw exception if compression is set. Not yet supported.
            if (isCompressed)
                throw new IllegalStateException("Compression is not yet supported at this time sorry");

            //TODO: Is this still needed?
            /*if(isCompressed) {
                //From Steam Condenser (thanks Koraktor)
                try {
                    ByteArrayInputStream stream = new ByteArrayInputStream(packetData);
                    stream.read();
                    stream.read();
                    BZip2CompressorInputStream bzip2 = new BZip2CompressorInputStream(stream);
                    byte[] uncompressedPacketData = new byte[uncompressedSize];
                    bzip2.read(uncompressedPacketData, 0, uncompressedSize);

                    CRC32 crc32 = new CRC32();
                    crc32.update(uncompressedPacketData);
                    int crc32checksum = (int) crc32.getValue();

                    if (crc32checksum != packetChecksum) {
                        throw new PacketFormatException(
                                "CRC32 checksum mismatch of uncompressed packet data.");
                    }
                    packetData = uncompressedPacketData;
                } catch(IOException e) {
                    throw new SteamCondenserException(e.getMessage(), e);
                }
            }*/

            packetBuffer.writeBytes(packetEntry.getValue());
        });

        log.trace("reassembleSplitPackets : END");

        return packetBuffer.readableBytes() > 0;
    }

    public static final class SplitPacketKey {
        private final Integer requestId;
        private final InetSocketAddress address;

        public SplitPacketKey(Integer requestId, InetSocketAddress address) {
            this.requestId = Objects.requireNonNull(requestId, "Missing request id");
            this.address = Objects.requireNonNull(address, "Missing address");
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof SplitPacketKey))
                return false;
            if (o == this)
                return true;
            SplitPacketKey rhs = (SplitPacketKey) o;
            return new EqualsBuilder()
                    .append(requestId, rhs.requestId)
                    .append(address.getAddress().getHostAddress(), rhs.address.getAddress().getHostAddress())
                    .append(address.getPort(), rhs.address.getPort())
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(63, 81)
                    .append(requestId)
                    .append(address.getAddress().getHostAddress())
                    .append(address.getPort()).hashCode();
        }

        @Override
        public String toString() {
            return String.format("[SplitPacketKey]: Request ID = %s, Address = %s", requestId, address);
        }
    }
}
