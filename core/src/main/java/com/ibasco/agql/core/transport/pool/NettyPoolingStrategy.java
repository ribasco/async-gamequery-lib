/*
 * Copyright 2022-2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.core.transport.pool;

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.Envelope;
import com.ibasco.agql.core.Message;
import io.netty.channel.pool.ChannelPool;

@FunctionalInterface
public interface NettyPoolingStrategy {

    /**
     * A new {@link ChannelPool} instance will be created using {@link AbstractRequest} as key
     */
    NettyPoolingStrategy MESSAGE_TYPE = envelope -> envelope.content().getClass();

    /**
     * A new {@link ChannelPool} instance will be created for each unique socket address
     */
    NettyPoolingStrategy ADDRESS = Envelope::recipient;

    Object extractKey(Envelope<? extends Message> envelope);

    default String getName() {
        if (this == MESSAGE_TYPE) {
            return "MESSAGE";
        } else if (this == ADDRESS) {
            return "ADDRESS";
        } else {
            return "CUSTOM";
        }
    }
}