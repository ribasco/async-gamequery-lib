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
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.ibasco.agql.core.util.Console.CYAN;
import static com.ibasco.agql.core.util.Console.WHITE;
import static com.ibasco.agql.core.util.Console.YELLOW;
import static com.ibasco.agql.core.util.Console.color;
import static com.ibasco.agql.core.util.Console.error;
import static com.ibasco.agql.core.util.Console.printLine;
import static com.ibasco.agql.core.util.Console.println;

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
        if (!info.isTopLevel() || !info.getName().startsWith("com.ibasco.agql") || !info.getName().toLowerCase().contains("options"))
            return false;
        Class<?> cls = info.load();
        int modifiers = cls.getModifiers();
        boolean validInterface = !Modifier.isInterface(modifiers);// || cls.isAnnotationPresent(Shared.class);
        return Options.class.isAssignableFrom(cls) && Modifier.isPublic(modifiers) && !Modifier.isAbstract(modifiers) && validInterface;
    };

    /**
     * Cache for all available {@link Option} instances in this project. Also describes the context the option is associated with.
     */
    private static final SetMultimap<Class<? extends Options>, CacheEntry> cache = Multimaps.synchronizedSetMultimap(MultimapBuilder.hashKeys().hashSetValues().build());

    //<editor-fold desc="Private Members">
    private final UUID id = UUID.create();

    private final String key;

    private final T defaultValue;

    private final boolean channelAttribute;

    private final boolean autoCreate;

    private final Class<? extends Options> declaringClass;

    String fieldName;
    //</editor-fold>

    /**
     * Creates a new {@link Option} instance with the key and default value
     */
    private Option(Class<? extends Options> declaringClass, String key, T defaultValue, boolean channelAttribute, boolean autoCreate) {
        this.declaringClass = declaringClass;
        this.key = key;
        this.defaultValue = defaultValue;
        this.channelAttribute = channelAttribute;
        this.autoCreate = autoCreate;
    }

    /**
     * Iterate through all available configuration {@link Option} classes found in the classpath then initialize and store the option keys into the cache.
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
            printLine();

            //1. Scan for 'Options' classes and initialize it's static field members
            for (ClassPath.ClassInfo info : classes) {
                //note: by manually loading the classses, we trigger Option#create() as a result, which populates the optionCache.
                Class<? extends Options> containerClass;
                try {
                    //noinspection unchecked
                    containerClass = (Class<? extends Options>) info.load();
                    Console.println("Processing %s", containerClass.getSimpleName());
                    Console.printLine();
                } catch (LinkageError e) {
                    error("Failed to load class '%s' (Error: %s)", info.getName(), e);
                    continue;
                }
                //1.a Recursively initialize fields
                initializeFields(containerClass);
            }

            //2. Display all registered options from the cache
            int total = 1;
            //Display registered options
            for (Class<? extends Options> cls : cache.keySet()) {
                printLine();
                println(color(YELLOW, "%s (Default Values)"), cls.getSimpleName());
                printLine();
                int ctr = 1;
                Set<CacheEntry> cacheEntries = cache.get(cls).stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new));
                for (CacheEntry cacheEntry : cacheEntries) {
                    String defaultValue = StringUtils.abbreviate(Objects.toString(cacheEntry.getOption().getDefaultValue(), "<NULL>"), 30);
                    println("%03d.%02d. " + color(CYAN, "%-50s") + ": " + color(WHITE, "%-30s (key: %s, context: %s, Id: %d)"), total++, ctr++, cacheEntry.getOption().getFieldName(), defaultValue, cacheEntry.getOption().getKey(), cacheEntry.getContext().getSimpleName(), cacheEntry.getOption().getId().getInteger());
                }
            }

            //3. Display summary
            printLine();
            println("Done. Loaded a total of '%d' options from %d modules", cache.values().size(), cache.keySet().size());
            printLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initializeFields(Class<? extends Options> containerClass) {
        initializeFields(containerClass, null);
    }

    private static void initializeFields(Class<? extends Options> contextClass, Class<? extends Options> parentClass) {
        if (parentClass == null)
            parentClass = contextClass;
        if (contextClass.isAnnotationPresent(Inherit.class)) {
            Class<? extends Options>[] optionClasses = contextClass.getDeclaredAnnotation(Inherit.class).options();
            for (Class<? extends Options> optionClass : optionClasses) {
                initializeFields(optionClass, parentClass);
            }
        }
        Field[] fields = contextClass.getFields(); //use getFields() to also include the fields declared from the parent
        if (fields.length == 0) {
            return;
        }
        for (Field field : fields) {
            try {
                int mod = field.getModifiers();
                if (!Option.class.isAssignableFrom(field.getType()) || !Modifier.isPublic(mod))
                    continue;
                Option<?> option = (Option<?>) field.get(null); //calling this method will force static field initialization (will trigger a call to Option#create)
                //note: if option is null, then it means it has not yet been created.
                assert option != null;
                //update option field name
                option.fieldName = field.getName();

                //add to cache
                CacheEntry cacheEntry = new CacheEntry(option, contextClass);
                boolean registered = cache.put(parentClass, cacheEntry);
                Console.println("%-30s :: %-30s -> %-30s = %-50s (Registered: %s, Declaring Class: %s, Context Class: %s, Id: %s)", parentClass.getSimpleName(), contextClass.getSimpleName(), option.getDeclaringClass().getSimpleName(), field.getName(), registered, option.getDeclaringClass().getSimpleName(), cacheEntry.getContext() != null ? cacheEntry.getContext().getSimpleName() : "N/A", option);
                //assert option.getOwner().equals(declaringClass);
            } catch (IllegalAccessException e) {
                error(e.getMessage());
            }
        }
        printLine();
    }

    /**
     * <p>Retrieve a singleton instance of an {@link com.ibasco.agql.core.util.Option} using the provided key.</p>
     *
     * <blockquote>
     * <strong>Note:</strong> This will lookup the {@link GeneralOptions} container by default
     * </blockquote>
     *
     * @param key
     *         The key to be used for the lookup
     * @param <V>
     *         a V class
     *
     * @return A singleton instance of {@link com.ibasco.agql.core.util.Option} associated with the key
     */
    public static <V> Option<V> of(String key) {
        //noinspection unchecked
        return (Option<V>) cache.get(GeneralOptions.class).stream().map(CacheEntry::getOption).filter(option -> option.getKey().equalsIgnoreCase(key)).findFirst().orElse(null);
    }

    /**
     * <p>Getter for the field <code>key</code>.</p>
     *
     * @return The unique key associated with this {@link com.ibasco.agql.core.util.Option}
     */
    public String getKey() {
        return key;
    }

    public static <V> Option<V> of(Class<? extends Options> containerClass, Class<?> context, Option<?> option) {
        return of(containerClass, context, option.getKey());
    }

    public static <V> Option<V> of(Class<? extends Options> containerClass, Class<?> context, String key) {
        if (containerClass == null)
            throw new IllegalArgumentException("Group cannot be null");
        if (Strings.isBlank(key))
            throw new IllegalArgumentException("Key cannot be null");
        if (!cache.containsKey(containerClass))
            return null;
        synchronized (cache) {
            Set<CacheEntry> cacheEntries = cache.get(containerClass);
            for (CacheEntry cacheEntry : cacheEntries) {
                Option<?> option = cacheEntry.getOption();
                Class<?> contextClass = cacheEntry.getContext();
                assert contextClass != null;
                if (option.getKey().equalsIgnoreCase(key) && contextClass.equals(context))
                    //noinspection unchecked
                    return (Option<V>) option;
            }
            return null;
        }
    }

    /**
     * <p>Returns a singleton {@link com.ibasco.agql.core.util.Option} instance from global</p>
     *
     * @param key
     *         The option key to be used for lookup
     * @param <V>
     *         The captured type of the {@link com.ibasco.agql.core.util.Option}
     *
     * @return The singleton {@link com.ibasco.agql.core.util.Option} instance if found, otherwise {@code null}.
     */
    public static <V> Option<V> ofGlobal(String key) {
        for (Map.Entry<Option<?>, Object> entry : GeneralOptions.getInstance()) {
            if (entry.getKey().getKey().equalsIgnoreCase(key) && entry.getKey().isGlobal())
                //noinspection unchecked
                return (Option<V>) entry.getKey();
        }
        return null;
    }

    /**
     * <p>isGlobal.</p>
     *
     * @return {@code true} if this option is a Global option type
     *
     * @see GeneralOptions
     */
    public boolean isGlobal() {
        return declaringClass != null && declaringClass.equals(GeneralOptions.class);
    }

    /**
     * Retrieve a global {@link com.ibasco.agql.core.util.Option} declared from {@link GeneralOptions} container
     *
     * @param option
     *         The {@link com.ibasco.agql.core.util.Option} to be used as lookup
     * @param <V>
     *         The captured return type
     *
     * @return The value of the specified {@link com.ibasco.agql.core.util.Option} retrieved from the {@link GeneralOptions} container
     */
    public static <V> V getGlobal(Option<V> option) {
        return GeneralOptions.getInstance().get(option);
    }

    /**
     * Retrieve a global {@link com.ibasco.agql.core.util.Option} declared from {@link GeneralOptions} container
     *
     * @param option
     *         The {@link com.ibasco.agql.core.util.Option} to be used as lookup
     * @param defaultValue
     *         The default value to return if the initial return value is {@code null}
     * @param <V>
     *         The captured return type
     *
     * @return The value of the specified {@link com.ibasco.agql.core.util.Option} retrieved from the {@link GeneralOptions} container
     */
    public static <V> V getGlobal(Option<V> option, V defaultValue) {
        return GeneralOptions.getInstance().get(option, defaultValue);
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
     * Creates a singleton instance of an {@link com.ibasco.agql.core.util.Option} based on the key provided. NOTE: The newly created instance will not be added to the cache.
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
        try {
            //perform some magic and determine the declaring class automatically
            Class<? extends Options> declaringClass = findDelcaringClass();
            if (declaringClass == null)
                throw new IllegalStateException("Failed to find declaring class for key " + key);
            Console.println("create(): %s) %s = %s", declaringClass.getSimpleName(), key, defaultValue);
            Option<?> option = Option.of(declaringClass, key);
            if (option != null)
                throw new IllegalStateException(String.format("Key '%s' already exists", key));
            if (!channelAttribute && autoCreate)
                throw new IllegalStateException("Auto create is set to true but option is not marked as a channel attribute");
            return new Option<>(declaringClass, key, defaultValue, channelAttribute, autoCreate);
        } catch (Throwable e) {
            e.printStackTrace(System.err);
        }
        return null;
    }

    private static Class<? extends Options> findDelcaringClass() {
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
     * <p>Retrieve an singleton {@link com.ibasco.agql.core.util.Option} instance using the provided class group and key combination</p>
     *
     * @param containerClass
     *         The {@link java.lang.Class} A class of type {@link Options}
     * @param key
     *         The key identifying the option
     * @param <V>
     *         The underlying type of the Option
     *
     * @return The {@link com.ibasco.agql.core.util.Option} instance or {@code null} if no {@link com.ibasco.agql.core.util.Option} found for the specified combination.
     */
    public static <V> Option<V> of(Class<? extends Options> containerClass, String key) {
        if (containerClass == null)
            throw new IllegalArgumentException("Group cannot be null");
        if (Strings.isBlank(key))
            throw new IllegalArgumentException("Key cannot be null");
        if (!cache.containsKey(containerClass))
            return null;
        synchronized (cache) {
            Set<CacheEntry> cacheEntries = cache.get(containerClass);
            for (CacheEntry cacheEntry : cacheEntries) {
                Option<?> option = cacheEntry.getOption();
                Class<?> contextClass = cacheEntry.getContext();
                assert contextClass != null;
                assert option != null;
                if (option.getKey().equalsIgnoreCase(key) && contextClass.equals(containerClass))
                    //noinspection unchecked
                    return (Option<V>) option;
            }
            return null;
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

    /**
     * Returns a read-only {@link com.google.common.collect.SetMultimap} containing of all the {@link Option}s provided by this library
     *
     * @return an {@link ImmutableSetMultimap}
     */
    public static SetMultimap<Class<? extends Options>, CacheEntry> getOptions() {
        return ImmutableSetMultimap.<Class<? extends Options>, CacheEntry>builder().putAll(cache).build();
    }

    public boolean isShared() {
        return getDeclaringClass().isAnnotationPresent(Shared.class);
    }

    /**
     * The declaring {@link java.lang.Class} of this {@link com.ibasco.agql.core.util.Option}
     *
     * @return The declaring {@link java.lang.Class} of this {@link com.ibasco.agql.core.util.Option}
     */
    public Class<? extends Options> getDeclaringClass() {
        return declaringClass;
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
     * <p>Getter for the field <code>fieldName</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
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

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getId()).toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Option)) return false;

        Option<?> option = (Option<?>) o;

        return new EqualsBuilder().append(getId(), option.getId()).isEquals();
    }

    public UUID getId() {
        return id;
    }

    public static final class CacheEntry implements Comparable<CacheEntry> {

        public static final Comparator<CacheEntry> BY_KEY = Comparator.comparing(e -> e.getOption().getKey());

        public static final Comparator<CacheEntry> BY_CONTEXT = Comparator.comparing(e -> e.getContext().getSimpleName());

        public static final Comparator<CacheEntry> BOTH = BY_CONTEXT.thenComparing(BY_KEY);

        private final Class<?> context;

        private final Option<?> option;

        private CacheEntry(Option<?> option, Class<?> context) {
            this.option = Objects.requireNonNull(option, "Option must not be null");
            this.context = Objects.requireNonNull(context, "Context class must not be null");
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37).append(getContext()).append(getOption()).toHashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (!(o instanceof Option.CacheEntry)) return false;

            CacheEntry that = (CacheEntry) o;

            return new EqualsBuilder().append(getContext(), that.getContext()).append(getOption(), that.getOption()).isEquals();
        }

        public Class<?> getContext() {
            return context;
        }

        public Option<?> getOption() {
            return option;
        }

        @Override
        public int compareTo(@NotNull Option.CacheEntry o) {
            return BOTH.compare(this, o);
        }
    }
}
