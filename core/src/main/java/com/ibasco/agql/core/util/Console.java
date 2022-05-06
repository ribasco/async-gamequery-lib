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
 * <p>Console class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public final class Console {

    /** Constant <code>RESET="\u001B[0m"</code> */
    public static final String RESET = "\u001B[0m";

    /** Constant <code>BLACK="\u001B[30m"</code> */
    public static final String BLACK = "\u001B[30m";

    /** Constant <code>RED="\u001B[31m"</code> */
    public static final String RED = "\u001B[31m";

    /** Constant <code>GREEN="\u001B[32m"</code> */
    public static final String GREEN = "\u001B[32m";

    /** Constant <code>YELLOW="\u001B[33m"</code> */
    public static final String YELLOW = "\u001B[33m";

    /** Constant <code>BLUE="\u001B[34m"</code> */
    public static final String BLUE = "\u001B[34m";

    /** Constant <code>PURPLE="\u001B[35m"</code> */
    public static final String PURPLE = "\u001B[35m";

    /** Constant <code>CYAN="\u001B[36m"</code> */
    public static final String CYAN = "\u001B[36m";

    /** Constant <code>WHITE="\u001B[37m"</code> */
    public static final String WHITE = "\u001B[37m";

    /**
     * <p>printLine.</p>
     */
    public static void printLine() {
        printLine(null);
    }

    /**
     * <p>printLine.</p>
     *
     * @param color
     *         a {@link java.lang.String} object
     */
    public static void printLine(String color) {
        if (color == null)
            color = BLUE;
        println(color(color, "------------------------------------------------------------------------------------------------------------------"));
    }

    /**
     * <p>println.</p>
     *
     * @param msg
     *         a {@link java.lang.String} object
     * @param args
     *         a {@link java.lang.Object} object
     */
    public static void println(String msg, Object... args) {
        if (!Properties.isVerbose())
            return;
        System.out.printf(color(GREEN, "[%-11s] ", true, Thread.currentThread().getName()) + msg + "%n", args);
    }

    /**
     * <p>color.</p>
     *
     * @param colorStr
     *         a {@link java.lang.String} object
     * @param value
     *         a {@link java.lang.String} object
     *
     * @return a {@link java.lang.String} object
     */
    public static String color(String colorStr, String value) {
        return color(colorStr, value, true);
    }

    /**
     * <p>color.</p>
     *
     * @param colorStr
     *         a {@link java.lang.String} object
     * @param format
     *         a {@link java.lang.String} object
     * @param reset
     *         a boolean
     * @param args
     *         a {@link java.lang.Object} object
     *
     * @return a {@link java.lang.String} object
     */
    public static String color(String colorStr, String format, boolean reset, Object... args) {
        return colorStr + String.format(format, args) + (reset ? RESET : "");
    }

    /**
     * <p>color.</p>
     *
     * @param colorStr
     *         a {@link java.lang.String} object
     * @param value
     *         a {@link java.lang.String} object
     * @param reset
     *         a boolean
     *
     * @return a {@link java.lang.String} object
     */
    public static String color(String colorStr, String value, boolean reset) {
        return colorStr + value + (reset ? RESET : "");
    }

    /**
     * <p>error.</p>
     *
     * @param msg
     *         a {@link java.lang.String} object
     * @param args
     *         a {@link java.lang.Object} object
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

    /**
     * <p>colorize.</p>
     *
     * @return a {@link com.ibasco.agql.core.util.Console.Colorize} object
     */
    public static Colorize colorize() {
        return new Colorize();
    }

    public static Colorize colorize(boolean allowPrinting) {
        return new Colorize(allowPrinting);
    }

    public static class Colorize {

        private final StringBuilder builder;

        private final boolean allowPrinting;

        private boolean allowColors;

        private Colorize() {
            this(Properties.isVerbose());
        }

        private Colorize(boolean allowPrinting) {
            this.builder = new StringBuilder();
            this.allowPrinting = allowPrinting;
            this.allowColors = true;
        }

        public Colorize enableColors() {
            this.allowColors = true;
            return this;
        }

        public Colorize disableColors() {
            this.allowColors = false;
            return this;
        }

        public Colorize red() {
            return append(RED);
        }

        private Colorize append(String color) {
            return append(color, null);
        }

        private Colorize append(String color, String format, Object... args) {
            if (allowColors)
                builder.append(color);
            if (!Strings.isBlank(format))
                builder.append(String.format(format, args));
            return this;
        }

        public Colorize red(String format, Object... args) {
            return append(RED, format, args);
        }

        public Colorize yellow() {
            return append(YELLOW);
        }

        public Colorize yellow(String format, Object... args) {
            return append(YELLOW, format, args);
        }

        public Colorize purple() {
            return append(PURPLE);
        }

        public Colorize purple(String format, Object... args) {
            return append(PURPLE, format, args);
        }

        public Colorize cyan() {
            return append(CYAN);
        }

        public Colorize cyan(String format, Object... args) {
            return append(CYAN, format, args);
        }

        public Colorize line() {
            builder.append(System.lineSeparator());
            return this;
        }

        public Colorize reset() {
            return append(RESET);
        }

        public Colorize reset(String format, Object... args) {
            return append(RESET, format, args);
        }

        public Colorize black() {
            return append(BLACK);
        }

        public Colorize black(String format, Object... args) {
            return append(BLACK, format, args);
        }

        public Colorize white() {
            return append(WHITE);
        }

        public Colorize white(String format, Object... args) {
            return append(WHITE, format, args);
        }

        public Colorize blue() {
            return append(BLUE);
        }

        public Colorize blue(String format, Object... args) {
            return append(BLUE, format, args);
        }

        public Colorize green() {
            return append(GREEN);
        }

        public Colorize green(String format, Object... args) {
            return append(GREEN, format, args);
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
            if (allowPrinting)
                System.out.print(builder);
        }

        public void println() {
            if (allowPrinting)
                System.out.println(builder);
        }

        public void printErr() {
            if (allowPrinting)
                System.err.print(builder);
        }

        public void printErrln() {
            if (allowPrinting)
                System.err.println(builder);
        }

        @Override
        public String toString() {
            return builder.toString();
        }
    }
}
