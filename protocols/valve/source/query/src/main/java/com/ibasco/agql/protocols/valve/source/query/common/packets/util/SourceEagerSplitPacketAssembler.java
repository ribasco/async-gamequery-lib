/*
 * Copyright (c) 2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.protocols.valve.source.query.common.packets.util;

import com.ibasco.agql.core.util.Netty;
import com.ibasco.agql.protocols.valve.source.query.common.packets.SourceQuerySplitPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Eager implementation of the {@link SourceSplitPacketAssembler}. The split-packet is decoded on-the-fly and the partial packet is then placed into a pre-allocated buffer
 * whose size is determined based on the initial parameters provided by the incoming split-packet(s). The pre-allocated buffer can then
 * be used as soon as it is marked as completed. Make sure to call {@link #reset()} after consuming the buffer.
 *
 * @author Rafael Luis Ibasco
 */
public class SourceEagerSplitPacketAssembler implements SourceSplitPacketAssembler {

    private static final Logger log = LoggerFactory.getLogger(SourceEagerSplitPacketAssembler.class);

    private SourceQuerySplitPacket[] packets;

    private int packetId = -1;

    private int maxPacketSize = -1;

    private int packetCount = -1;

    private int lastPacketNum = -1;

    private ByteBuf buffer;

    private boolean completed;

    private ByteBufAllocator allocator;

    private ChannelHandlerContext ctx;

    private int lastOffset = -1;

    public SourceEagerSplitPacketAssembler(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        this.allocator = ctx.alloc();
    }

    @Override
    public boolean add(SourceQuerySplitPacket splitPacket) {
        Objects.requireNonNull(splitPacket, "Packet cannot be null");

        if (splitPacket.isCompressed())
            throw new IllegalStateException("Compressed packet is not supported in this implementation");

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
        log.debug("{} ASSEMBLER => Adding packet: {} (Last Packet Number Received: {}, Count: {})", Netty.id(ctx), splitPacket, lastPacketNum, received());

        this.packets[splitPacket.getPacketNumber()] = splitPacket;
        this.lastPacketNum = splitPacket.getPacketNumber();

        assert buffer != null;
        int offset = splitPacket.getPacketNumber() * splitPacket.getPacketMaxSize();
        buffer.writerIndex(offset);
        buffer.writeBytes(splitPacket.content());
        log.debug("{} ASSEMBLER => Writing packet to offset: {}", Netty.id(ctx), offset);
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
        log.debug("{} ASSEMBLER => Successfully reset assembler", Netty.id(ctx));
    }

    private void initialize(SourceQuerySplitPacket packet) {
        if (completed || this.packets != null)
            throw new IllegalStateException("Not yet in completed state");
        log.debug("{} ASSEMBLER => Initializing packet container", Netty.id(ctx));
        this.packets = new SourceQuerySplitPacket[packet.getPacketCount()];
        this.packetId = packet.getId();
        this.maxPacketSize = packet.getPacketMaxSize();
        this.packetCount = packet.getPacketCount();
        //pre-allocate buffer
        int bufferSize = maxPacketSize * packetCount;
        this.buffer = allocator.directBuffer(bufferSize);
        log.debug("{} ASSEMBLER => Pre-allocated buffer with {} bytes", Netty.id(ctx), bufferSize);
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
}
