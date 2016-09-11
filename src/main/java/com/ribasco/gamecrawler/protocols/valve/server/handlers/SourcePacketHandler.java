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

package com.ribasco.gamecrawler.protocols.valve.server.handlers;

import com.ribasco.gamecrawler.protocols.SplitPacketContainer;
import com.ribasco.gamecrawler.protocols.valve.server.SourceMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.netty.buffer.ByteBufUtil.prettyHexDump;

/**
 * <p>Responsible for verifying and assembling datagram packets</p>
 */
public class SourcePacketHandler extends ChannelInboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(SourcePacketHandler.class);

    //TODO: We might need to check from time to time this map.
    /**
     * A container for split packets. Since we are dealing with multiple responses here,
     * we need to create a map for each type of response to avoid conflicts.
     */
    private Map<Integer, SplitPacketContainer> requestMap;

    public SourcePacketHandler() {
        this.requestMap = new ConcurrentHashMap<>();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.trace("SourcePacketHandler.channelRead() : START");

        //Make sure we are only receiving an instance of DatagramPacket
        if (!(msg instanceof DatagramPacket)) {
            ReferenceCountUtil.release(msg);
            return;
        }

        //REVIEW: Figure out why some request in the registry have no promise assigned to them
        //REVIEW: (e.g. No promise was assigned to request '101.99.86.58:29028:SourcePlayerRequestPacket' after retrieval)

        //Retrived the packet instance
        DatagramPacket packet = (DatagramPacket) msg;
        ByteBuf data = ((DatagramPacket) msg).content();

        if (log.isDebugEnabled()) {
            System.out.println(prettyHexDump(packet.content()));
        }

        //Verify size
        if (data.readableBytes() <= 5) {
            log.debug("Not a valid datagram for processing. Size getTotalRequests needs to be at least more than or equal to 5 bytes. Discarding. (Readable Bytes: {})", data.readableBytes());
            ((DatagramPacket) msg).release();
            return;
        }

        //Try to read protocol header, determine if its a single packet or a split-packet
        int protocolHeader = data.readIntLE();

        //If the packet arrived is single type, we can forward it to the next handler
        if (protocolHeader == 0xFFFFFFFF) {
            //Verify if packet is a valid source packet
            if (SourceMapper.isValidResponsePacket(data)) {
                log.debug("VALID HEADER, PASSING TO THE CHAIN");
                //Pass the message to the succeeding handlers
                ctx.fireChannelRead(packet.retain());
            }
        }
        //If the packet is a split type...further processing get needed
        else if (protocolHeader == 0xFFFFFFFE) {
            processSplitPackets(data, ctx, packet);
        }
        //Packet is not being handled by any of our processors, discard
        else {
            log.debug("Not a valid protocol header. Discarding. (Header Received: Dec = {}, Hex = {})", protocolHeader, Integer.toHexString(protocolHeader));
            ((DatagramPacket) msg).release();
        }
        log.trace("SourcePacketHandler.channelRead() : END");
    }

    private void processSplitPackets(ByteBuf data, ChannelHandlerContext ctx, DatagramPacket packet) {
        int packetCount, packetNumber, requestId, splitSize, packetChecksum = 0;
        boolean isCompressed = false;

        //Start processing
        requestId = data.readIntLE();
        //read the most significant bit is set
        isCompressed = ((requestId & 0x80000000) != 0);
        //The total number of packets in the response.
        packetCount = data.readByte();
        //The number of the packet. Starts at 0.
        packetNumber = data.readByte();

        log.debug("Split Packet Received = (Request {}, Packet Number {}, Packet Count {}, Is Compressed: {})", requestId, packetNumber, packetCount, isCompressed);

        //Retrieve the split packet map
        SplitPacketContainer splitPacketContainer = this.requestMap.get(requestId);

        //If request is not yet on the map, create and retrieve
        if (splitPacketContainer == null) {
            //Create our new split packet container
            splitPacketContainer = new SplitPacketContainer(packetCount);
            //Add it to the map
            this.requestMap.put(requestId, splitPacketContainer);
        }

        //As per protocol, the size is only present in the first packet of the response and only if the response is being compressed.
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
        splitPacketContainer.addPacket(packetNumber, splitPacket);

        //Have we received all packets for this request?
        if (splitPacketContainer.isComplete()) {
            log.debug("Split Packets have all been successfully received from Request {}. Re-assembling packets.", requestId);

            //Re-assemble the split packets
            ByteBuf reassembledPacket = reassembleSplitPackets(splitPacketContainer, ctx.channel().alloc(), isCompressed, splitSize, packetChecksum);

            //Discard the protocol header
            reassembledPacket.readIntLE();

            //Pass to the next handlers in the chain
            ctx.fireChannelRead(new DatagramPacket(reassembledPacket.retain(), packet.recipient(), packet.sender()));
        }
    }

    /**
     * Re-assemble's the packets from the container.
     *
     * @param container
     *
     * @return
     */
    private ByteBuf reassembleSplitPackets(SplitPacketContainer container, ByteBufAllocator allocator, boolean isCompressed, int decompressedSize, int packetChecksum) {
        log.trace("reassembleSplitPackets : START");

        int packetSize = container.getPacketSize();
        ByteBuf packetBuffer = allocator.buffer(packetSize);

        //Loop throgh each entry
        container.forEachEntry(packetEntry -> {
            log.debug("Packet #{} : {}", packetEntry.getKey(), packetEntry.getValue());

            //Throw exception if compression is set. Not yet supported.
            if (isCompressed)
                throw new IllegalStateException("Compression is not yet supported at this time sorry");

            //TODO: From Steam Condenser
            /*if(isCompressed) {
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

        return packetBuffer;
    }
}
