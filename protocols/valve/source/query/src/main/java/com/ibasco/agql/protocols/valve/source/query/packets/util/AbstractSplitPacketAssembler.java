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

import com.ibasco.agql.core.util.CompressUtil;
import com.ibasco.agql.core.util.NettyUtil;
import com.ibasco.agql.protocols.valve.source.query.packets.SourceQuerySplitPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

abstract public class AbstractSplitPacketAssembler implements SourceSplitPacketAssembler {

    private static final Logger log = LoggerFactory.getLogger(AbstractSplitPacketAssembler.class);

    private SourceQuerySplitPacket[] packets;

    private int packetId = -1;

    private int maxPacketSize = -1;

    private int packetCount = -1;

    private int lastPacketNum = -1;

    private ByteBuf buffer;

    private boolean completed;

    private ByteBufAllocator allocator;

    private ChannelHandlerContext ctx;

    protected AbstractSplitPacketAssembler(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        this.allocator = ctx.alloc();
    }

    @Override
    public boolean isComplete() {
        return this.completed;
    }

    @Override
    public ByteBuf getBuffer() {
        return this.buffer;
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

    protected void initialize(SourceQuerySplitPacket packet) {
        if (completed || this.packets != null)
            throw new IllegalStateException("Not yet in completed state");
        log.debug("{} ASSEMBLER => Initializing packet container", NettyUtil.id(ctx));
        this.packets = new SourceQuerySplitPacket[packet.getPacketCount()];
        this.packetId = packet.getId();
        this.maxPacketSize = packet.getPacketMaxSize();
        this.packetCount = packet.getPacketCount();
        //pre-allocate buffer
        int bufferSize = maxPacketSize * packetCount;
        this.buffer = allocator.directBuffer(bufferSize);
        log.debug("{} ASSEMBLER => Pre-allocated buffer with {} bytes", NettyUtil.id(ctx), bufferSize);
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
    protected ByteBuf decompress(final SourceQuerySplitPacket packet) throws IOException {
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
        try (ByteBufInputStream stream = new ByteBufInputStream(payload); BZip2CompressorInputStream bzip2 = new BZip2CompressorInputStream(stream)) {
            //note: skip 2 bytes?
            //start decompression
            ByteBuf dcPayload = alloc.buffer(packet.getDecompressedSize());
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

    protected ByteBufAllocator getAllocator() {
        return this.allocator;
    }
}
