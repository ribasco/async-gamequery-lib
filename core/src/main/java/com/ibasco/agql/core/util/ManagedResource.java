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

import org.slf4j.LoggerFactory;

import java.io.Closeable;

/**
 * A reference counted resource that will be automatically closed once the count reaches zero.
 *
 * @param <T>
 *         The underlying type of the managed resource
 * @author Rafael Luis Ibasco
 */
public interface ManagedResource<T> extends Closeable {

    /**
     * <p>getReferenceCount.</p>
     *
     * @return a int
     */
    int getReferenceCount();

    /**
     * <p>retain.</p>
     */
    void retain();

    /**
     * <p>release.</p>
     *
     * @return a int
     */
    int release();

    /**
     * <p>release.</p>
     *
     * @param count a int
     * @return a int
     */
    int release(int count);

    /**
     * <p>getResource.</p>
     *
     * @return a T object
     */
    T getResource();

    /**
     * <p>isManaged.</p>
     *
     * @param resource a {@link java.lang.Object} object
     * @return a boolean
     */
    static boolean isManaged(Object resource) {
        return resource instanceof ManagedResource<?>;
    }

    /**
     * Utility function for releasing {@link com.ibasco.agql.core.util.ManagedResource} type instances. If the instance is not a {@link com.ibasco.agql.core.util.ManagedResource} the function will simply return.
     *
     * @param resource
     *         The resource to release
     * @return The updated reference count
     */
    static int release(Object resource) {
        return release(resource, 1);
    }

    /**
     * Utility function for releasing {@link com.ibasco.agql.core.util.ManagedResource} type instances. If the instance is not a {@link com.ibasco.agql.core.util.ManagedResource} the function will simply return.
     *
     * @param resource
     *         The resource to release
     * @return The updated reference count
     * @param releaseCount a int
     */
    static int release(Object resource, int releaseCount) {
        if (!(resource instanceof ManagedResource<?>))
            return -1;
        int count = ((ManagedResource<?>) resource).release(releaseCount);
        LoggerFactory.getLogger(ManagedResource.class).debug("MANAGED_RESOURCE => Releasing resource {} (New reference count: {})", resource, count);
        //System.out.printf("Released managed resource: %s, New reference count: %d\n", resource, count);
        return count;
    }
}
