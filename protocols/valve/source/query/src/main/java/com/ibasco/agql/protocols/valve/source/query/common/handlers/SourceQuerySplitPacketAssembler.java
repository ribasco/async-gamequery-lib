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

package com.ibasco.agql.protocols.valve.source.query.common.handlers;

import com.ibasco.agql.core.NettyChannelContext;
import com.ibasco.agql.core.PacketDecoder;
import com.ibasco.agql.core.transport.enums.ChannelEvent;
import com.ibasco.agql.core.transport.handlers.MessageInboundHandler;
import com.ibasco.agql.protocols.valve.source.query.SourceQuery;
import com.ibasco.agql.protocols.valve.source.query.common.packets.SourceQuerySinglePacket;
import com.ibasco.agql.protocols.valve.source.query.common.packets.SourceQuerySplitPacket;
import com.ibasco.agql.protocols.valve.source.query.common.packets.util.SourceLazySplitPacketAssembler;
import com.ibasco.agql.protocols.valve.source.query.common.packets.util.SourceQueryPacketDecoderProvider;
import com.ibasco.agql.protocols.valve.source.query.common.packets.util.SourceSplitPacketAssembler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Re-assembles {@link SourceQuerySplitPacket} instances and converts it back to a single-type {@link SourceQuerySinglePacket} instance.
 *
 * @author Rafael Luis Ibasco
 */
public class SourceQuerySplitPacketAssembler extends MessageInboundHandler {

    private static final Logger log = LoggerFactory.getLogger(SourceQuerySplitPacketAssembler.class);

    private static final AttributeKey<SourceSplitPacketAssembler> ASSEMBLER = AttributeKey.valueOf("splitPacketAssembler");

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        ensureNotSharable();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        SourceSplitPacketAssembler assembler = getAssembler(ctx);
        //did we receive a timeout while we are still processing packets?
        if (assembler.isProcessing()) {
            NettyChannelContext context = NettyChannelContext.getContext(ctx.channel());
            debug(log, ctx, "An error was fired but we are still receiving incoming packets from the server (Error: {}, Packets received: {}, Packets expected: {}, Request: {})", cause.getClass().getSimpleName(), assembler.received(), assembler.count(), context.properties().envelope());
            assembler.reset();
        }
        ctx.fireExceptionCaught(cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        try {
            switch ((ChannelEvent) evt) {
                case ACQUIRED: {
                    debug(log, ctx, "Channel acquired. Creating new assembler for channel '{}'", ctx.channel());
                    //this.assembler = new SourceLazySplitPacketAssembler(ctx);
                    break;
                }
                case RELEASED:
                case CLOSED: {
                    debug(log, ctx, "Channel closed. Forcing reset of assembler", evt);
                    getAssembler(ctx).reset();
                    break;
                }
            }
        } finally {
            ctx.fireUserEventTriggered(evt);
        }
    }

    @Override
    protected void readMessage(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof SourceQuerySplitPacket)) {
            debug(log, ctx, "REJECTED '{}'", msg.getClass().getSimpleName());
            ctx.fireChannelRead(msg);
            return;
        }
        SourceSplitPacketAssembler assembler = getAssembler(ctx);
        SourceQuerySplitPacket splitPacket = (SourceQuerySplitPacket) msg;
        try {
            if (!assembler.add(splitPacket)) {
                debug(log, ctx, "[SPLIT-PACKET-ASSEMBLER] Added split-packet {} to assembler {}", splitPacket, assembler);
                return;
            } else {
                debug(log, ctx, "[SPLIT-PACKET-ASSEMBLER] Added last split-packet {} to assembler {}. Assembling packets.", splitPacket, assembler);
            }
            reassembleAndDecode(ctx);
        } catch (Exception ex) {
            error(log, ctx, "[SPLIT-PACKET-ASSEMBLER] An error occured while attempting to re-assemble split packets. Releasing split-packet and resetting assembler (Assembler complete: {})", assembler.isComplete(), ex);
            splitPacket.release();
            assembler.reset();
            throw ex;
        }
    }

    private void reassembleAndDecode(ChannelHandlerContext ctx) throws Exception {
        SourceSplitPacketAssembler assembler = getAssembler(ctx);
        debug(log, ctx, "=======================================================================================================================");
        debug(log, ctx, "Collected the required amounts of split-packets. Attempting to re-assemble (Packet Size: {})", assembler.received());
        debug(log, ctx, "=======================================================================================================================");
        final ByteBuf assembledPayload = assembler.getBuffer();
        final int packetType = assembledPayload.readIntLE();
        assert packetType == SourceQuery.SOURCE_PACKET_TYPE_SINGLE;

        PacketDecoder<SourceQuerySinglePacket> factory = SourceQueryPacketDecoderProvider.getDecoder(packetType);
        SourceQuerySinglePacket packet = factory.decode(assembledPayload);
        debug(log, ctx, "=======================================================================================================================");
        debug(log, ctx, "Successfully re-assembled split-packet to '{}'", packet);
        debug(log, ctx, "=======================================================================================================================");

        debug(log, ctx, "Passing assembled packet to the next handler: {}", packet);
        ctx.fireChannelRead(packet.retain());
        if (assembler.isComplete()) {
            debug(log, ctx, "Assembler now in completed state. Resetting");
            assembler.reset();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        getAssembler(ctx).reset();
        ctx.channel().attr(ASSEMBLER).set(null);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        debug(log, ctx, "Read Complete");
        super.channelReadComplete(ctx);
    }

    private SourceSplitPacketAssembler getAssembler(ChannelHandlerContext ctx) {
        Attribute<SourceSplitPacketAssembler> attr = ctx.channel().attr(ASSEMBLER);
        SourceSplitPacketAssembler assembler = attr.get();
        if (assembler == null) {
            assembler = new SourceLazySplitPacketAssembler(ctx);
            attr.set(assembler);
        }
        return assembler;
    }
}
