/*
 * Copyright 2022 Asynchronous Game Query Library
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

package com.ibasco.agql.protocols.valve.source.query.handlers;

import com.ibasco.agql.core.PacketDecoder;
import com.ibasco.agql.core.exceptions.IncompletePacketException;
import com.ibasco.agql.core.handlers.MessageInboundHandler;
import com.ibasco.agql.core.transport.ChannelAttributes;
import com.ibasco.agql.core.transport.enums.ChannelEvent;
import com.ibasco.agql.core.util.TransportOptions;
import com.ibasco.agql.protocols.valve.source.query.SourceQuery;
import com.ibasco.agql.protocols.valve.source.query.packets.SourceQuerySinglePacket;
import com.ibasco.agql.protocols.valve.source.query.packets.SourceQuerySplitPacket;
import com.ibasco.agql.protocols.valve.source.query.packets.util.SourceLazySplitPacketAssembler;
import com.ibasco.agql.protocols.valve.source.query.packets.util.SourceQueryPacketDecoderProvider;
import com.ibasco.agql.protocols.valve.source.query.packets.util.SourceSplitPacketAssembler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.ReadTimeoutException;

/**
 * Re-assembles {@link SourceQuerySplitPacket} instances and converts it back to a single-type {@link SourceQuerySinglePacket} instance.
 *
 * @author Rafael Luis Ibasco
 */
public class SourceQuerySplitPacketAssembler extends MessageInboundHandler {

    private SourceSplitPacketAssembler assembler;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        ensureNotSharable();
        this.assembler = new SourceLazySplitPacketAssembler(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        this.assembler = null;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //did we receive a timeout while we are still processing packets?
        if (cause instanceof ReadTimeoutException && (this.assembler != null && this.assembler.isProcessing())) {
            debug("A read timeout was fired but we are still receiving incoming packets from the server (Packets received: {}, Packets expected: {}, Request: {})", assembler.received(), assembler.count(), ctx.channel().attr(ChannelAttributes.REQUEST));
        }
        ctx.fireExceptionCaught(cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        try {
            switch ((ChannelEvent) evt) {
                case RELEASED:
                case CLOSED: {
                    checkAssemblerState(ctx);
                    break;
                }
            }
        } finally {
            ctx.fireUserEventTriggered(evt);
        }
    }

    private void checkAssemblerState(ChannelHandlerContext ctx) throws IncompletePacketException {
        Boolean throwOnIncomplete = TransportOptions.REPORT_INCOMPLETE_PACKET.attr(ctx);
        //if the channel was pre-maturely closed while we are still processing packets, make sure we reset it.
        if (this.assembler != null && this.assembler.isProcessing()) {
            debug("Channel has been pre-maturely released/closed and we have not received and processed the entire response from the server. " +
                         "Forcing reset of assembler (Packets received: {}, Packets expected: {})",
                 this.assembler.received(), this.assembler.count());
            this.assembler.reset();
            if (throwOnIncomplete) {
                warn("Throwing exception");
                throw new IncompletePacketException(assembler.received(), assembler.count(), assembler.dump());
            }
        }
    }

    @Override
    protected void readMessage(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof SourceQuerySplitPacket)) {
            debug("REJECTED '{}'", msg.getClass().getSimpleName());
            ctx.fireChannelRead(msg);
            return;
        }

        assert assembler != null;

        try {
            SourceQuerySplitPacket splitPacket = (SourceQuerySplitPacket) msg;
            if (!assembler.add(splitPacket))
                return;

            debug("=======================================================================================================================");
            debug("Collected the required amounts of split-packets. Attempting to re-assemble (Packet Size: {})", assembler.received());
            debug("=======================================================================================================================");
            final ByteBuf assembledPayload = assembler.getBuffer();
            final int packetType = assembledPayload.readIntLE();
            assert packetType == SourceQuery.SOURCE_PACKET_TYPE_SINGLE;
            PacketDecoder<SourceQuerySinglePacket> factory = SourceQueryPacketDecoderProvider.getDecoder(packetType);
            SourceQuerySinglePacket packet = factory.decode(assembledPayload);
            debug("=======================================================================================================================");
            debug("Successfully re-assembled split-packet to '{}'", packet);
            debug("=======================================================================================================================");
            ctx.fireChannelRead(packet.retain());
        } finally {
            if (assembler.isComplete())
                assembler.reset();
        }
    }
}
