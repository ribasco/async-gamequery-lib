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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Represents a configuration option
 *
 * @param <T>
 *         The underlying type of the option value
 *
 * @author Rafael Luis Ibasco
 */
public final class Option<T> {

    private static final Logger log = LoggerFactory.getLogger(Option.class);

    private static final Predicate<ClassPath.ClassInfo> VALID_OPTION_CONTAINERS = info -> {
        if (!info.isTopLevel() || !info.getName().toLowerCase().contains("options"))
            return false;
        Class<?> cls = info.load();
        int modifiers = cls.getModifiers();
        boolean validInterface = !Modifier.isInterface(modifiers);// || cls.isAnnotationPresent(Shared.class);
        return Options.class.isAssignableFrom(cls) && Modifier.isPublic(modifiers) && !Modifier.isAbstract(modifiers) && validInterface;
    };

    private static final SetMultimap<Class<? extends Options>, Option<?>> optionCache = Multimaps.synchronizedSetMultimap(MultimapBuilder.hashKeys().hashSetValues().build());

    //<editor-fold desc="Private Members">
    private final String key;

    private final T defaultValue;

    private String fieldName;

    private final boolean channelAttribute;

    private final boolean autoCreate;

    private final Class<? extends Options> ownerClass;
    //</editor-fold>

    static {
        Platform.initialize();
    }

    /**
     * Creates a new {@link Option} instance with the key and default value
     *
     * @param key
     *         The unique key associated with this option
     * @param defaultValue
     *         The default option value
     */
    private Option(Class<? extends Options> ownerClass, String key, T defaultValue, boolean channelAttribute, boolean autoCreate) {
        this.ownerClass = ownerClass;
        this.key = key;
        this.defaultValue = defaultValue;
        this.channelAttribute = channelAttribute;
        this.autoCreate = autoCreate;
    }

