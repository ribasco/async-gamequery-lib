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

import com.ibasco.agql.core.NettyChannelContext;
import com.ibasco.agql.core.PacketDecoder;
import com.ibasco.agql.core.exceptions.IncompletePacketException;
import com.ibasco.agql.core.transport.enums.ChannelEvent;
import com.ibasco.agql.core.transport.handlers.MessageInboundHandler;
import com.ibasco.agql.core.util.TransportOptions;
import com.ibasco.agql.protocols.valve.source.query.SourceQuery;
import com.ibasco.agql.protocols.valve.source.query.packets.SourceQuerySinglePacket;
import com.ibasco.agql.protocols.valve.source.query.packets.SourceQuerySplitPacket;
import com.ibasco.agql.protocols.valve.source.query.packets.util.SourceLazySplitPacketAssembler;
import com.ibasco.agql.protocols.valve.source.query.packets.util.SourceQueryPacketDecoderProvider;
import com.ibasco.agql.protocols.valve.source.query.packets.util.SourceSplitPacketAssembler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Re-assembles {@link SourceQuerySplitPacket} instances and converts it back to a single-type {@link SourceQuerySinglePacket} instance.
 *
 * @author Rafael Luis Ibasco
 */
public class SourceQuerySplitPacketAssembler extends MessageInboundHandler {

    private static final Logger log = LoggerFactory.getLogger(SourceQuerySplitPacketAssembler.class);

    private SourceSplitPacketAssembler assembler;

    /*private static final AttributeKey<SourceSplitPacketAssembler> ASSEMBLER = AttributeKey.valueOf("splitPacketAssembler");

    private SourceSplitPacketAssembler getAssembler(ChannelHandlerContext ctx) {
        return ctx.channel().attr(ASSEMBLER).setIfAbsent(new SourceLazySplitPacketAssembler(ctx));
    }*/

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        ensureNotSharable();
        debug(log, ctx, "Initializing split-packet assembler");
        this.assembler = new SourceLazySplitPacketAssembler(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        debug(log, ctx, "De-allocating split-packet assembler");
        this.assembler.reset();
        this.assembler = null;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //did we receive a timeout while we are still processing packets?
        if (this.assembler != null && this.assembler.isProcessing()) {
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
                case RELEASED:
                case CLOSED: {
                    debug(log, ctx, "Channel event occured '{}'. Forcing reset of assembler", evt);
                    //checkAssemblerState(ctx);
                    this.assembler.reset();
                    break;
                }
            }
        } finally {
            ctx.fireUserEventTriggered(evt);
        }
    }

    private void checkAssemblerState(ChannelHandlerContext ctx) throws IncompletePacketException {
        Boolean throwOnIncomplete = TransportOptions.REPORT_INCOMPLETE_PACKET.attr(ctx);
        if (assembler != null)
            debug(log, ctx, "Assembler: {}, Complete: {}, Processing: {}, Received packets: {}", assembler, assembler.isComplete(), assembler.isProcessing(), assembler.received());
        else
            debug(log, ctx, "Assembler not available");
        //if the channel was pre-maturely closed while we are still processing packets, make sure we reset it.
        if (this.assembler != null && this.assembler.isProcessing()) {
            error(log, ctx, "Channel has been pre-maturely released/closed and we have not received and processed the entire response from the server. " +
                    "Forcing reset of assembler (Packets received: {}, Packets expected: {})", this.assembler.received(), this.assembler.count());
            this.assembler.reset();
            if (throwOnIncomplete != null && throwOnIncomplete)
                throw new IncompletePacketException(assembler.received(), assembler.count(), assembler.dump());
        }
    }

    @Override
    protected void readMessage(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof SourceQuerySplitPacket)) {
            debug(log, ctx, "REJECTED '{}'", msg.getClass().getSimpleName());
            ctx.fireChannelRead(msg);
            return;
        }
        assert assembler != null;
        SourceQuerySplitPacket splitPacket = (SourceQuerySplitPacket) msg;
        try {
            debug(log, ctx, "Collecting split-packet: {}", splitPacket);
            if (!assembler.add(splitPacket)) {
                return;
            }
            debug(log, ctx, "Collected all split-packets. Last packet received: {}", splitPacket);
            reassembleAndDecode(ctx);
        } catch (Exception ex) {
            error(log, ctx, "An error occured while attempting to re-assemble split packets. Releasing split-packet and resetting assembler (Assembler complete: {})", assembler.isComplete(), ex);
            splitPacket.release();
            assembler.reset();
            throw ex;
        }
    }

    private void reassembleAndDecode(ChannelHandlerContext ctx) throws Exception {
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
        assembler.reset();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        debug(log, ctx, "Read Complete");
        super.channelReadComplete(ctx);
    }
}
