/*
 * Copyright 2022-2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.protocols.valve.source.query.packets.util;

import com.ibasco.agql.core.exceptions.PacketDecodeException;
import com.ibasco.agql.core.util.CompressUtil;
import com.ibasco.agql.core.util.NettyUtil;
import com.ibasco.agql.protocols.valve.source.query.packets.SourceQuerySinglePacket;
import com.ibasco.agql.protocols.valve.source.query.packets.SourceQuerySplitPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A lazy implementation of the {@link SourceSplitPacketAssembler}. Packets will only be re-assembled when {@link #getBuffer()} is
 * called and the expected number of packets have been received.
 *
 * @author Rafael Luis Ibasco
 */
public class SourceLazySplitPacketAssembler implements SourceSplitPacketAssembler {

    private static final Logger log = LoggerFactory.getLogger(SourceLazySplitPacketAssembler.class);

    private final Supplier<Stream<SourceQuerySplitPacket>> packetStream = () -> Arrays.stream(SourceLazySplitPacketAssembler.this.packets).filter(Objects::nonNull).sorted();

    private final ByteBufAllocator allocator;

    private SourceQuerySplitPacket[] packets;

    private int packetId = -1;

    private int maxPacketSize = -1;

    private int packetCount = -1;

    private int lastPacketNum = -1;

    private ByteBuf buffer;

    private boolean completed;

    private final ChannelHandlerContext ctx;

    public SourceLazySplitPacketAssembler(ChannelHandlerContext ctx) {
        this.allocator = Objects.requireNonNull(ctx.alloc());
        this.ctx = ctx;
    }

