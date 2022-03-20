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

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.AbstractResponse;
import com.ibasco.agql.core.NettyChannelContext;
import com.ibasco.agql.core.NettyMessenger;
import io.netty.channel.Channel;

public class DefaultChannlContextFactory<M extends NettyMessenger<? extends AbstractRequest, ? extends AbstractResponse>> implements NettyChannelContextFactory {

    private final M messenger;

    public DefaultChannlContextFactory(M messenger) {
        this.messenger = messenger;
    }

    @Override
    public final NettyChannelContext create(Channel channel) {
        return newChannelContext(channel, this.messenger);
    }

    public final M getMessenger() {
        return messenger;
    }

    protected NettyChannelContext newChannelContext(Channel channel, M messenger) {
        return new NettyChannelContext(channel, messenger);
    }
}
