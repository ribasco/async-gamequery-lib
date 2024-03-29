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

package com.ibasco.agql.core;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.DefaultByteBufHolder;

/**
 * Base class for {@link com.ibasco.agql.core.Packet}. Extends from Netty's {@link io.netty.buffer.DefaultByteBufHolder}
 *
 * @author Rafael Luis Ibasco
 */
abstract public class AbstractPacket extends DefaultByteBufHolder implements Packet {

    /**
     * <p>Constructor for AbstractPacket.</p>
     *
     * @param data
     *         a {@link io.netty.buffer.ByteBuf} object
     */
    public AbstractPacket(ByteBuf data) {
        super(data);
    }
}
