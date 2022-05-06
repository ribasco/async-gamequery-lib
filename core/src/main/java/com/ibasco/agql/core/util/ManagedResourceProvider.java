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
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A provider for {@link com.ibasco.agql.core.util.ManagedResource} types.
 *
 * @param <T>
 *         The type of {@link com.ibasco.agql.core.util.ManagedResource}
 *
 * @author Rafael Luis Ibasco
 */
public class ManagedResourceProvider<T extends ManagedResource> {

    private final Map<String, ManagedResource> resourceMap = new ConcurrentHashMap<>();

    private final Function<String, T> factory;

    /**
     * <p>Constructor for ManagedResourceProvider.</p>
     *
     * @param supplier
     *         a {@link java.util.function.Supplier} object
     */
    public ManagedResourceProvider(Supplier<T> supplier) {
        this.factory = s -> supplier.get();
    }

    /**
     * <p>acquire.</p>
     *
     * @param resourceName
     *         a {@link java.lang.String} object
     *
     * @return a T object
     */
    public T acquire(String resourceName) {
        //noinspection unchecked
        T resource = (T) resourceMap.computeIfAbsent(resourceName, factory);
        if (resource == null)
            throw new IllegalStateException("Managed resource not found: " + resourceName);
        resource.retain();
        return resource;
    }
}
