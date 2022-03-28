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

package com.ibasco.agql.protocols.valve.source.query.handlers;

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.transport.enums.ChannelEvent;
import com.ibasco.agql.core.transport.handlers.MessageInboundDecoder;
import com.ibasco.agql.core.util.ByteUtil;
import com.ibasco.agql.protocols.valve.source.query.SourceRcon;
import com.ibasco.agql.protocols.valve.source.query.SourceRconOptions;
import com.ibasco.agql.protocols.valve.source.query.message.SourceRconCmdRequest;
import com.ibasco.agql.protocols.valve.source.query.packets.SourceRconPacket;
import com.ibasco.agql.protocols.valve.source.query.packets.SourceRconPacketFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Re-assembles all {@link SourceRconPacket} split-packet back into a single {@link SourceRconPacket} instance.
 *
 * <blockquote>
 * <h4>Note</h4>
 * When terminator packets are enabled, we expect to receive two terminator packets with request id of -1 and with terminator byte of 0 then 1.
 * This implementation expects only to receive a SINGLE terminator packet, because there are cases when an unauthenticated request is sent, then the server will only send a single terminator packet (with terminator value of 0) instead of two.
 * </blockquote>
 *
 * @author Rafael Luis Ibasco
 * @see SourceRconOptions#USE_TERMINATOR_PACKET
 */
public class SourceRconPacketAssembler extends MessageInboundDecoder {

    private Deque<SourceRconPacket> splitPackets = new ArrayDeque<>();

    private boolean markedForConsolidation;

    @Override
    protected boolean acceptMessage(AbstractRequest request, Object msg) {
        //make sure the originating request is a 'Execute Command' request
        if (!(request instanceof SourceRconCmdRequest))
            return false;
        if (!(msg instanceof SourceRconPacket))
            return false;
        final SourceRconPacket packet = (SourceRconPacket) msg;
        //setSuppressLog(true);
        //make sure to only accept terminator or response value packets
        return SourceRcon.isTerminatorPacket(packet) || SourceRcon.isResponseValuePacket(packet);
    }

    private int counter;

    @Override
    protected Object decodeMessage(ChannelHandlerContext ctx, AbstractRequest request, Object msg) {
        assert msg instanceof SourceRconPacket;
        final SourceRconPacket packet = (SourceRconPacket) msg;
        debug("ASSEMBLER: START");
        if (markedForConsolidation) {
            debug("Marked for consolidation. Returning : {}", msg);
            return null;
        }

        //discard terminator packets (should be released automatically)
        if (SourceRcon.terminatorPacketEnabled(ctx) && SourceRcon.isTerminatorPacket(packet)) {
            assert SourceRcon.isInitialTerminatorPacket(packet);
            //should we mark for consolidation?
            debug("Received initial terminator packet '{}' ({}). Marked for consolidation.", packet.getTerminator(), ByteUtil.toHexString(packet.getTerminator()));
            markedForConsolidation = true;
            return null;
        } else {
            //only collect response value packets with id > 0
            if (packet.getId() > 0) {
                //make sure to call retain so the decoder does not automatically release on return
                container().addLast(ReferenceCountUtil.retain(packet));
                debug("{}) Added Packet to container: '{}'", ++counter, msg);
            } else {
                debug("Skipping packet '{}'", packet);
            }
        }
        return null;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //if terminator packets are enabled, check terminate flag,
        // if set then we should start consolidating
        try {
            debug("ASSEMBLER: END (Checking if there are packets that needs to be assembled)");
            SourceRconPacket decoded;
            if (SourceRcon.terminatorPacketEnabled(ctx)) {
                if (markedForConsolidation) {
                    try {
                        debug("Terminate flag set. Attemping to decode/assemble packet(s)");
                        decoded = decodePacket(ctx);
                    } finally {
                        debug("Terminate flag reset");
                        markedForConsolidation = false;
                    }
                } else {
                    debug("Terminate flag not set. Do not decode/assemble.");
                    decoded = null;
                }
            } else {
                debug("Terminator packets disabled by configuration. Attempting to decode/assemble packet(s)");
                decoded = decodePacket(ctx);
            }
            //did we decode something?
            if (decoded != null) {
                debug("Decoded/Re-assembled packet '{}'. Passing to next handler", decoded);
                ctx.fireChannelRead(decoded);
            } else
                debug("Nothing to decode/assemble. Skipping");
        } finally {
            ctx.fireChannelReadComplete();
        }
    }

