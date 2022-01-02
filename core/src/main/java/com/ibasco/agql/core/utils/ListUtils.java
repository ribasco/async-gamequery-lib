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

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Deprecated
public class ListUtils {
    /**
     * <p>Convert the internal type of the List to another type (e.g. From List<String> to List<Integer>)</p>
     *
     * @param from The {@link List} to convert
     * @param func The {@link Function} that will perform the conversion
     * @param <T>  The origin Type
     * @param <U>  The destination Type
     *
     * @return A {@link List} containing the array elements of the converted type
     */
    public static <T, U> List<U> convertList(List<T> from, Function<T, U> func) {
        return from.stream().map(func).collect(Collectors.toList());
    }
}
