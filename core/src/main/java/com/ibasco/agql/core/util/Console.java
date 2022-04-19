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

    public static final String RESET = "\u001B[0m";

    public static final String BLACK = "\u001B[30m";

    public static final String RED = "\u001B[31m";

    public static final String GREEN = "\u001B[32m";

    public static final String YELLOW = "\u001B[33m";

    public static final String BLUE = "\u001B[34m";

    public static final String PURPLE = "\u001B[35m";

    public static final String CYAN = "\u001B[36m";

    public static final String WHITE = "\u001B[37m";

    public static String color(String colorStr, String value) {
        return color(colorStr, value, true);
    }

    public static String color(String colorStr, String value, boolean reset) {
        return colorStr + value + (reset ? RESET : "");
    }

    public static String color(String colorStr, String format, boolean reset, Object... args) {
        return colorStr + String.format(format, args) + (reset ? RESET : "");
    }

    public static void printLine() {
        printLine(null);
    }

    public static void printLine(String color) {
        if (color == null)
            color = BLUE;
        println(color(color, "------------------------------------------------------------------------------------------------------------------"));
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
        if (!Properties.isVerbose())
            return;
        System.out.printf(color(GREEN, "[%-11s] ", true, Thread.currentThread().getName()) + msg + "%n", args);
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
        if (!Properties.isVerbose())
            return;
        Object[] merged = args != null && args.length > 0 ? new Object[args.length + 1] : new Object[1];
        merged[0] = Thread.currentThread().getName().toLowerCase();
        if (args != null)
            System.arraycopy(args, 0, merged, 1, args.length);
        System.err.printf("[%-11s]\u001B[37m " + color(RESET, msg) + "\u001B[0m%n", merged);
    }

    public static Colorize colorize() {
        return new Colorize();
    }

    public static class Colorize {

        private final StringBuilder builder;

        private Colorize() {
            builder = new StringBuilder();
        }

        public Colorize red() {
            builder.append(RED);
            return this;
        }

        public Colorize yellow() {
            builder.append(YELLOW);
            return this;
        }

        public Colorize cyan() {
            builder.append(CYAN);
            return this;
        }

        public Colorize reset() {
            builder.append(RESET);
            return this;
        }

        public Colorize black() {
            builder.append(BLACK);
            return this;
        }

        public Colorize white() {
            builder.append(WHITE);
            return this;
        }

        public Colorize blue() {
            builder.append(BLUE);
            return this;
        }

        public Colorize green() {
            builder.append(GREEN);
            return this;
        }

        public Colorize text(String text) {
            builder.append(text);
            return this;
        }

        public Colorize text(String format, Object... args) {
            builder.append(String.format(format, args));
            return this;
        }

        public Colorize textln(String text) {
            builder.append(text).append(System.lineSeparator());
            return this;
        }

        public Colorize textln(String format, Object... args) {
            builder.append(String.format(format + "%n", args));
            return this;
        }

        public Colorize clear() {
            System.out.print("\033[H\033[2J");
            return this;
        }

        public void print() {
            if (Properties.isVerbose())
                System.out.print(builder);
        }

        public void println() {
            if (Properties.isVerbose())
                System.out.println(builder);
        }

        public void printErr() {
            if (Properties.isVerbose())
                System.err.print(builder);
        }

        public void printErrln() {
            if (Properties.isVerbose())
                System.err.println(builder);
        }

        @Override
        public String toString() {
            return builder.toString();
        }
    }
}
