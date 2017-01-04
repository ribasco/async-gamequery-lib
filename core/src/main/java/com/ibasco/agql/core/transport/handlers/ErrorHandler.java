/*
 * MIT License
 *
 * Copyright (c) 2016 Asynchronous Game Query Library
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

package com.ibasco.agql.core.transport.handlers;

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.exceptions.ResponseException;
import com.ibasco.agql.core.exceptions.TransportException;
import com.ibasco.agql.core.transport.ChannelAttributes;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorHandler extends ChannelDuplexHandler {
    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (log.isDebugEnabled()) {
            log.error("Unhandled exception caught within the pipeline {} for Channel {}, Id: {}", cause, ctx.channel(), ctx.channel().id());
            if (ctx.channel().hasAttr(ChannelAttributes.LAST_REQUEST_SENT)) {
                AbstractRequest request = ctx.channel().attr(ChannelAttributes.LAST_REQUEST_SENT).get();
                if (request != null && SocketChannel.class.isAssignableFrom(ctx.channel().getClass())) {
                    Throwable ex = new ResponseException(request, cause);
                    SimpleChannelInboundHandler responseRouter = ctx.pipeline().get(SimpleChannelInboundHandler.class);
                    responseRouter.channelRead(ctx, ex);
                    return;
                }
            }
            throw new TransportException(cause);
        }
    }
}
