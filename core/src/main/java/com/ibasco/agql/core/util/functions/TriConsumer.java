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

package com.ibasco.agql.core.util.functions;

import java.util.Objects;

/**
 * <p>TriConsumer interface.</p>
 *
 * @author Rafael Luis Ibasco
 */
@FunctionalInterface
public interface TriConsumer<T, U, V> {

    /**
     * <p>andThen.</p>
     *
     * @param after
     *         a {@link com.ibasco.agql.core.util.functions.TriConsumer} object
     *
     * @return a {@link com.ibasco.agql.core.util.functions.TriConsumer} object
     */
    default TriConsumer<T, U, V> andThen(TriConsumer<? super T, ? super U, ? super V> after) {
        Objects.requireNonNull(after);
        return (a, b, c) -> {
            accept(a, b, c);
            after.accept(a, b, c);
        };
    }

    /**
     * <p>accept.</p>
     *
     * @param t
     *         a T object
     * @param u
     *         a U object
     * @param v
     *         a V object
     */
    void accept(T t, U u, V v);
}
