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

public interface Configurable {

    OptionMap getOptions();

    default <V> V getOrDefault(Option<V> option) {
        return getOrDefault(option, null);
    }

    default <V> V getOrDefault(Option<V> option, V defaultValue) {
        return getOptions().get(option, defaultValue);
    }

    default <V> V get(Option<V> option) {
        return getOptions().get(option);
    }

    default <V> void set(Option<V> option, V value) {
        getOptions().add(option, value);
    }

    default <V> void lock(Option<V> option, V value) {
        getOptions().add(option, value,true);
    }

    default <V> void unlock(Option<V> option) {
        if (!getOptions().isLocked(option))
            return;
        getOptions().get(option);
    }
}