    @Override
    public boolean add(SourceQuerySplitPacket splitPacket) {
        Objects.requireNonNull(splitPacket, "Packet cannot be null");

        //container has been marked as completed and has not yet been reset.
        if (completed)
            throw new IllegalStateException("Container is in completed state. Make sure to explicitly call reset first");

        //should we initialize the array?
        if (this.packets == null)
            initialize(splitPacket);
        else {
            //is the packet within the same group we are currently collecting?
            if (splitPacket.getId() != packetId)
                throw new IllegalStateException(String.format("Rejected split-packet. Expected packet id of %d but was %d", packetId, splitPacket.getId()));
            //has this packet been added already?
            if (Arrays.stream(packets).filter(Objects::nonNull).anyMatch(splitPacket::equals))
                throw new IllegalStateException(String.format("Packet '%s' has already been added in this container", splitPacket));
        }

        //its already initialized
        log.debug("{} ASSEMBLER => Adding packet: {} (Last Packet Number Received: {}, Count: {})", NettyUtil.id(ctx), splitPacket, lastPacketNum, received());
        this.packets[splitPacket.getPacketNumber()] = splitPacket;
        this.lastPacketNum = splitPacket.getPacketNumber();
        if (splitPacket.getPacketCount() == received()) {
            this.completed = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean isComplete() {
        return this.completed;
    }

    @Override
    public ByteBuf getBuffer() {
        if (!this.completed)
            throw new IllegalStateException("Not yet in completed state");
        log.debug("{} ASSEMBLER => Container is now in completed state (Total packets received: {})", NettyUtil.id(ctx), received());
        try {
            validatePackets();
            this.buffer = reassemble();
            log.debug("{} ASSEMBLER => Packet container is now in a valid state (Buffer: {})", NettyUtil.id(ctx), this.buffer);
            return Objects.requireNonNull(this.buffer, "Buffer not yet available. Make sure to to check isComplete before accessing this method");
        } catch (PacketDecodeException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public int received() {
        if (this.packets == null)
            return 0;
        int count = 0;
        for (SourceQuerySplitPacket packet : this.packets) {
            if (packet != null)
                count++;
        }
        return count;
    }

    @Override
    public int count() {
        return this.packetCount;
    }

    @Override
    public void reset() {
        this.packets = null;
        this.packetId = -1;
        this.maxPacketSize = -1;
        this.packetCount = -1;
        this.lastPacketNum = -1;
        if (this.buffer != null)
            this.buffer.release();
        this.buffer = null;
        this.completed = false;
        log.debug("{} ASSEMBLER => Successfully reset assembler", NettyUtil.id(ctx));
    }

    @Override
    public List<ByteBuf> dump() {
        ArrayList<ByteBuf> packets = new ArrayList<>();
        if (this.packets == null)
            return packets;
        //initialize
        for (int i = 0; i < this.packets.length; i++)
            packets.add(null);
        for (SourceQuerySplitPacket packet : this.packets) {
            ByteBuf buf = Unpooled.copiedBuffer(packet.content());
            packets.set(packet.getId(), buf);
        }
        return packets;
    }

    /**
     * Re-assembles a collection of {@link SourceQuerySplitPacket} and converts it into a {@link SourceQuerySinglePacket} instance.
     *
     * @return A {@link SourceQuerySinglePacket} containing the assembled packets
     *
     * @throws PacketDecodeException
     *         If an error occurs during the decoding process
     */
    private ByteBuf reassemble() throws PacketDecodeException {
        int totalPacketSize = packetStream.get().mapToInt(SourceQuerySplitPacket::getPacketSize).sum();
        int maxBufferSize = this.maxPacketSize * this.packetCount;
        log.debug("{} ASSEMBLER => reassemble(-1): Total Size of received packets: {} (Max: {})", NettyUtil.id(ctx), totalPacketSize, maxBufferSize);
        assert totalPacketSize > 0;
        ByteBuf buf = allocator.directBuffer(totalPacketSize);
        log.debug("{} ASSEMBLER => reassemble(-1): Allocated single-type packet size to {} bytes", NettyUtil.id(ctx), totalPacketSize);
        for (int index = 0, packetsLength = packets.length; index < packetsLength; index++) {
            SourceQuerySplitPacket packet = packets[index];
            assert packet.content() != null;
            try {
                if (packet.isCompressed()) {
                    ByteBuf payload = null;
                    try {
                        payload = decompress(packet);
                        buf.writeBytes(payload);
                    } catch (IOException e) {
                        throw new PacketDecodeException(String.format("Failed to decompress packet '%s'", packet), e);
                    } finally {
                        if (payload != null) {
                            payload.release();
                            log.debug("{} ASSEMBLER => reassemble(): Released allocated buffer used for decompression (Packet: {})", NettyUtil.id(ctx), packet);
                        }
                    }
                } else {
                    buf.writeBytes(packet.content());
                }
                log.debug("{} ASSEMBLER => reassemble({}): {} (Remaining: {})", NettyUtil.id(ctx), index, packet, buf.writableBytes());
            } finally {
                packet.release();
            }
        }
        return buf;
    }

    /**
     * Decompress {@link SourceQuerySplitPacket}. Please note that for decompression to work, the value returned by {@link SourceQuerySplitPacket#isCompressed()} should be {@code true}.
     *
     * @param packet
     *         The split-type packet to decompress.
     *
     * @return A heap-based {@link ByteBuf} instance containing the decompressed payload of the packet.
     *
     * @throws IOException
     *         If the payload could not be decompressed or a CRC32 checksum mismatch occurs
     * @throws IllegalStateException
     *         if the packet payload is null or not compressed
     */
    private ByteBuf decompress(SourceQuerySplitPacket packet) throws IOException {
        if (packet == null)
            throw new NullPointerException("Packet is null");
        if (packet.content() == null)
            throw new IllegalStateException("Packet's payload is null");
        if (!packet.isCompressed())
            throw new IllegalStateException("Packet is not compressed");

        assert packet.getDecompressedSize() != null;
        assert packet.getCrcChecksum() != null;

        ByteBuf payload = packet.content();
        ByteBufAllocator alloc = payload.alloc();
        try (BZip2CompressorInputStream bzip2 = new BZip2CompressorInputStream(new ByteBufInputStream(payload))) {
            //start decompression
            ByteBuf dcPayload = alloc.directBuffer(packet.getDecompressedSize());
            //store the decompressed data into dcPayload's backing buffer
            if (bzip2.read(dcPayload.array(), 0, dcPayload.writableBytes()) > 0) {
                //Verify checksum
                if (CompressUtil.getCrc32Checksum(dcPayload.array()) != packet.getCrcChecksum())
                    throw new IOException("CRC32 checksum mismatch");
            } else {
                throw new IOException("Failed to decompress packet data. Possibly corrupted");
            }
            return dcPayload;
        }
    }

    private void validatePackets() {
        Objects.requireNonNull(this.packets, "Packet container is null");
        if (this.packets.length == 0)
            throw new IllegalStateException("Packet container is empty. No split-packets were received?");
        //ensure that we have received everything
        List<Integer> emptyIndices = IntStream.range(0, this.packets.length - 1).filter(index -> this.packets[index] == null).boxed().collect(Collectors.toList());
        if (!emptyIndices.isEmpty())
            throw new IllegalStateException(String.format("Found missing split-packets within the container (Missing packet numbers: '%s')", emptyIndices.stream().map(String::valueOf).collect(Collectors.joining(", "))));
        //verify packet size
        final int maxBufferSize = this.maxPacketSize * this.packetCount;
        final int totalBytesReceived = packetStream.get().mapToInt(SourceQuerySplitPacket::getPacketSize).sum();
        if (totalBytesReceived > maxBufferSize)
            throw new IllegalStateException(String.format("The total bytes received is larger than the maximum allowable size (Max: %d, Actual: %d)", maxBufferSize, totalBytesReceived));

        log.debug("{} ASSEMBLER => All packets have been received and are in a valid state", NettyUtil.id(ctx));
    }

    private void initialize(SourceQuerySplitPacket packet) {
        if (completed || this.packets != null)
            throw new IllegalStateException("Not yet in completed state");
        log.debug("{} ASSEMBLER => Initializing packet container", NettyUtil.id(ctx));
        this.packets = new SourceQuerySplitPacket[packet.getPacketCount()];
        this.packetId = packet.getId();
        this.maxPacketSize = packet.getPacketMaxSize();
        this.packetCount = packet.getPacketCount();
        this.buffer = null;
    }

    private void ensureInitialized() {
        if (this.packets == null)
            throw new IllegalStateException("Assembler not yet initialized. Please make sure to call initialize first");
    }
}