    private SourceRconPacket decodePacket(ChannelHandlerContext ctx) {
        final Deque<SourceRconPacket> container = container();

        //Is the container empty? Reset and return immediately
        if (container.isEmpty()) {
            debug("decodePacket(1) : Resetting container");
            reset();
            counter = 0;
            return null;
        }

        try {
            //make sure we have more than 1 packets to re-assemble, otherwise just pass the packet to the next handler
            int packetCount = container.size();
            if (packetCount == 1) {
                SourceRconPacket singlePacket = container.removeFirst();
                debug("Received only a single-packet ({}). Passing to the next handler(s)", singlePacket);
                return singlePacket;
            } else {
                final SourceRconPacket reassembledPacket = reassemble(ctx);
                debug("Passing rcon packet '{}' to the next handler(s)", reassembledPacket);
                return reassembledPacket;
            }
        } finally {
            debug("decodePacket(2) : Resetting container");
            reset();
            counter = 0;
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (!(evt instanceof ChannelEvent)) {
            ctx.fireUserEventTriggered(evt);
            return;
        }
        ChannelEvent event = (ChannelEvent) evt;
        if (ChannelEvent.RELEASED.equals(event) || ChannelEvent.CLOSED.equals(event))
            reset();
        markedForConsolidation = false;
    }

    @Override
    public void channelActive(@NotNull ChannelHandlerContext ctx) throws Exception {
        debug("channelActive() : Resetting container");
        reset();
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(@NotNull ChannelHandlerContext ctx) throws Exception {
        debug("channelInactive() : Resetting container");
        reset();
        ctx.fireChannelInactive();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        debug("exceptionCaught() : Resetting container");
        reset();
        ctx.fireExceptionCaught(cause);
    }

    /**
     * Consolidate multiple {@link SourceRconPacket} instances into one {@link SourceRconPacket}
     *
     * @param ctx
     *         The {@link ChannelHandlerContext} currently associated with this handler
     *
     * @return A {@link SourceRconPacket} containing the consolidated payload
     */
    private SourceRconPacket reassemble(ChannelHandlerContext ctx) {
        try {
            final Deque<SourceRconPacket> container = container();

            //validate packets
            ensureValidState(container);
            debug("Re-assembling {} split-packet(s)", container.size());

            int totalPayloadSize = 0;
            int packetCtr = 0;
            Integer id = null;

            //thank the netty gods for CompositeByteBuf
            CompositeByteBuf payload = ctx.alloc().compositeDirectBuffer(container.size());

            //start assembling
            SourceRconPacket packet;
            while ((packet = container.pollFirst()) != null) {
                //Get the id of the first packet
                if (id == null)
                    id = packet.getId();

                //we shouldn't include the null terminating byte during consolidation, so we slice it
                int length = container.peekFirst() != null ? packet.content().capacity() - 1 : packet.content().capacity();
                ByteBuf data = packet.content().slice(0, length);
                totalPayloadSize += data.capacity();

                debug("({}) Id: {}, Type: {}, Packet Size: {}, Payload Size: {}, Payload Capacity: {}", String.format("%03d", ++packetCtr), packet.getId(), packet.getType(), packet.getSize(), packet.content().readableBytes(), packet.content().capacity());
                payload.addComponent(true, data);
            }

            assert container.isEmpty();
            assert payload.readableBytes() == totalPayloadSize;

            debug("Successfully re-assembled {} packet(s) with a total of {} bytes", packetCtr, payload.readableBytes());
            if (id == null)
                throw new IllegalStateException("No id is present");

            return SourceRconPacketFactory.createResponseValue(id, payload.consolidate().clear());
        } finally {
            debug("reassemble() : Resetting container");
            reset();
        }
    }

    private void ensureValidState(Deque<SourceRconPacket> packets) {
        if (packets == null || packets.isEmpty())
            throw new IllegalStateException("Split packet container is null or empty");
        int firstId = packets.peekFirst().getId();
        List<Integer> ids = packets.stream().mapToInt(SourceRconPacket::getId).distinct().boxed().collect(Collectors.toList());
        if (ids.size() > 1) {
            throw new IllegalStateException(String.format("Not all split-packets share the same id (Expected: %d, Actual: %s)", firstId, ids.stream().map(String::valueOf).collect(Collectors.joining(", "))));
        }
    }

    private Deque<SourceRconPacket> container() {
        if (this.splitPackets == null) {
            this.splitPackets = new ArrayDeque<>();
            debug("Initialized split-packet container");
        }
        return this.splitPackets;
    }

    private void reset() {
        try {
            if (splitPackets == null)
                return;
            //release
            SourceRconPacket packet;
            while ((packet = splitPackets.pollFirst()) != null) {
                packet.release();
            }
            splitPackets = null;
            debug("Split packet container has been reset");
        } catch (Throwable e) {
            debug("Failed to reset split packet container", e);
        }
    }
}
