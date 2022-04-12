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

import com.ibasco.agql.core.NettyChannelContext;

import java.util.concurrent.CompletionException;

/**
 * <p>ResponseException class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class ResponseException extends CompletionException {

    private final NettyChannelContext context;

    /**
     * <p>Constructor for ResponseException.</p>
     *
     * @param cause a {@link java.lang.Throwable} object
     * @param context a {@link com.ibasco.agql.core.NettyChannelContext} object
     */
    public ResponseException(Throwable cause, NettyChannelContext context) {
        super(cause);
        this.context = context;
    }

    /**
     * <p>Getter for the field <code>context</code>.</p>
     *
     * @return a {@link com.ibasco.agql.core.NettyChannelContext} object
     */
    public NettyChannelContext getContext() {
        return context;
    }
}
