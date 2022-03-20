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

import com.ibasco.agql.core.transport.enums.TransportType;
import com.ibasco.agql.core.util.Options;

public interface NettyChannelFactoryProvider {

    NettyChannelFactory getFactory(final TransportType type, final Options options);

    /**
     * Get a new decorated {@link NettyChannelFactory}
     *
     * @param type
     *         The {@link TransportType} of the {@link NettyChannelFactory}
     * @param options
     *         The configuration {@link Options} to be used by the factory
     *
     * @return A decorated {@link NettyContextChannelFactory}
     */
    NettyContextChannelFactory getContextualFactory(final TransportType type, final Options options);

    NettyContextChannelFactory getContextualFactory(final TransportType type, final Options options, NettyChannelContextFactory contextFactory);
}
