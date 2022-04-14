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

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Base class for all {@link Option} containers. This is thread-safe.
 *
 * @author Rafael Luis Ibasco
 */
abstract public class AbstractOptions implements Options {

    private final Map<Option<?>, AbstractOptions.OptionValue> options = new ConcurrentHashMap<>();

    /**
     * <p>add.</p>
     *
     * @param option
     *         a {@link com.ibasco.agql.core.util.Option} object
     * @param value
     *         a X object
     * @param <X>
     *         a X class
     */
    @Override
    public <X> void add(Option<X> option, X value) {
        add(option, value, false);
    }

    /**
     * <p>add.</p>
     *
     * @param option
     *         a {@link com.ibasco.agql.core.util.Option} object
     * @param value
     *         a X object
     * @param locked
     *         a boolean
     * @param <X>
     *         a X class
     */
    @Override
    public <X> void add(Option<X> option, X value, boolean locked) {
        if (isLocked(option))
            throw new IllegalStateException(String.format("Option '%s' is locked. Cannot modify. (Locked by '%s')", option.getKey(), getClass().getSimpleName()));
        options.put(option, new AbstractOptions.OptionValue(value, locked));
    }

    /**
     * <p>isLocked.</p>
     *
     * @param option
     *         a {@link com.ibasco.agql.core.util.Option} object
     *
     * @return a boolean
     */
    @Override
    public boolean isLocked(Option<?> option) {
        AbstractOptions.OptionValue optval = options.get(option);
        if (optval == null)
            return false;
        return optval.locked;
    }

    /**
     * <p>contains.</p>
     *
     * @param option
     *         a {@link com.ibasco.agql.core.util.Option} object
     *
     * @return a boolean
     */
    @Override
    public boolean contains(Option<?> option) {
        return options.containsKey(option);
    }

    /**
     * <p>remove.</p>
     *
     * @param option
     *         a {@link com.ibasco.agql.core.util.Option} object
     * @param <X>
     *         a X class
     */
    @Override
    public synchronized <X> void remove(Option<X> option) {
        options.remove(option);
    }

    /**
     * <p>Retrieve option value</p>
     *
     * @param option
     *         a {@link com.ibasco.agql.core.util.Option} object
     * @param <X>
     *         a The capturing type
     *
     * @return The value of the option
     */
    @Override
    public synchronized <X> X get(Option<X> option) {
        AbstractOptions.OptionValue optVal = options.get(option);
        if (optVal == null)
            return null;
        //noinspection unchecked
        return (X) optVal.value;
    }

    /**
     * <p>get.</p>
     *
     * @param option
     *         a {@link com.ibasco.agql.core.util.Option} object
     * @param defaultValue
     *         a X object
     * @param <X>
     *         a X class
     *
     * @return a X object
     */
    @Override
    public <X> X get(Option<X> option, X defaultValue) {
        AbstractOptions.OptionValue optVal = options.get(option);
        if (optVal == null || optVal.value == null)
            return defaultValue == null ? option.getDefaultValue() : defaultValue;
        //noinspection unchecked
        return (X) optVal.value;
    }

    /**
     * <p>getOrDefault.</p>
     *
     * @param option
     *         a {@link com.ibasco.agql.core.util.Option} object
     * @param <X>
     *         a X class
     *
     * @return a X object
     */
    @Override
    public <X> X getOrDefault(Option<X> option) {
        AbstractOptions.OptionValue optVal = options.get(option);
        if (optVal == null || optVal.value == null)
            return option.getDefaultValue();
        //noinspection unchecked
        return (X) optVal.value;
    }

    /**
     * <p>size.</p>
     *
     * @return a int
     */
    @Override
    public int size() {
        return options.size();
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public Iterator<Map.Entry<Option<?>, Object>> iterator() {
        return options.entrySet().stream().map((Function<Map.Entry<Option<?>, AbstractOptions.OptionValue>, Map.Entry<Option<?>, Object>>) oldEntry -> new Map.Entry<Option<?>, Object>() {
            @Override
            public Option<?> getKey() {
                return oldEntry.getKey();
            }

            @Override
            public Object getValue() {
                return oldEntry.getValue().value;
            }

            @Override
            public Object setValue(Object value) {
                AbstractOptions.OptionValue oldOptVal = oldEntry.getValue();
                oldEntry.setValue(new AbstractOptions.OptionValue(value, oldOptVal.locked));
                return oldOptVal.value;
            }
        }).iterator();
    }

    private static class OptionValue {

        private final Object value;

        private final boolean locked;

        private OptionValue(Object value, boolean locked) {
            this.value = value;
            this.locked = locked;
        }
    }
}
