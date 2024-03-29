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

import com.ibasco.agql.core.exceptions.WriteTimeoutException;
import com.ibasco.agql.core.util.Netty;
import io.netty.channel.ChannelHandlerContext;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>WriteTimeoutHandler class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class WriteTimeoutHandler extends io.netty.handler.timeout.WriteTimeoutHandler {

    private static final Logger log = LoggerFactory.getLogger(WriteTimeoutHandler.class);

    private boolean timeoutFired;

    /**
     * <p>Constructor for WriteTimeoutHandler.</p>
     *
     * @param timeout
     *         a long
     * @param unit
     *         a {@link java.util.concurrent.TimeUnit} object
     */
    public WriteTimeoutHandler(long timeout, TimeUnit unit) {
        super(timeout, unit);
    }

    /** {@inheritDoc} */
    @Override
    protected void writeTimedOut(ChannelHandlerContext ctx) throws Exception {
        if (!timeoutFired) {
            log.debug("{} OUT => Firing WriteTimeoutException", Netty.id(ctx.channel()));
            ctx.fireExceptionCaught(WriteTimeoutException.INSTANCE);
            timeoutFired = true;
        }
    }
}
