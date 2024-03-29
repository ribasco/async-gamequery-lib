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

import com.ibasco.agql.core.util.HttpOptions;
import com.ibasco.agql.core.util.Options;
import java.util.concurrent.CompletableFuture;

/**
 * A generic Http Client base
 *
 * @author Rafael Luis Ibasco
 */
abstract class AsyncHttpClient extends AbstractClient<AbstractWebRequest, AbstractWebResponse> {

    /**
     * <p>Constructor for AsyncHttpClient.</p>
     *
     * @param options
     *         a {@link com.ibasco.agql.core.util.Options} object
     */
    protected AsyncHttpClient(HttpOptions options) {
        super(options);
    }

    /** {@inheritDoc} */
    @Override
    abstract protected HttpMessenger createMessenger(Options options);

    /**
     * <p>send.</p>
     *
     * @param message
     *         a {@link com.ibasco.agql.core.AbstractWebRequest} object
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    protected CompletableFuture<AbstractWebResponse> send(AbstractWebRequest message) {
        return getMessenger().send(null, message);
    }
}
