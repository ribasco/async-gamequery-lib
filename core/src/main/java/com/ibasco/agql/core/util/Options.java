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

import java.util.Map;

/**
 * A container for configuration {@link com.ibasco.agql.core.util.Option} instances. Implementation of this must be thread-safe.
 *
 * @author Rafael Luis Ibasco
 */
public interface Options extends Cloneable, Iterable<Map.Entry<Option<?>, Object>> {

    /**
     * <p>Add option value to this container</p>
     *
     * @param option
     *         The {@link com.ibasco.agql.core.util.Option} key
     * @param value
     *         The {@link Option} value
     * @param <X>
     *         The captured type of the {@link com.ibasco.agql.core.util.Option}
     */
    <X> void put(Option<X> option, X value);

    /**
     * <p>Add option value to this container if not yet existing</p>
     *
     * @param option
     *         The {@link com.ibasco.agql.core.util.Option} key
     * @param value
     *         The {@link Option} value
     * @param <X>
     *         The captured type of the {@link com.ibasco.agql.core.util.Option}
     */
    <X> X putIfAbsent(Option<X> option, X value);

    /**
     * <p>Add option value to this container with the additional option to lockout the value</p>
     *
     * @param option
     *         The {@link com.ibasco.agql.core.util.Option} key to be used as lookup
     * @param value
     *         The value to be added
     * @param locked
     *         Set to {@code true} if we should lockout this value. Future invocations of {@link #put(Option, Object)} will result in failure for this configuration {@link com.ibasco.agql.core.util.Option}
     * @param <X>
     *         The captured type of the {@link com.ibasco.agql.core.util.Option}
     */
    <X> void put(Option<X> option, X value, boolean locked);

    /**
     * <p>Check locked status of option</p>
     *
     * @param option
     *         a {@link com.ibasco.agql.core.util.Option} object
     *
     * @return {@code true} if the specified {@link com.ibasco.agql.core.util.Option} is currently locked.
     */
    boolean isLocked(Option<?> option);

    /**
     * <p>Checks if the provided {@link com.ibasco.agql.core.util.Option} key exists in this container</p>
     *
     * @param option
     *         The {@link com.ibasco.agql.core.util.Option} key to be used as lookup
     *
     * @return {@code true} if the {@link com.ibasco.agql.core.util.Option} exists in this container
     */
    boolean contains(Option<?> option);

    /**
     * <p>Remove {@link com.ibasco.agql.core.util.Option} value from this container.</p>
     *
     * @param option
     *         The {@link com.ibasco.agql.core.util.Option} key to be used as lookup
     * @param <X>
     *         The captured type of the {@link com.ibasco.agql.core.util.Option}
     */
    <X> void remove(Option<X> option);

    /**
     * <p>Retrieve {@link com.ibasco.agql.core.util.Option} value from this container</p>
     *
     * @param option
     *         The {@link com.ibasco.agql.core.util.Option} key to be used as lookup
     * @param <X>
     *         The captured type of the {@link com.ibasco.agql.core.util.Option}
     *
     * @return The value associated with the provided {@link com.ibasco.agql.core.util.Option}
     */
    <X> X get(Option<X> option);

    default <X> X getOrDefault(Option<X> option, Class<? extends Options> context) {
        //note: we do not use the provided option instance, instead we use it's associated key for lookup
        //as its possible to have multiple option instances having the same key inside a single container
        return getOrDefault(option.getKey(), context);
    }

    default <X> X getOrDefault(String key, Class<? extends Options> context) {
        Option<X> option = Option.of(getClass(), context, key);
        if (option == null)
            throw new IllegalStateException(String.format("Option key '%s' does not exists (context '%s')", key, context));
        X value = get(option, context);
        return value == null ? option.getDefaultValue() : value;
    }

    default <X> X get(Option<?> option, Class<? extends Options> context) {
        //note: we do not use the provided option instance, instead we use it's associated key for lookup
        //as its possible to have multiple option instances having the same key inside a single container
        return get(option.getKey(), context);
    }

    <X> X get(String key, Class<? extends Options> context);

    /**
     * <p>Retrieve option value from this container. If missing, the provided default value will be returned</p>
     *
     * @param option
     *         The {@link com.ibasco.agql.core.util.Option} key to be used as lookup
     * @param defaultValue
     *         The default value to return if the initial return value is {@code null}
     * @param <X>
     *         The captured type of the {@link com.ibasco.agql.core.util.Option}
     *
     * @return The value associated with the provided {@link com.ibasco.agql.core.util.Option}
     */
    <X> X get(Option<X> option, X defaultValue);

    /**
     * <p>Retrieve the option value. If missing, the default value defined by the Option will be returned instead</p>
     *
     * @param option
     *         a {@link com.ibasco.agql.core.util.Option} object
     * @param <X>
     *         The captured type of the {@link com.ibasco.agql.core.util.Option}
     *
     * @return The value associated with the provided {@link com.ibasco.agql.core.util.Option}
     */
    <X> X getOrDefault(Option<X> option);

    /**
     * Check if container is empty.
     *
     * @return {@code true} if the instance is empty.
     */
    default boolean isEmpty() {
        return size() == 0;
    }

    /**
     * <p>Returns the number of configuration {@link com.ibasco.agql.core.util.Option} present for this container</p>
     *
     * @return An integer representing the size of the {@link com.ibasco.agql.core.util.Option}s present in this container.
     */
    int size();
}
