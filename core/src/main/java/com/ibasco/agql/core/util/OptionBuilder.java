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

package com.ibasco.agql.core.util;

/**
 * A helper class for building a map of configuration {@link com.ibasco.agql.core.util.Option}
 *
 * @author Rafael Luis Ibasco
 */
public class OptionBuilder {

    private final Options map;

    private OptionBuilder(Class<?> group) {
        map = new Options(group);
    }

    /**
     * <p>newBuilder.</p>
     *
     * @return a {@link com.ibasco.agql.core.util.OptionBuilder} object
     */
    public static OptionBuilder newBuilder() {
        return newBuilder(null);
    }

    /**
     * <p>newBuilder.</p>
     *
     * @param group a {@link java.lang.Class} object
     * @return a {@link com.ibasco.agql.core.util.OptionBuilder} object
     */
    public static OptionBuilder newBuilder(Class<?> group) {
        return new OptionBuilder(group);
    }

    /**
     * <p>option.</p>
     *
     * @param option a {@link com.ibasco.agql.core.util.Option} object
     * @param value a X object
     * @param <X> a X class
     * @return a {@link com.ibasco.agql.core.util.OptionBuilder} object
     */
    public <X> OptionBuilder option(Option<X> option, X value) {
        map.add(option, value);
        return this;
    }

    /**
     * <p>build.</p>
     *
     * @return a {@link com.ibasco.agql.core.util.Options} object
     */
    public Options build() {
        return map;
    }
}
