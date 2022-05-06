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

import org.apache.commons.lang3.BooleanUtils;
import org.jetbrains.annotations.ApiStatus;
import static com.ibasco.agql.core.util.Console.error;

/**
 * <p>A collection of global library properties. Note: This is not meant to be used outside.</p>
 *
 * @author Rafael Luis Ibasco
 */
@ApiStatus.Internal
public final class Properties {

    private static final String PROP_USE_NATIVE = "agql.nativeTransport";

    private static final String PROP_VERBOSE = "agql.verbose";

    private static final String PROP_CORE_POOL_SIZE = "agql.corePoolSize";

    private static final boolean USE_NATIVE_TRANSPORT;

    private static final boolean VERBOSE;

    /** Constant <code>DEFAULT_THREAD_SIZE=Runtime.getRuntime().availableProcessors() + 1</code> */
    private static final int DEFAULT_CORE_POOL_SIZE;

    static {
        VERBOSE = Properties.readBoolProperty(PROP_VERBOSE, false);
        Console.println("Initializing Properties");
        USE_NATIVE_TRANSPORT = Properties.readBoolProperty(PROP_USE_NATIVE, true); //note: native transports are used by default, unless disabled via JVM property
        DEFAULT_CORE_POOL_SIZE = Properties.readIntProperty(PROP_CORE_POOL_SIZE, Runtime.getRuntime().availableProcessors() + 1);
    }

    private Properties() {}

    /**
     * Check if verbose mode is set. Debug statements will be printed in the console.
     *
     * @return {@code true} if verbose setting is set
     */
    public static boolean isVerbose() {
        return VERBOSE;
    }

    /**
     * <p>getDefaultPoolSize.</p>
     *
     * @return a int
     */
    public static int getDefaultPoolSize() {
        return DEFAULT_CORE_POOL_SIZE;
    }

    /**
     * <p>useNativeTransport.</p>
     *
     * @return a boolean
     */
    public static boolean useNativeTransport() {
        return USE_NATIVE_TRANSPORT;
    }

    /**
     * <p>readBoolProperty.</p>
     *
     * @param property
     *         a {@link java.lang.String} object
     * @param defaultValue
     *         a boolean
     *
     * @return a boolean
     */
    public static boolean readBoolProperty(String property, boolean defaultValue) {
        String value = readProperty(property);
        return (value == null) ? defaultValue : BooleanUtils.toBoolean(value);
    }

    /**
     * <p>readProperty.</p>
     *
     * @param property
     *         a {@link java.lang.String} object
     *
     * @return a {@link java.lang.String} object
     */
    public static String readProperty(String property) {
        return readProperty(property, null);
    }

    /**
     * <p>readProperty.</p>
     *
     * @param property
     *         a {@link java.lang.String} object
     * @param defaultValue
     *         a {@link java.lang.String} object
     *
     * @return a {@link java.lang.String} object
     */
    public static String readProperty(String property, String defaultValue) {
        try {
            String value = System.getProperty(property);
            if (value == null)
                return defaultValue;
            return value;
        } catch (Exception e) {
            error("WARNING: Failed to read system property '%s'. Defaulting to 'true' (Reason: %s)", property, e.getMessage());
            return defaultValue;
        }
    }

    /**
     * <p>readIntProperty.</p>
     *
     * @param property
     *         a {@link java.lang.String} object
     * @param defaultValue
     *         a int
     *
     * @return a int
     */
    public static int readIntProperty(String property, int defaultValue) {
        String value = readProperty(property);
        if (value != null && !Strings.isNumeric(value.trim()))
            throw new IllegalArgumentException("Value is not numeric: " + property);
        return value == null ? defaultValue : Integer.parseInt(value.trim());
    }
}
