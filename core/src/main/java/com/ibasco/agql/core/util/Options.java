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
 * A special map for storing configuration {@link com.ibasco.agql.core.util.Option} instances.
 *
 * @author Rafael Luis Ibasco
 */
public final class Options implements Cloneable, Iterable<Map.Entry<Option<?>, Object>> {

    private final Class<?> group;

    private final Map<Option<?>, OptionValue> options = new ConcurrentHashMap<>();

    /**
     * <p>Constructor for Options.</p>
     */
    public Options() {
        this(null);
    }

    /**
     * <p>Constructor for Options.</p>
     *
     * @param group
     *         a {@link java.lang.Class} object
     */
    public Options(Class<?> group) {
        this.group = group;
    }

    /**
     * <p>Getter for the field <code>group</code>.</p>
     *
     * @return a {@link java.lang.Class} object
     */
    public Class<?> getGroup() {
        return group;
    }

    /**
     * <p>isLocked.</p>
     *
     * @param option
     *         a {@link com.ibasco.agql.core.util.Option} object
     * @return a boolean
     */
    public synchronized boolean isLocked(Option<?> option) {
        OptionValue optval = options.get(option);
        if (optval == null)
            return false;
        return optval.locked;
    }

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
    public synchronized <X> void add(Option<X> option, X value) {
        add(option, value, false);
    }

    /**
     * <p>contains.</p>
     *
     * @param option
     *         a {@link com.ibasco.agql.core.util.Option} object
     * @return a boolean
     */
    public boolean contains(Option<?> option) {
        return options.containsKey(option);
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
    public synchronized <X> void add(Option<X> option, X value, boolean locked) {
        if (isLocked(option))
            throw new IllegalStateException(String.format("Option '%s' is locked. Cannot modify. (Locked by '%s')", option.getKey(), getGroup().getSimpleName()));
        options.put(option, new OptionValue(value, locked));
    }

    /**
     * <p>remove.</p>
     *
     * @param option
     *         a {@link com.ibasco.agql.core.util.Option} object
     * @param <X>
     *         a X class
     */
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
     * @return The value of the option
     */
    public synchronized <X> X get(Option<X> option) {
        OptionValue optVal = options.get(option);
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
     * @return a X object
     */
    public synchronized <X> X get(Option<X> option, X defaultValue) {
        OptionValue optVal = options.get(option);
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
     * @return a X object
     */
    public synchronized <X> X getOrDefault(Option<X> option) {
        OptionValue optVal = options.get(option);
        if (optVal == null || optVal.value == null) {
            return option.getDefaultValue();
        }
        //noinspection unchecked
        return (X) optVal.value;
    }

    /**
     * <p>clone.</p>
     *
     * @return a {@link com.ibasco.agql.core.util.Options} object
     * @throws java.lang.CloneNotSupportedException
     *         if any.
     */
    public Options clone() throws CloneNotSupportedException {
        Options clone = (Options) super.clone();
        clone.options.putAll(options);
        return clone;
    }

    /**
     * <p>size.</p>
     *
     * @return a int
     */
    public synchronized int size() {
        return options.size();
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public Iterator<Map.Entry<Option<?>, Object>> iterator() {
        return options.entrySet().stream().map((Function<Map.Entry<Option<?>, OptionValue>, Map.Entry<Option<?>, Object>>) oldEntry -> new Map.Entry<Option<?>, Object>() {
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
                OptionValue oldOptVal = oldEntry.getValue();
                oldEntry.setValue(new OptionValue(value, oldOptVal.locked));
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
