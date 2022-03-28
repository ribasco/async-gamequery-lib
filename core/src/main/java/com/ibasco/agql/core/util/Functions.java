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

import com.ibasco.agql.core.NettyChannelContext;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Function;

/**
 * Utilities for Functional Interfaces
 *
 * @author Rafael Luis Ibasco
 */
@ApiStatus.Internal
public class Functions {

    public static final Function<Object, Boolean> TRUE = unused -> true;

    public static final Function<Object, Boolean> FALSE = unused -> false;

    public static <B, C extends B> C convert(B b) {
        //noinspection unchecked
        return (C) b;
    }

    public static <A> A returnFirst(A a) {
        return a;
    }

    public static <A, B> A selectFirst(A a, B b) {
        return a;
    }

    public static <A, B> B selectSecond(A a, B b) {
        return b;
    }

    public static boolean isTypeOf(Object v, Class<?>... arr) {
        if (arr == null || arr.length == 0)
            return false;
        for (Class<?> c : arr) {
            if (c.equals(v.getClass()))
                return true;
        }
        return false;
    }

    public static NettyChannelContext supplyThis(NettyChannelContext c) {
        return c;
    }
}
