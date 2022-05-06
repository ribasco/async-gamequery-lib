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
import java.util.function.Function;

/**
 * <p>TriFunction interface.</p>
 *
 * @author Rafael Luis Ibasco
 */
@FunctionalInterface
public interface TriFunction<A, B, C, R> {

    /**
     * <p>andThen.</p>
     *
     * @param after
     *         a {@link java.util.function.Function} object
     * @param <V>
     *         a V class
     *
     * @return a {@link com.ibasco.agql.core.util.functions.TriFunction} object
     */
    default <V> TriFunction<A, B, C, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (A a, B b, C c) -> after.apply(apply(a, b, c));
    }

    /**
     * <p>apply.</p>
     *
     * @param a
     *         a A object
     * @param b
     *         a B object
     * @param c
     *         a C object
     *
     * @return a R object
     */
    R apply(A a, B b, C c);
}
