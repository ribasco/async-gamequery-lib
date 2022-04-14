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

import java.util.Map;

//TODO: Convert this to abstract class, use this as a replacement for OptionContainer

/**
 * A special map for storing configuration {@link com.ibasco.agql.core.util.Option} instances.
 *
 * @author Rafael Luis Ibasco
 */
public interface Options extends Cloneable, Iterable<Map.Entry<Option<?>, Object>> {

    <X> void add(Option<X> option, X value);

    <X> void add(Option<X> option, X value, boolean locked);

    boolean isLocked(Option<?> option);

    boolean contains(Option<?> option);

    <X> void remove(Option<X> option);

    <X> X get(Option<X> option);

    <X> X get(Option<X> option, X defaultValue);

    <X> X getOrDefault(Option<X> option);

    int size();
}
