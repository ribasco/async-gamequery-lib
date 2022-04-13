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

public class Console {

    public static final String ANSI_RESET = "\u001B[0m";

    public static final String ANSI_BLACK = "\u001B[30m";

    public static final String ANSI_RED = "\u001B[31m";

    public static final String ANSI_GREEN = "\u001B[32m";

    public static final String ANSI_YELLOW = "\u001B[33m";

    public static final String ANSI_BLUE = "\u001B[34m";

    public static final String ANSI_PURPLE = "\u001B[35m";

    public static final String ANSI_CYAN = "\u001B[36m";

    public static final String ANSI_WHITE = "\u001B[37m";

    public static String color(String colorStr, String value) {
        return color(colorStr, value, true);
    }

    public static String color(String colorStr, String value, boolean reset) {
        return colorStr + value + (reset ? ANSI_RESET : "");
    }

    public static String color(String colorStr, String format, boolean reset, Object... args) {
        return colorStr + String.format(format, args) + (reset ? ANSI_RESET : "");
    }

    /**
     * <p>println.</p>
     *
     * @param msg
     *         a {@link String} object
     * @param args
     *         a {@link Object} object
     */
    public static void println(String msg, Object... args) {
        if (!Platform.isVerbose())
            return;
        System.out.printf("\u001B[32m[info]\u001B[0m " + (msg) + "%n", args);
    }

    /**
     * <p>error.</p>
     *
     * @param msg
     *         a {@link String} object
     * @param args
     *         a {@link Object} object
     */
    public static void error(String msg, Object... args) {
        if (!Platform.isVerbose())
            return;
        System.err.printf("\u001B[31m[error]\u001B[0m " + (msg) + "%n", args);
    }

}
