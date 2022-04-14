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

import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import com.google.common.reflect.ClassPath;
import static com.ibasco.agql.core.util.Console.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    private static final Predicate<ClassPath.ClassInfo> OPTION_CONTAINERS = info -> info.isTopLevel() && info.getName().toLowerCase().contains("options") && Options.class.isAssignableFrom(info.load());

    private static final SetMultimap<Class<? extends Options>, Option<?>> optionCache = Multimaps.synchronizedSetMultimap(MultimapBuilder.hashKeys().hashSetValues().build());

    //<editor-fold desc="Private Members">
    private final String key;

    private final T defaultValue;

    private final boolean channelAttribute;

    private final boolean autoCreate;

    private final Class<? extends Options> optionClass;

    //</editor-fold>

    /**
     * Creates a new {@link Option} instance with the key and default value
     *
     * @param key
     *         The unique key associated with this option
     * @param defaultValue
     *         The default option value
     */
    private Option(Class<? extends Options> optionClass, String key, T defaultValue, boolean channelAttribute, boolean autoCreate) {
        this.optionClass = optionClass;
        this.key = key;
        this.defaultValue = defaultValue;
        this.channelAttribute = channelAttribute;
        this.autoCreate = autoCreate;
    }

    /**
     * Load and register all available configuration {@link Option}s and store into cache.
     */
    static void initialize() {
        Console.println("Initializing Options");

        //Initialize all available option classes from the loaded modules
        try {
            ClassPath classPath = ClassPath.from(Platform.class.getClassLoader());
            List<ClassPath.ClassInfo> classes = classPath.getAllClasses()
                                                         .stream()
                                                         .filter(OPTION_CONTAINERS)
                                                         .sorted(Comparator.comparing(ClassPath.ClassInfo::getSimpleName))
                                                         .collect(Collectors.toList());
            printLine();
            println("List of global/module options (Option Key/Default Value)");
            for (ClassPath.ClassInfo info : classes) {
                //noinspection unchecked
                Class<? extends Options> cls = (Class<? extends Options>) info.load();
                Field[] fields = cls.getDeclaredFields();
                printLine();
                println(color(ANSI_YELLOW, "%s"), cls.getSimpleName());
                printLine();
                int ctr = 1;
                for (Field field : fields) {
                    try {
                        int mod = field.getModifiers();
                        if (!Option.class.isAssignableFrom(field.getType()) || !Modifier.isPublic(mod))
                            continue;
                        Option<?> option = (Option<?>) field.get(null);
                        //note: if option is null, then it means it has not yet been created.
                        assert option != null;
                        assert option.getOptionClass().equals(cls);
                        println("%02d. " + color(ANSI_CYAN, "%-50s") + ": " + color(ANSI_WHITE, "%-30s (key: %s)"), ctr++, field.getName(), option.getDefaultValue(), option.getKey());
                    } catch (IllegalAccessException e) {
                        error(e.getMessage());
                    }
                }
            }
            printLine();
            println("Done. Loaded a total of '%d' options from %d modules", optionCache.values().size(), optionCache.keySet().size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Class<? extends Options> getOptionClass() {
        return optionClass;
    }

    /**
     * <p>Getter for the field <code>defaultValue</code>.</p>
     *
     * @return The default value assciated with this {@link com.ibasco.agql.core.util.Option}
     */
    public T getDefaultValue() {
        return defaultValue;
    }

    /**
     * <p>Getter for the field <code>key</code>.</p>
     *
     * @return The unique key associated with this {@link com.ibasco.agql.core.util.Option}
     */
    public String getKey() {
        return key;
    }

    /**
     * Creates a singleton instance of an {@link com.ibasco.agql.core.util.Option} based on the key provided
     *
     * @param key
     *         The unique key of the option
     * @param <V>
     *         The underlying type of the {@link com.ibasco.agql.core.util.Option}
     *
     * @return A singleton {@link com.ibasco.agql.core.util.Option} instance
     */
    public static <V> Option<V> createOption(String key) {
        return createOption(key, null);
    }

    /**
     * Creates a singleton instance of an {@link com.ibasco.agql.core.util.Option} based on the key provided
     *
     * @param key
     *         The unique key of the option
     * @param defaultValue
     *         The default value for this {@link com.ibasco.agql.core.util.Option}
     * @param <V>
     *         The underlying type of the {@link com.ibasco.agql.core.util.Option}
     *
     * @return A singleton {@link com.ibasco.agql.core.util.Option} instance
     */
    public static <V> Option<V> createOption(String key, V defaultValue) {
        return createOption(key, defaultValue, false);
    }

    /**
     * Creates a singleton instance of an {@link com.ibasco.agql.core.util.Option} based on the key provided
     *
     * @param key
     *         The unique key of the option
     * @param defaultValue
     *         The default value for this {@link com.ibasco.agql.core.util.Option}
     * @param <V>
     *         The underlying type of the {@link com.ibasco.agql.core.util.Option}
     * @param channelAttribute
     *         {@code true} if this option should be added as a default channel attribute
     *
     * @return A singleton {@link com.ibasco.agql.core.util.Option} instance based on the key provided.
     */
    public static <V> Option<V> createOption(String key, V defaultValue, boolean channelAttribute) {
        return createOption(key, defaultValue, channelAttribute, false);
    }

    /**
     * Creates a singleton instance of an {@link com.ibasco.agql.core.util.Option} based on the key provided
     *
     * @param key
     *         The unique key of the option
     * @param defaultValue
     *         The default value for this {@link com.ibasco.agql.core.util.Option}
     * @param <V>
     *         The underlying type of the {@link com.ibasco.agql.core.util.Option}
     * @param channelAttribute
     *         {@code true} if this option should be added as a default channel attribute
     * @param autoCreate
     *         If {@code true}, the channel attribute will be automatically initialized in the underlying {@link io.netty.channel.Channel} regardless whether or not it is explicitly set by the client. This is only applicable when channelAttribute is set to {@code true}.
     *
     * @return A singleton {@link com.ibasco.agql.core.util.Option} instance based on the key provided.
     */
    public static <V> Option<V> createOption(String key, V defaultValue, boolean channelAttribute, boolean autoCreate) {
        //perform some magic and determine the declaring class automatically
        Class<? extends Options> group = getDeclaringClass();
        if (group == null)
            throw new IllegalStateException("Failed to find declaring class for key " + key);
        Option<?> option = getOption(group, key);
        if (option != null)
            throw new IllegalStateException(String.format("Key '%s' already exists", key));
        if (!channelAttribute && autoCreate)
            throw new IllegalStateException("Auto create is set to true but option is not marked as a channel attribute");
        //create new option
        synchronized (optionCache) {
            option = new Option<>(group, key, defaultValue, channelAttribute, autoCreate);
            if (optionCache.put(group, option)) {
                //noinspection unchecked
                return (Option<V>) option;
            }
        }
        throw new IllegalStateException(String.format("Failed to add option '%s' to cache", option));
    }

    private static Class<? extends Options> getDeclaringClass() {
        Class<? extends Options> containerClass = null;
        try {
            for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
                try {
                    if (element.isNativeMethod() || !element.getClassName().endsWith("Options"))
                        continue;
                    Class<?> cls = Class.forName(element.getClassName());
                    if (Options.class.isAssignableFrom(cls)) {
                        //noinspection unchecked
                        containerClass = (Class<? extends Options>) Class.forName(element.getClassName());
                        break;
                    }
                } catch (ClassNotFoundException ignored) {
                }
            }
        } catch (Throwable e) {
            error("Failed to retrieve calling class: '%s'", e.getMessage());
        }
        return containerClass;
    }

    /**
     * <p>Retrieve an existing {@link Option} using the provided class group and key combination</p>
     *
     * @param group
     *         The {@link Class}
     * @param key
     *         The key identifying the option
     * @param <V>
     *         The underlying type of the Option
     *
     * @return The {@link Option} instance or {@code null} if no {@link Option} found for the specified combination.
     */
    public static <V> Option<V> getOption(Class<? extends Options> group, String key) {
        if (group == null)
            throw new IllegalArgumentException("Group cannot be null");
        if (Strings.isBlank(key))
            throw new IllegalArgumentException("Key cannot be null");
        if (!optionCache.containsKey(group))
            return null;
        Set<Option<?>> groupOptions = optionCache.get(group);
        synchronized (optionCache) {
            for (Option<?> option : groupOptions) {
                if (option.getKey().equalsIgnoreCase(key))
                    //noinspection unchecked
                    return (Option<V>) option;
            }
        }
        return null;
    }

    /**
     * Returns a read-only {@link SetMultimap} containing of all the avialable options of this library
     *
     * @return A read-only {@link SetMultimap}
     */
    public static SetMultimap<Class<? extends Options>, Option<?>> getOptions() {
        return ImmutableSetMultimap.<Class<? extends Options>, Option<?>>builder().putAll(optionCache).build();
    }

    /**
     * <p>isChannelAttribute.</p>
     *
     * @return {@code true} if this option is also a channel attribute
     */
    public boolean isChannelAttribute() {
        return channelAttribute;
    }

    /**
     * <p>isAutoCreate.</p>
     *
     * @return {@code true} if autoCreate flag is set.
     */
    public boolean isAutoCreate() {
        return autoCreate;
    }

    /**
     * <p>toAttributeKey.</p>
     *
     * @return An {@link io.netty.util.AttributeKey} instance of this option
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
     *         The {@link io.netty.channel.ChannelHandlerContext} whose {@link io.netty.channel.Channel} will be updated
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
     *         The {@link io.netty.channel.Channel} that will be updated
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
     *         The {@link io.netty.channel.ChannelHandlerContext} whos {@link io.netty.channel.Channel} will be queried using the attribute associated with this option.
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
     *         The {@link io.netty.channel.Channel} that will be queried using the attribute associated with this option.
     *
     * @return The value of the attribute
     */
    public T attr(Channel channel) {
        if (!channelAttribute)
            throw new IllegalStateException("Option is not marked as a channel attribute");
        //noinspection unchecked
        return (T) channel.attr(AttributeKey.valueOf(key)).get();
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(getOptionClass(), getKey());
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Option)) return false;
        Option<?> option = (Option<?>) o;
        return new EqualsBuilder()
                .append(getKey(), option.getKey())
                .append(getOptionClass(), option.getOptionClass())
                .isEquals();
    }

}
