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
 * <p>WebException class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class WebException extends AgqlRuntimeException {

    private int statusCode;

    private String responseBody;

    /**
     * <p>Constructor for WebException.</p>
     */
    public WebException() {
        super();
    }

    /**
     * <p>Constructor for WebException.</p>
     *
     * @param message a {@link java.lang.String} object
     */
    public WebException(String message) {
        super(message);
    }

    /**
     * <p>Constructor for WebException.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param cause a {@link java.lang.Throwable} object
     */
    public WebException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * <p>Constructor for WebException.</p>
     *
     * @param cause a {@link java.lang.Throwable} object
     */
    public WebException(Throwable cause) {
        super(cause);
    }

    /**
     * <p>Constructor for WebException.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param cause a {@link java.lang.Throwable} object
     * @param enableSuppression a boolean
     * @param writableStackTrace a boolean
     */
    public WebException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
