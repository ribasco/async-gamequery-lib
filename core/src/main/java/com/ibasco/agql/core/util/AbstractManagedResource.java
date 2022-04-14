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

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>Abstract AbstractManagedResource class.</p>
 *
 * @author Rafael Luis Ibasco
 */
abstract public class AbstractManagedResource<T> implements ManagedResource<T> {

    private final AtomicInteger referenceCount = new AtomicInteger();

    private final T resource;

    /**
     * <p>Constructor for AbstractManagedResource.</p>
     *
     * @param resource a T object
     */
    protected AbstractManagedResource(T resource) {
        this.resource = resource;
    }

    /** {@inheritDoc} */
    @Override
    public int getReferenceCount() {
        return referenceCount.get();
    }

    /** {@inheritDoc} */
    @Override
    public void retain() {
        referenceCount.incrementAndGet();
    }

    /** {@inheritDoc} */
    @Override
    public int release() {
        return release(1);
    }

    /** {@inheritDoc} */
    @Override
    public int release(int releaseCount) {
        int currentCount = referenceCount.get();
        Console.println("Requested to release resource '%s' (Subtrahend: %d, Old Ref Count: %d, New Ref Count: %d)", this, releaseCount, currentCount, currentCount - releaseCount);
        if (currentCount <= 0)
            throw new IllegalStateException("Resource has already been released");
        int newCount = Math.max(currentCount - releaseCount, 0);
        if (referenceCount.compareAndSet(currentCount, newCount)) {
            if (newCount <= 0) {
                try {
                    Console.println("Reference count of resource '%s' has reached 0. Attempting to close", this);
                    close();
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
            return newCount;
        } else {
            throw new IllegalStateException(String.format("Failed to update reference count (Expected: %d, Actual: %d)", currentCount, referenceCount.get()));
        }
    }

    /** {@inheritDoc} */
    @Override
    public final T getResource() {
        return resource;
    }
}
