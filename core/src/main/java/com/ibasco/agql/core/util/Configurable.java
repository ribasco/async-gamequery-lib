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
 * <p>Configurable interface.</p>
 *
 * @author Rafael Luis Ibasco
 */
public interface Configurable {

    /**
     * <p>getOptions.</p>
     *
     * @return a {@link com.ibasco.agql.core.util.Options} object
     */
    Options getOptions();

    /**
     * <p>getOrDefault.</p>
     *
     * @param option a {@link com.ibasco.agql.core.util.Option} object
     * @param <V> a V class
     * @return a V object
     */
    default <V> V getOrDefault(Option<V> option) {
        return getOrDefault(option, null);
    }

    /**
     * <p>getOrDefault.</p>
     *
     * @param option a {@link com.ibasco.agql.core.util.Option} object
     * @param defaultValue a V object
     * @param <V> a V class
     * @return a V object
     */
    default <V> V getOrDefault(Option<V> option, V defaultValue) {
        return getOptions().get(option, defaultValue);
    }

    /**
     * <p>get.</p>
     *
     * @param option a {@link com.ibasco.agql.core.util.Option} object
     * @param <V> a V class
     * @return a V object
     */
    default <V> V get(Option<V> option) {
        return getOptions().get(option);
    }

    /**
     * <p>set.</p>
     *
     * @param option a {@link com.ibasco.agql.core.util.Option} object
     * @param value a V object
     * @param <V> a V class
     */
    default <V> void set(Option<V> option, V value) {
        getOptions().add(option, value);
    }

    /**
     * <p>lock.</p>
     *
     * @param option a {@link com.ibasco.agql.core.util.Option} object
     * @param value a V object
     * @param <V> a V class
     */
    default <V> void lock(Option<V> option, V value) {
        getOptions().add(option, value,true);
    }

    /**
     * <p>unlock.</p>
     *
     * @param option a {@link com.ibasco.agql.core.util.Option} object
     * @param <V> a V class
     */
    default <V> void unlock(Option<V> option) {
        if (!getOptions().isLocked(option))
            return;
        getOptions().get(option);
    }
}
