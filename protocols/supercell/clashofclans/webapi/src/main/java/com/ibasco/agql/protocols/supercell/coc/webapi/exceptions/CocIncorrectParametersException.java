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

package com.ibasco.agql.protocols.supercell.coc.webapi.exceptions;

import com.ibasco.agql.core.exceptions.BadRequestException;
import org.jetbrains.annotations.ApiStatus;

/**
 * <p>CocIncorrectParametersException class.</p>
 *
 * @author Rafael Luis Ibasco
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
public class CocIncorrectParametersException extends BadRequestException {

    /**
     * <p>Constructor for CocIncorrectParametersException.</p>
     */
    public CocIncorrectParametersException() {
        super();
    }

    /**
     * <p>Constructor for CocIncorrectParametersException.</p>
     *
     * @param message
     *         a {@link java.lang.String} object
     */
    public CocIncorrectParametersException(String message) {
        super(message);
    }

    /**
     * <p>Constructor for CocIncorrectParametersException.</p>
     *
     * @param message
     *         a {@link java.lang.String} object
     * @param cause
     *         a {@link java.lang.Throwable} object
     */
    public CocIncorrectParametersException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * <p>Constructor for CocIncorrectParametersException.</p>
     *
     * @param cause
     *         a {@link java.lang.Throwable} object
     */
    public CocIncorrectParametersException(Throwable cause) {
        super(cause);
    }

    /**
     * <p>Constructor for CocIncorrectParametersException.</p>
     *
     * @param message
     *         a {@link java.lang.String} object
     * @param cause
     *         a {@link java.lang.Throwable} object
     * @param enableSuppression
     *         a boolean
     * @param writableStackTrace
     *         a boolean
     */
    public CocIncorrectParametersException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
