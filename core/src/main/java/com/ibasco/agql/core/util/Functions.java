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

import org.jetbrains.annotations.ApiStatus;

import java.util.function.Function;

/**
 * Utilities for Functional Interfaces
 *
 * @author Rafael Luis Ibasco
 */
@ApiStatus.Internal
public class Functions {

    /** Constant <code>TRUE</code> */
    public static final Function<Object, Boolean> TRUE = unused -> true;

    /** Constant <code>FALSE</code> */
    public static final Function<Object, Boolean> FALSE = unused -> false;

    /**
     * <p>Cast argument to it's returning type</p>
     *
     * @param a
     *         a A object
     * @param <A>
     *         The captured type of the parameter
     * @param <B>
     *         The captured return type
     * @return The casted instance
     */
    public static <A, B> B cast(A a) {
        //noinspection unchecked
        return (B) a;
    }

    /**
     * <p>Cast argument into the captured type</p>
     *
     * @param b
     *         a B object
     * @param <B>
     *         a B class
     * @param <C>
     *         a C class
     * @return a C object
     */
    public static <B, C extends B> C convert(B b) {
        //noinspection unchecked
        return (C) b;
    }

    /**
     * <p>returnArg.</p>
     *
     * @param a
     *         a A object
     * @param <A>
     *         a A class
     * @return a A object
     */
    public static <A> A returnArg(A a) {
        return a;
    }

    /**
     * <p>Select first argument of the function and return it</p>
     *
     * @param a
     *         a A object
     * @param b
     *         a B object
     * @param <A>
     *         a A class
     * @param <B>
     *         a B class
     * @return a A object
     */
    public static <A, B> A selectFirst(A a, B b) {
        return a;
    }

    /**
     * <p>Select second argument of the function and return it</p>
     *
     * @param a
     *         a A object
     * @param b
     *         a B object
     * @param <A>
     *         a A class
     * @param <B>
     *         a B class
     * @return a B object
     */
    public static <A, B> B selectSecond(A a, B b) {
        return b;
    }

    /**
     * <p>isTypeOf.</p>
     *
     * @param v
     *         a {@link java.lang.Object} object
     * @param arr
     *         a {@link java.lang.Class} object
     * @return a boolean
     */
    public static boolean isTypeOf(Object v, Class<?>... arr) {
        if (arr == null || arr.length == 0)
            return false;
        for (Class<?> c : arr) {
            if (c.equals(v.getClass()))
                return true;
        }
        return false;
    }
}
