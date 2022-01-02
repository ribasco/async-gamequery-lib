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

package com.ibasco.agql.core.handlers;

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.Envelope;
import com.ibasco.agql.core.transport.ChannelAttributes;
import com.ibasco.agql.core.util.NettyUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ReadTimeoutHandler extends IdleStateHandler {

    private static final Logger log = LoggerFactory.getLogger(ReadTimeoutHandler.class);

    private boolean timeoutFired;

    public ReadTimeoutHandler(long timeout, TimeUnit unit) {
        super(timeout, 0, 0, unit);
    }

    @Override
    protected final void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        assert evt.state() == IdleState.READER_IDLE;
        //do not throw timeout exceptions for channels with no request associated
        if (ctx.channel().hasAttr(ChannelAttributes.REQUEST) && ctx.channel().attr(ChannelAttributes.REQUEST).get() == null) {
            log.debug("{} INB => No request associated with channel. Not firing timeout", NettyUtil.id(ctx.channel()));
            return;
        }
        readTimedOut(ctx, ctx.channel().attr(ChannelAttributes.REQUEST).get());
    }

    protected void readTimedOut(ChannelHandlerContext ctx, Envelope<AbstractRequest> request) throws Exception {
        if (!timeoutFired) {
            log.debug("{} INB => Firing ReadTimeoutException (Time: {} ms)", NettyUtil.id(ctx.channel()), getReaderIdleTimeInMillis());
            ctx.fireExceptionCaught(ReadTimeoutException.INSTANCE);
            timeoutFired = true;
        }
    }
}
