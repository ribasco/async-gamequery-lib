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
 * A helper class for building a map of configuration {@link com.ibasco.agql.core.util.Option} instances.
 *
 * @author Rafael Luis Ibasco
 */
public class OptionBuilder<T extends Options> {

    private final T map;

    /**
     * <p>Constructor for OptionBuilder.</p>
     *
     * @param optionClass
     *         a {@link java.lang.Class} object
     */
    private OptionBuilder(Class<T> optionClass) {
        if (optionClass == null)
            throw new IllegalArgumentException("Option class must not be null");
        try {
            map = optionClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>Creates a new option container based on the provided {@link com.ibasco.agql.core.util.Options} class.</p>
     *
     * @param optionsClass
     *         any {@link java.lang.Class} implementing {@link com.ibasco.agql.core.util.Options}
     * @param <C>
     *         Captured type of {@link Options}
     * @param <O>
     *         Captured type of {@link OptionBuilder}
     *
     * @return An {@link com.ibasco.agql.core.util.OptionBuilder} instance for the specified {@link com.ibasco.agql.core.util.Options} class
     */
    public static <C extends Options, O extends OptionBuilder<C>> O newBuilder(Class<C> optionsClass) {
        //noinspection unchecked
        return (O) new OptionBuilder<C>(optionsClass);
    }

    /**
     * <p>Add a new configuration {@link Option}.</p>
     *
     * @param option
     *         a {@link com.ibasco.agql.core.util.Option} object
     * @param value
     *         The value of the {@link Option}
     * @param <X>
     *         The captured type of the {@link Option}
     *
     * @return a {@link com.ibasco.agql.core.util.OptionBuilder} object
     */
    public <X> OptionBuilder<T> option(Option<X> option, X value) {
        map.put(option, value);
        return this;
    }

    /**
     * <p>Builds the {@link Options} container</p>
     *
     * @return The {@link com.ibasco.agql.core.util.Options} container instance
     */
    public T build() {
        return map;
    }
}
