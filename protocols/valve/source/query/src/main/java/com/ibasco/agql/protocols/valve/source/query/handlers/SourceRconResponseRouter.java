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

import com.ibasco.agql.core.Messenger;
import com.ibasco.agql.core.exceptions.ResponseException;
import com.ibasco.agql.protocols.valve.source.query.SourceRconRequest;
import com.ibasco.agql.protocols.valve.source.query.SourceRconResponse;
import com.ibasco.agql.protocols.valve.source.query.SourceRconResponsePacket;
import com.ibasco.agql.protocols.valve.source.query.exceptions.SourceRconExecutionException;
import com.ibasco.agql.protocols.valve.source.query.packets.response.SourceRconAuthResponsePacket;
import com.ibasco.agql.protocols.valve.source.query.packets.response.SourceRconCmdResponsePacket;
import com.ibasco.agql.protocols.valve.source.query.response.SourceRconAuthResponse;
import com.ibasco.agql.protocols.valve.source.query.response.SourceRconCmdResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.function.BiConsumer;

public class SourceRconResponseRouter extends SimpleChannelInboundHandler<Object> {

    private static final Logger log = LoggerFactory.getLogger(SourceRconResponseRouter.class);

    private final Messenger<SourceRconRequest, SourceRconResponse> messenger;

    public SourceRconResponseRouter(Messenger<SourceRconRequest, SourceRconResponse> responseCallback) {
        this.messenger = responseCallback;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        SourceRconResponse response = null;

        if (msg instanceof SourceRconAuthResponsePacket) {
            response = new SourceRconAuthResponse();
        } else if (msg instanceof SourceRconCmdResponsePacket) {
            response = new SourceRconCmdResponse();
        } else if (msg instanceof ResponseException) {
            log.debug("Received response exception. Passing to messenger");
            messenger.receive(null, (ResponseException) msg);
            return;
        }

        if (response != null) {
            SourceRconResponsePacket packet = (SourceRconResponsePacket) msg;
            response.setTransactionId(ctx.channel().id().asShortText());
            response.setSender((InetSocketAddress) ctx.channel().remoteAddress());
            response.setRecipient((InetSocketAddress) ctx.channel().localAddress());
            response.setRequestId(packet.getId());
            response.setResponsePacket(packet);
            log.debug("Response Processed. Sending back to the messenger : '{}={}'", response.getClass().getSimpleName(), response.sender());
            messenger.receive(response, null);
        } else
            messenger.receive(null, new SourceRconExecutionException("Invalid response received"));
    }
}
