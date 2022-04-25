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

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Base class for all {@link com.ibasco.agql.core.util.Option} containers
 *
 * @author Rafael Luis Ibasco
 */
abstract public class AbstractOptions implements Options {

    private static final Comparator<Map.Entry<Option<?>, Object>> BY_OPTION_FIELD = Comparator.comparing(key -> key.getKey().getFieldName() != null ? key.getKey().getFieldName() : key.getKey().getKey());

    private static final Comparator<Map.Entry<Option<?>, Object>> BY_OPTION_CLASS = Comparator.comparing(key -> key.getKey().getDeclaringClass().getSimpleName());

    private final Map<Option<?>, OptionValue<?>> options = new ConcurrentHashMap<>();

    private static final Function<Map.Entry<Option<?>, OptionValue<?>>, Map.Entry<Option<?>, Object>> OPTION_ENTRIES = oldEntry -> new Map.Entry<Option<?>, Object>() {
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
            oldEntry.setValue(new OptionValue<>(value, oldOptVal.locked));
            return oldOptVal.value;
        }
    };

    /**
     * <p>Constructor for AbstractOptions.</p>
     */
    protected AbstractOptions() {}

    /** {@inheritDoc} */
    @Override
    public <X> void put(Option<X> option, X value) {
        put(option, value, false);
    }

    /** {@inheritDoc} */
    @Override
    public <X> void put(Option<X> option, X value, boolean locked) {
        checkOption(option);
        options.put(option, new OptionValue<>(value, locked));
    }

    @Override
    public <X> X putIfAbsent(Option<X> option, X value) {
        checkOption(option);
        //noinspection unchecked
        OptionValue<X> optVal = (OptionValue<X>) options.putIfAbsent(option, new OptionValue<>(value, false));
        if (optVal != null)
            return optVal.value;
        return null;
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
        //order of retrieval
        //- retrieve value directly from this instance if option key is present
        //- if value is null, check the default value defined by this option key
        //- if default value is null,
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

    @Override
    public <X> X get(String key, Class<? extends Options> context) {
        return get(Option.of(getClass(), context, key));
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

        if (!option.getDeclaringClass().equals(getClass()) && !option.isGlobal() && !option.isShared()) {
            Console.println("Option %s, Declaring Class: %s, Enclosing Class: %s", option.getFieldName(), option.getDeclaringClass(), option.getClass().getEnclosingClass());
            throw new IllegalStateException(String.format("Option '%s' (%s) is not allowed on this container (only options declared by this container are allowed, unless it is a global type)", option.getKey(), option.getFieldName()));
        }
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public Iterator<Map.Entry<Option<?>, Object>> iterator() {
        return options.entrySet().stream().map(OPTION_ENTRIES).sorted(BY_OPTION_CLASS.thenComparing(BY_OPTION_FIELD)).iterator();
    }

    private static class OptionValue<V> {

        private final V value;

        private final boolean locked;

        private OptionValue(V value, boolean locked) {
            this.value = value;
            this.locked = locked;
        }
    }
}
