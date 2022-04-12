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

package com.ibasco.agql.core.exceptions;

import io.netty.channel.Channel;

/**
 * <p>NoChannelContextException class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class NoChannelContextException extends AsyncGameLibUncheckedException {

    private final Channel channel;

    /**
     * <p>Constructor for NoChannelContextException.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param channel a {@link io.netty.channel.Channel} object
     */
    public NoChannelContextException(String message, Channel channel) {
        super(message);
        this.channel = channel;
    }

    /**
     * <p>Getter for the field <code>channel</code>.</p>
     *
     * @return a {@link io.netty.channel.Channel} object
     */
    public final Channel getChannel() {
        return channel;
    }
}
