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

import com.ibasco.agql.core.NettyChannelContext;
import io.netty.channel.Channel;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

/**
 * Channel context for the Rcon module
 *
 * @author Rafael Luis Ibasco
 */
@ApiStatus.Internal
public final class SourceRconChannelContext extends NettyChannelContext {

    private static final Logger log = LoggerFactory.getLogger(SourceRconChannelContext.class);

    private boolean authenticated;

    SourceRconChannelContext(Channel channel, final SourceRconMessenger messenger) {
        super(channel, messenger);
    }

    @Override
    public SourceRconMessenger messenger() {
        return (SourceRconMessenger) super.messenger();
    }

    @Override
    protected Properties newProperties(Properties copy) {
        if (copy instanceof RconProperties)
            return new RconProperties((RconProperties) copy);
        return new RconProperties();
    }

    @Override
    public CompletableFuture<SourceRconChannelContext> send() {
        //noinspection unchecked
        return super.send().thenCompose(NettyChannelContext::composedFuture);
    }

    @Override
    public RconProperties properties() {
        return (RconProperties) super.properties();
    }

    @Override
    public SourceRconChannelContext save() {
        return (SourceRconChannelContext) super.save();
    }

    @Override
    public SourceRconChannelContext restore() {
        return (SourceRconChannelContext) super.restore();
    }

    public static SourceRconChannelContext getContext(Channel channel) {
        return (SourceRconChannelContext) NettyChannelContext.getContext(channel);
    }

    public class RconProperties extends NettyChannelContext.Properties {

        public RconProperties() {
            SourceRconChannelContext.this.authenticated = false;
        }

        public RconProperties(RconProperties properties) {
            super(properties);
            SourceRconChannelContext.this.authenticated = false;
        }

        public boolean authenticated() {
            return SourceRconChannelContext.this.authenticated;
        }

        public void authenticated(boolean authenticated) {
            SourceRconChannelContext.this.authenticated = authenticated;
        }
    }
}