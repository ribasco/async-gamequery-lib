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

package com.ibasco.agql.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p>Abstract AbstractCriteriaBuilder class.</p>
 *
 * @author Rafael Luis Ibasco
 */
abstract public class AbstractCriteriaBuilder<T extends AbstractCriteriaBuilder> {

    private final Map<String, Object> criteriaMap = new HashMap<>();

    /**
     * <p>put.</p>
     *
     * @param name
     *         a {@link java.lang.String} object
     * @param value
     *         a {@link java.lang.Object} object
     *
     * @return a T object
     */
    protected T put(String name, Object value) {
        criteriaMap.put(name, value);
        //noinspection unchecked
        return (T) this;
    }

    /**
     * <p>getCriteriaSet.</p>
     *
     * @return a {@link java.util.Set} object
     */
    public Set<Map.Entry<String, Object>> getCriteriaSet() {
        return criteriaMap.entrySet();
    }
}
