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

package com.ibasco.agql.protocols.valve.source.query;

import com.ibasco.agql.core.NettyMessenger;
import com.ibasco.agql.core.transport.DefaultChannlContextFactory;
import com.ibasco.agql.core.transport.NettyChannelFactory;
import com.ibasco.agql.core.transport.NettyContextChannelFactory;
import com.ibasco.agql.core.transport.enums.ChannelPoolType;
import com.ibasco.agql.core.transport.enums.TransportType;
import com.ibasco.agql.core.util.Options;
import com.ibasco.agql.core.util.Platform;
import com.ibasco.agql.core.util.TransportOptions;
import com.ibasco.agql.protocols.valve.source.query.message.SourceQueryRequest;
import com.ibasco.agql.protocols.valve.source.query.message.SourceQueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Messenger implementation for the Source Query Protocol
 *
 * @author Rafael Luis Ibasco
 */
public final class SourceQueryMessenger extends NettyMessenger<SourceQueryRequest, SourceQueryResponse> {

    private static final Logger log = LoggerFactory.getLogger(SourceQueryMessenger.class);

    public SourceQueryMessenger(Options options) {
        super(options);
    }

    @Override
    protected void configure(Options options) {
        //enable pooling by default
        defaultOption(options, TransportOptions.CONNECTION_POOLING, true);
        defaultOption(options, TransportOptions.POOL_TYPE, ChannelPoolType.FIXED);
        defaultOption(options, TransportOptions.POOL_MAX_CONNECTIONS, Platform.getDefaultPoolSize());
        defaultOption(options, TransportOptions.READ_TIMEOUT, 10000);
    }

    @Override
    protected NettyChannelFactory newChannelFactory() {
        NettyContextChannelFactory channelFactory = getFactoryProvider().getContextualFactory(TransportType.UDP_CONNLESS, getOptions(), new DefaultChannlContextFactory(this));
        return new SourceQueryChannelFactory(channelFactory);
    }

}
