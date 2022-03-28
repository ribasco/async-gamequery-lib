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
 * A special map for storing configuration {@link Option} instances.
 *
 * @author Rafael Luis Ibasco
 */
public final class Options implements Cloneable, Iterable<Map.Entry<Option<?>, Object>> {

    private final Class<?> group;

    private final Map<Option<?>, OptionValue> options = new ConcurrentHashMap<>();

    public Options() {
        this(null);
    }

    public Options(Class<?> group) {
        this.group = group;
    }

    public Class<?> getGroup() {
        return group;
    }

    public synchronized boolean isLocked(Option<?> option) {
        OptionValue optval = options.get(option);
        if (optval == null)
            return false;
        return optval.locked;
    }

    public synchronized <X> void add(Option<X> option, X value) {
        add(option, value, false);
    }

    public boolean contains(Option<?> option) {
        return options.containsKey(option);
    }

    public synchronized <X> void add(Option<X> option, X value, boolean locked) {
        if (isLocked(option))
            throw new IllegalStateException(String.format("Option '%s' is locked. Cannot modify. (Locked by '%s')", option.getKey(), getGroup().getSimpleName()));
        options.put(option, new OptionValue(value, locked));
    }

    public synchronized <X> void remove(Option<X> option) {
        options.remove(option);
    }

    public synchronized <X> X get(Option<X> option) {
        OptionValue optVal = options.get(option);
        if (optVal == null)
            return null;
        //noinspection unchecked
        return (X) optVal.value;
    }

    public synchronized <X> X get(Option<X> option, X defaultValue) {
        OptionValue optVal = options.get(option);
        if (optVal == null || optVal.value == null)
            return defaultValue == null ? option.getDefaultValue() : defaultValue;
        //noinspection unchecked
        return (X) optVal.value;
    }

    public synchronized <X> X getOrDefault(Option<X> option) {
        OptionValue optVal = options.get(option);
        if (optVal == null || optVal.value == null) {
            return option.getDefaultValue();
        }
        //noinspection unchecked
        return (X) optVal.value;
    }

    public Options clone() throws CloneNotSupportedException {
        Options clone = (Options) super.clone();
        clone.options.putAll(options);
        return clone;
    }

    public synchronized int size() {
        return options.size();
    }

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
