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

import com.ibasco.agql.core.NettyChannelContext;
import io.netty.util.AttributeKey;
import org.jetbrains.annotations.ApiStatus;

/**
 * Channel attributes used internally by this library
 *
 * @author Rafael Luis Ibasco
 */
@ApiStatus.Internal
public final class NettyChannelAttributes {

    /**
     * The attribute for the {@link NettyChannelContext}
     */
    public static final AttributeKey<NettyChannelContext> CHANNEL_CONTEXT = AttributeKey.valueOf("channelContext");
}
