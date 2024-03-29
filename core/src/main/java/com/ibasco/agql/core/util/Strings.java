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

/**
 * <p>Strings class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class Strings {

    /** Constant <code>EMPTY=""</code> */
    public static final String EMPTY = "";

    /**
     * <p>isNumeric.</p>
     *
     * @param str
     *         a {@link java.lang.String} object
     *
     * @return a boolean
     */
    public static boolean isNumeric(String str) {
        if (isBlank(str)) {
            return false;
        }
        final int size = str.length();
        for (int i = 0; i < size; i++) {
            if (!Character.isDigit(str.charAt(i)))
                return false;
        }
        return true;
    }

    /**
     * <p>isBlank.</p>
     *
     * @param str
     *         a {@link java.lang.String} object
     *
     * @return a boolean
     */
    public static boolean isBlank(String str) {
        return str == null || "".equals(str.trim());
    }

    /**
     * <p>defaultIfEmpty.</p>
     *
     * @param value
     *         a {@link java.lang.String} object
     * @param defaultValue
     *         a {@link java.lang.String} object
     *
     * @return a {@link java.lang.String} object
     */
    public static String defaultIfEmpty(String value, String defaultValue) {
        return isBlank(value) ? defaultValue : value;
    }

}
