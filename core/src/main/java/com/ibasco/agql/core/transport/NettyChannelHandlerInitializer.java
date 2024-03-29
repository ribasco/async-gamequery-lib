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

package com.ibasco.agql.core.transport;

import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOutboundHandler;
import java.util.LinkedList;

/**
 * Allows ordered registration of inbound and outbound handlers for newly created channels
 *
 * @author Rafael Luis Ibasco
 */
public interface NettyChannelHandlerInitializer {

    //From HEAD to TAIL

    /**
     * <p>registerInboundHandlers.</p>
     *
     * @param handlers
     *         a {@link java.util.LinkedList} object
     */
    void registerInboundHandlers(final LinkedList<ChannelInboundHandler> handlers);

    //From TAIL to HEAD

    /**
     * <p>registerOutboundHandlers.</p>
     *
     * @param handlers
     *         a {@link java.util.LinkedList} object
     */
    void registerOutboundHandlers(final LinkedList<ChannelOutboundHandler> handlers);
}
