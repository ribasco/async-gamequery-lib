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
 * Base class for all {@link Option} containers
 *
 * @author Rafael Luis Ibasco
 */
abstract public class AbstractOptions implements Options {

    private final Map<Option<?>, OptionValue> options = new ConcurrentHashMap<>();

    //Map Entry<Option<?>, OptionValue> to Entry<Option<?>, Object>
    private static final Function<Map.Entry<Option<?>, OptionValue>, Map.Entry<Option<?>, Object>> OPTION_ENTRIES = new Function<Map.Entry<Option<?>, OptionValue>, Map.Entry<Option<?>, Object>>() {
        @Override
        public Map.Entry<Option<?>, Object> apply(Map.Entry<Option<?>, OptionValue> oldEntry) {
            return new Map.Entry<Option<?>, Object>() {
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
            };
        }
    };

    protected AbstractOptions() {

    }

    /** {@inheritDoc} */
    @Override
    public <X> void add(Option<X> option, X value) {
        add(option, value, false);
    }

    /** {@inheritDoc} */
    @Override
    public <X> void add(Option<X> option, X value, boolean locked) {
        checkOption(option);
        options.put(option, new OptionValue(value, locked));
    }

    /** {@inheritDoc} */
    @Override
    public boolean isLocked(Option<?> option) {
        OptionValue optval = options.get(option);
        if (optval == null)
            return false;
        return optval.locked;
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(Option<?> option) {
        return options.containsKey(option);
    }

    /** {@inheritDoc} */
    @Override
    public <X> void remove(Option<X> option) {
        options.remove(option);
    }

    /** {@inheritDoc} */
    @Override
    public <X> X get(Option<X> option) {
        OptionValue optVal = options.get(option);
        if (optVal == null) {

            return null;
        }
        //noinspection unchecked
        return (X) optVal.value;
    }

    /** {@inheritDoc} */
    @Override
    public <X> X get(Option<X> option, X defaultValue) {
        OptionValue optVal = options.get(option);
        if (optVal == null || optVal.value == null)
            return defaultValue == null ? option.getDefaultValue() : defaultValue;
        //noinspection unchecked
        return (X) optVal.value;
    }

    /** {@inheritDoc} */
    @Override
    public <X> X getOrDefault(Option<X> option) {
        OptionValue optVal = options.get(option);
        if (optVal == null || optVal.value == null)
            return option.getDefaultValue();
        //noinspection unchecked
        return (X) optVal.value;
    }

    /** {@inheritDoc} */
    @Override
    public int size() {
        return options.size();
    }

    private void checkOption(Option<?> option) {
        if (option == null)
            throw new IllegalArgumentException("Option must not be null");
        if (isLocked(option))
            throw new IllegalStateException(String.format("Option '%s' is locked. Cannot modify. (Locked by '%s')", option.getKey(), getClass().getSimpleName()));

        if (!option.getOwner().equals(getClass()) && !option.getOwner().equals(GlobalOptions.class)) {
            Console.println("Option %s, Declaring Class: %s, Enclosing Class: %s", option.getFieldName(), option.getOwner(), option.getClass().getEnclosingClass());
            throw new IllegalStateException(String.format("Option '%s' (%s) is not allowed on this container (only options declared by this container are allowed, unless it is a global type)", option.getKey(), option.getFieldName()));
        }
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public Iterator<Map.Entry<Option<?>, Object>> iterator() {
        return options.entrySet().stream().map(OPTION_ENTRIES).iterator();
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
