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

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A key-value pair representing a configuration entry to be used by the underlying transport.
 *
 * @param <T>
 *         The underlying type of the option value
 *
 * @author Rafael Luis Ibasco
 */
public final class Option<T> {

    private static final Logger log = LoggerFactory.getLogger(Option.class);

    //<editor-fold desc="Private Members">
    private final String key;

    private final T defaultValue;

    private final boolean channelAttribute;

    private final boolean autoCreate;

    private boolean locked;

    private static final Map<String, Option<?>> optionMap = new ConcurrentHashMap<>();
    //</editor-fold>

    /**
     * Returns a read-only {@link Map} of all the avialable options in this library
     *
     * @return A read-only {@link Map} containing all the options created
     */
    public static Collection<Option<?>> getOptions() {
        return Collections.unmodifiableCollection(optionMap.values());
    }

    /**
     * Creates a new {@link Option} instance with the key and default value
     *
     * @param key
     *         The unique key associated with this option
     * @param defaultValue
     *         The default option value
     */
    private Option(String key, T defaultValue, boolean channelAttribute, boolean autoCreate) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.channelAttribute = channelAttribute;
        this.autoCreate = autoCreate;
    }

    /**
     * @return The unique key associated with this {@link Option}
     */
    public String getKey() {
        return key;
    }

    /**
     * @return The default value assciated with this {@link Option}
     */
    public T getDefaultValue() {
        return defaultValue;
    }

    /**
     * @return {@code true} if this option is also a channel attribute
     */
    public boolean isChannelAttribute() {
        return channelAttribute;
    }

    /**
     * @return {@code true} if autoCreate flag is set.
     */
    public boolean isAutoCreate() {
        return autoCreate;
    }

    /**
     * @return An {@link AttributeKey} instance of this option
     */
    public AttributeKey<T> toAttributeKey() {
        if (!channelAttribute)
            throw new IllegalStateException("Option is not marked as a channel attribute");
        return AttributeKey.valueOf(key);
    }

    /**
     * A convenient method for modifying a channel's attribute
     *
     * @param context
     *         The {@link ChannelHandlerContext} whose {@link Channel} will be updated
     * @param value
     *         The value to be modified
     */
    public void attr(ChannelHandlerContext context, T value) {
        attr(context.channel(), value);
    }

    /**
     * A convenient method for modifying a channel's attribute
     *
     * @param channel
     *         The {@link Channel} that will be updated
     * @param value
     *         The value to be modified
     */
    public void attr(Channel channel, T value) {
        if (!channelAttribute)
            throw new IllegalStateException("Option is not marked as a channel attribute");
        channel.attr(AttributeKey.valueOf(key)).set(value);
    }

    /**
     * A convenient method for retrieving a channel's attribute associated with this option
     *
     * @param context
     *         The {@link ChannelHandlerContext} whos {@link Channel} will be queried using the attribute associated with this option.
     *
     * @return The value of the attribute
     */
    public T attr(ChannelHandlerContext context) {
        if (!channelAttribute)
            throw new IllegalStateException("Option is not marked as a channel attribute");
        return attr(context.channel());
    }

    /**
     * A convenient method for retrieving a channel's attribute associated with this option
     *
     * @param channel
     *         The {@link Channel} that will be queried using the attribute associated with this option.
     *
     * @return The value of the attribute
     */
    public T attr(Channel channel) {
        if (!channelAttribute)
            throw new IllegalStateException("Option is not marked as a channel attribute");
        //noinspection unchecked
        return (T) channel.attr(AttributeKey.valueOf(key)).get();
    }

    /**
     * Creates a singleton instance of an {@link Option} based on the key provided
     *
     * @param key
     *         The unique key of the option
     * @param <V>
     *         The underlying type of the {@link Option}
     *
     * @return A singleton {@link Option} instance
     */
    public static <V> Option<V> createOption(String key) {
        return createOption(key, null);
    }

    /**
     * Creates a singleton instance of an {@link Option} based on the key provided
     *
     * @param key
     *         The unique key of the option
     * @param defaultValue
     *         The default value for this {@link Option}
     * @param <V>
     *         The underlying type of the {@link Option}
     *
     * @return A singleton {@link Option} instance
     */
    public static <V> Option<V> createOption(String key, V defaultValue) {
        return createOption(key, defaultValue, false);
    }

    /**
     * Creates a singleton instance of an {@link Option} based on the key provided
     *
     * @param key
     *         The unique key of the option
     * @param defaultValue
     *         The default value for this {@link Option}
     * @param <V>
     *         The underlying type of the {@link Option}
     * @param channelAttribute
     *         {@code true} if this option should be added as a default channel attribute
     *
     * @return A singleton {@link Option} instance based on the key provided.
     */
    public static <V> Option<V> createOption(String key, V defaultValue, boolean channelAttribute) {
        return createOption(key, defaultValue, channelAttribute, false);
    }

    /**
     * Creates a singleton instance of an {@link Option} based on the key provided
     *
     * @param key
     *         The unique key of the option
     * @param defaultValue
     *         The default value for this {@link Option}
     * @param <V>
     *         The underlying type of the {@link Option}
     * @param channelAttribute
     *         {@code true} if this option should be added as a default channel attribute
     * @param autoCreate
     *         If {@code true}, the channel attribute will be automatically initialized in the underlying {@link Channel} regardless whether or not it is explicitly set by the client. This is only applicable when channelAttribute is set to {@code true}.
     *
     * @return A singleton {@link Option} instance based on the key provided.
     */
    public static <V> Option<V> createOption(String key, V defaultValue, boolean channelAttribute, boolean autoCreate) {
        if (optionMap.containsKey(key))
            throw new IllegalStateException(String.format("Key '%s' already exists", key));
        if (!channelAttribute && autoCreate)
            throw new IllegalStateException("Auto create is set to true but option is not marked as a channel attribute");

        //noinspection unchecked
        return (Option<V>) optionMap.computeIfAbsent(key, s -> new Option<V>(key, defaultValue, channelAttribute, autoCreate));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Option<?> option = (Option<?>) o;
        return getKey().equals(option.getKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey());
    }
}
