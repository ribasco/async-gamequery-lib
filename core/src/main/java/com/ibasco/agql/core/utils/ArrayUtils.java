/*
 * Copyright 2018-2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.core.utils;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.IntFunction;

@Deprecated
public class ArrayUtils {

    /**
     * <p>Convert one array type to another</p>
     *
     * @param from
     *         The array to convert
     * @param func
     *         A {@link Function} responsible for the type conversion
     * @param generator
     *         An {@link IntFunction} that will generate the instances of the desired type
     * @param <T>
     *         The source type
     * @param <U>
     *         The destination type
     *
     * @return An array of the converted type instances
     *
     * @see <a href="http://stackoverflow.com/questions/23057549/lambda-expression-to-convert-array-list-of-string-to-array-list-of-integers">Lambda expression to convert array/List of String to array/List of Integers</a>
     */
    public static <T, U> U[] convertArray(T[] from, Function<T, U> func, IntFunction<U[]> generator) {
        return Arrays.stream(from).map(func).toArray(generator);
    }
}