    /**
     * Load all available configuration {@link Option} classes found in the classpath. The loading will then trigger the options to get created and get stored in a global cache registry.
     */
    static void initialize() {
        Console.println("Initializing Options");
        //Initialize all available option classes from the loaded modules
        try {
            ClassPath classPath = ClassPath.from(Platform.class.getClassLoader());
            //fina all valid option containers (classes implementing the Options interface)
            List<ClassPath.ClassInfo> classes = classPath.getAllClasses()
                                                         .stream()
                                                         .filter(VALID_OPTION_CONTAINERS)
                                                         .sorted(Comparator.comparing(ClassPath.ClassInfo::getSimpleName))
                                                         .collect(Collectors.toList());
            printLine();
            println("List of global/module options (Option Key/Default Value)");
            for (ClassPath.ClassInfo info : classes) {
                //note: by manually loading the classses, we trigger Option#create() as a result, which populates the optionCache.

                //noinspection unchecked
                Class<? extends Options> declaringClass = (Class<? extends Options>) info.load();
                Field[] fields = declaringClass.getFields(); //use getFields() to also include the fields of parent classses

                int ctr = 1;
                if (fields.length == 0) {
                    continue;
                }
                printLine();
                println(color(YELLOW, "%s (Default Values)"), declaringClass.getSimpleName());
                printLine();
                for (Field field : fields) {
                    try {
                        int mod = field.getModifiers();
                        if (!Option.class.isAssignableFrom(field.getType()) || !Modifier.isPublic(mod))
                            continue;
                        Option<?> option = (Option<?>) field.get(null);
                        //println("Processing option '%s' (Owner Class: %s, Parent Class: %s)", field.getName(), enclosingClass.getSimpleName(), field.getDeclaringClass().getSimpleName());
                        //note: if option is null, then it means it has not yet been created.
                        assert option != null;
                        assert option.getOwner().equals(declaringClass);
                        String defaultValue = StringUtils.abbreviate(Objects.toString(option.getDefaultValue(), "<NULL>"), 30);
                        println("%02d. " + color(CYAN, "%-50s") + ": " + color(WHITE, "%-30s (key: %s)"), ctr++, field.getName(), defaultValue, option.getKey());
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

    /**
     * @return {@code true} if this option is a Global option type
     *
     * @see GlobalOptions
     */
    public boolean isGlobal() {
        return ownerClass != null && ownerClass.equals(GlobalOptions.class);
    }

    /**
     * <p>Retrieve a singleton instance of an {@link Option} using the provided key.</p>
     *
     * <blockquote>
     * <strong>Note:</strong> This will lookup the {@link GlobalOptions} container by default
     * </blockquote>
     *
     * @param key
     *         The key to be used for the lookup
     *
     * @return A singleton instance of {@link Option} associated with the key
     */
    public static <V> Option<V> of(String key) {
        //noinspection unchecked
        return (Option<V>) optionCache.get(GlobalOptions.class).stream().filter(o -> o.getKey().equalsIgnoreCase(key)).findFirst().orElse(null);
    }

    /**
     * <p>Retrieve an singleton {@link Option} instance using the provided class group and key combination</p>
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
    public static <V> Option<V> of(Class<? extends Options> group, String key) {
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
     * The declaring {@link Class} of this {@link Option}
     *
     * @return The declaring {@link Class} of this {@link Option}
     */
    public Class<? extends Options> getOwner() {
        return ownerClass;
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
     * <p>Returns a singleton {@link Option} instance from global</p>
     *
     * @param key
     *         The option key to be used for lookup
     * @param <V>
     *         The captured type of the {@link Option}
     *
     * @return The singleton {@link Option} instance if found, otherwise {@code null}.
     */
    public static <V> Option<V> ofGlobal(String key) {
        for (Map.Entry<Option<?>, Object> entry : GlobalOptions.getContainer()) {
            if (entry.getKey().getKey().equalsIgnoreCase(key) && entry.getKey().isGlobal())
                //noinspection unchecked
                return (Option<V>) entry.getKey();
        }
        return null;
    }

    /**
     * Retrieve a global {@link Option} declared from {@link GlobalOptions} container
     *
     * @param option
     *         The {@link Option} to be used as lookup
     * @param <V>
     *         The captured return type
     *
     * @return The value of the specified {@link Option} retrieved from the {@link GlobalOptions} container
     */
    public static <V> V getGlobal(Option<V> option) {
        return GlobalOptions.getContainer().get(option);
    }

    /**
     * Retrieve a global {@link Option} declared from {@link GlobalOptions} container
     *
     * @param option
     *         The {@link Option} to be used as lookup
     * @param defaultValue
     *         The default value to return if the initial return value is {@code null}
     * @param <V>
     *         The captured return type
     *
     * @return The value of the specified {@link Option} retrieved from the {@link GlobalOptions} container
     */
    public static <V> V getGlobal(Option<V> option, V defaultValue) {
        return GlobalOptions.getContainer().get(option, defaultValue);
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
    public static <V> Option<V> create(String key) {
        return create(key, null);
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
    public static <V> Option<V> create(String key, V defaultValue) {
        return create(key, defaultValue, false);
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
    public static <V> Option<V> create(String key, V defaultValue, boolean channelAttribute) {
        return create(key, defaultValue, channelAttribute, false);
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
    public static <V> Option<V> create(String key, V defaultValue, boolean channelAttribute, boolean autoCreate) {
        //perform some magic and determine the declaring class automatically
        Class<? extends Options> declaringClass = getDeclaringClass();
        if (declaringClass == null)
            throw new IllegalStateException("Failed to find declaring class for key " + key);
        Option<?> option = Option.of(declaringClass, key);
        if (option != null)
            throw new IllegalStateException(String.format("Key '%s' already exists", key));
        if (!channelAttribute && autoCreate)
            throw new IllegalStateException("Auto create is set to true but option is not marked as a channel attribute");
        //create new option
        synchronized (optionCache) {
            option = new Option<>(declaringClass, key, defaultValue, channelAttribute, autoCreate);
            //Console.println(">> [%-20s] Initializing option %-35s with default value of '%s'", color(ANSI_WHITE, ownerClass.getSimpleName()), color(ANSI_CYAN, key), defaultValue);
            if (optionCache.put(declaringClass, option)) {
                //noinspection unchecked
                return (Option<V>) option;
            }
        }
        throw new IllegalStateException(String.format("Failed to add option '%s' to cache", option));
    }

    static void updateFieldNames() {
        for (Class<? extends Options> key : Option.getOptions().keySet()) {
            for (Option<?> option : Option.getOptions().get(key)) {
                option.fieldName = getFieldName(option.getKey(), option.getOwner());
            }
        }
    }

    private static String getFieldName(String key, Class<? extends Options> ownerClass) {
        if (Strings.isBlank(key))
            throw new IllegalStateException("Key must not be null/empty");
        if (ownerClass == null)
            throw new IllegalStateException("Owner class must not be null");
        Field[] fields = ownerClass.getDeclaredFields();
        for (Field field : fields) {
            try {
                int mod = field.getModifiers();
                if (!Option.class.isAssignableFrom(field.getType()) || !Modifier.isPublic(mod))
                    continue;
                Option<?> option = (Option<?>) field.get(null);
                if (option == null)
                    continue;
                if (option.getKey().equalsIgnoreCase(key))
                    return field.getName();
            } catch (IllegalAccessException e) {
                error(e.getMessage());
            }
        }
        return null;
    }

    private static Class<? extends Options> getDeclaringClass() {
        Class<? extends Options> containerClass = null;
        try {
            for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
                try {
                    if (element.isNativeMethod() || !element.getClassName().endsWith("Options")) {
                        continue;
                    }
                    Class<?> cls = Class.forName(element.getClassName());
                    if (Options.class.isAssignableFrom(cls) || cls.isAnnotationPresent(Shared.class)) {
                        //noinspection unchecked
                        containerClass = (Class<? extends Options>) Class.forName(element.getClassName());
                        break;
                    }
                } catch (ClassNotFoundException ignored) {
                }
            }
        } catch (Exception e) {
            error("Failed to retrieve calling class: '%s'", e.getMessage());
        }
        return containerClass;
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
     * Merge the default options not existing within the provided container.
     *
     * @param options
     *         The options container to be merged
     *
     * @return A {@link Map} containing the merged entries
     */
    public static Map<Option<?>, Object> merge(Options options) {
        return merge(options, (o, v) -> true);
    }

    public static Map<Option<?>, Object> merge(Options options, BiPredicate<Option<?>, Object> filter) {
        return merge(options, filter, true);
    }

    /**
     * Merge the rest of the default options that was not added on the container.
     *
     * @param options
     *         The options container to be merged
     * @param filter
     *         A {@link BiPredicate} to filter out unwanted entries
     *
     * @return A flat {@link Map} containing the merged entries
     */
    public static Map<Option<?>, Object> merge(Options options, BiPredicate<Option<?>, Object> filter, boolean mergeGlobal) {
        Class<? extends Options> owner = options.getClass();
        Map<Option<?>, Object> mergedOptions = new HashMap<>();
        //merge all the default options provided by this container.
        for (Option<?> option : Option.getOptions().get(owner)) {
            Object value = options.get(option);
            if (value == null)
                value = option.getDefaultValue();
            if (!filter.test(option, value))
                continue;
            mergedOptions.put(option, value);
        }
        //merge the rest of the options that is not owned by this container
        for (Map.Entry<Option<?>, Object> entry : options) {
            Option<?> option = entry.getKey();
            if (option.getOwner().equals(owner))
                continue;
            Object value = entry.getValue() == null ? option.getDefaultValue() : entry.getValue();
            if (!filter.test(option, value))
                continue;
            mergedOptions.put(option, value);
        }
        //should we merge global options too?
        if (!mergeGlobal)
            return mergedOptions;
        for (Map.Entry<Option<?>, Object> entry : GlobalOptions.getContainer()) {
            Option<?> globalOption = entry.getKey();
            Object globalOptionValue = entry.getValue();
            if (mergedOptions.containsKey(globalOption)) {
                //Console.println("%s contains a global option %s", options.getClass().getSimpleName(), globalOption.getFieldName());
            }
        }
        return mergedOptions;
    }

    public String getFieldName() {
        return fieldName;
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
        return Objects.hash(getOwner(), getKey());
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Option)) return false;
        Option<?> option = (Option<?>) o;
        return new EqualsBuilder()
                .append(getKey(), option.getKey())
                .append(getOwner(), option.getOwner())
                .isEquals();
    }

}
