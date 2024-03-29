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

package com.ibasco.agql.core.transport.handlers;

import com.ibasco.agql.core.NettyChannelContext;
import com.ibasco.agql.core.exceptions.ReadTimeoutException;
import com.ibasco.agql.core.util.Netty;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>ReadTimeoutHandler class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class ReadTimeoutHandler extends IdleStateHandler {

    private static final Logger log = LoggerFactory.getLogger(ReadTimeoutHandler.class);

    private boolean timeoutFired;

    /**
     * <p>Constructor for ReadTimeoutHandler.</p>
     *
     * @param timeout
     *         a long
     * @param unit
     *         a {@link java.util.concurrent.TimeUnit} object
     */
    public ReadTimeoutHandler(long timeout, TimeUnit unit) {
        super(timeout, 0, 0, unit);
    }

    /** {@inheritDoc} */
    @Override
    protected final void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        assert evt.state() == IdleState.READER_IDLE;
        NettyChannelContext context = NettyChannelContext.getContext(ctx.channel());
        //do not throw timeout exceptions for channels with no request associated
        if (context.properties().envelope() == null || context.properties().responsePromise().isDone()) {
            log.debug("{} INB => No request associated with channel. Not firing timeout", Netty.id(ctx.channel()));
            return;
        }
        readTimedOut(ctx);
    }

    /**
     * <p>readTimedOut.</p>
     *
     * @param ctx
     *         a {@link io.netty.channel.ChannelHandlerContext} object
     *
     * @throws java.lang.Exception
     *         if any.
     */
    protected void readTimedOut(ChannelHandlerContext ctx) throws Exception {
        if (!timeoutFired) {
            log.debug("{} INB => Firing ReadTimeoutException (Time: {} ms)", Netty.id(ctx.channel()), getReaderIdleTimeInMillis());
            ctx.fireExceptionCaught(ReadTimeoutException.INSTANCE);
            timeoutFired = true;
        }
    }
}
