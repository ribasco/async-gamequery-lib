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

import java.util.concurrent.CompletionException;

/**
 * Utilities for {@link java.lang.Exception} handling
 *
 * @author Rafael Luis Ibasco
 */
public class Errors {

    /**
     * <p>unwrap.</p>
     *
     * @param error
     *         a {@link java.lang.Throwable} object
     *
     * @return a {@link java.lang.Throwable} object
     */
    public static Throwable unwrap(Throwable error) {
        if (error == null)
            return null;
        if (error.getCause() != null)
            return error.getCause();
        return error;
    }

    /**
     * <p>unwrapAndThrow.</p>
     *
     * @param s
     *         a U object
     * @param error
     *         a {@link java.lang.Throwable} object
     * @param <U>
     *         a U class
     *
     * @return a U object
     */
    public static <U> U unwrapAndThrow(U s, Throwable error) {
        unwrapAndThrow(error);
        return s;
    }

    /**
     * <p>unwrapAndThrow.</p>
     *
     * @param error
     *         a {@link java.lang.Throwable} object
     */
    public static void unwrapAndThrow(Throwable error) {
        if (error == null)
            return;
        if (error instanceof CompletionException)
            throw (CompletionException) error;
        throw new CompletionException(error);
    }
}
