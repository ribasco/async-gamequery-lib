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

package com.ibasco.agql.core.exceptions;

/**
 * <p>ReadTimeoutException class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class ReadTimeoutException extends TimeoutException {

    /** Constant <code>INSTANCE</code> */
    public static final ReadTimeoutException INSTANCE = new ReadTimeoutException();

    /**
     * <p>Constructor for ReadTimeoutException.</p>
     */
    public ReadTimeoutException() {
    }

    /**
     * <p>Constructor for ReadTimeoutException.</p>
     *
     * @param message
     *         a {@link java.lang.String} object
     */
    public ReadTimeoutException(String message) {
        super(message);
    }

    /**
     * <p>Constructor for ReadTimeoutException.</p>
     *
     * @param message
     *         a {@link java.lang.String} object
     * @param cause
     *         a {@link java.lang.Throwable} object
     */
    public ReadTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
