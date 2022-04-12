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

package com.ibasco.agql.core.transport.http;

import java.util.function.Function;

/**
 * <p>Abstract ContentTypeProcessor class.</p>
 *
 * @author Rafael Luis Ibasco
 */
abstract public class ContentTypeProcessor<T> implements Function<String, T> {
    /**
     * <p>processContent.</p>
     *
     * @param body a {@link java.lang.String} object
     * @return a T object
     */
    protected abstract T processContent(String body);

    /** {@inheritDoc} */
    @Override
    public final T apply(String s) {
        return processContent(s);
    }
}
