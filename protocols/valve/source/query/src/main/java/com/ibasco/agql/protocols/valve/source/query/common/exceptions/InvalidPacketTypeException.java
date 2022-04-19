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

package com.ibasco.agql.protocols.valve.source.query.common.exceptions;

import com.ibasco.agql.core.exceptions.AgqlRuntimeException;

/**
 * <p>InvalidPacketTypeException class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class InvalidPacketTypeException extends AgqlRuntimeException {

    private final int type;

    /**
     * <p>Constructor for InvalidPacketTypeException.</p>
     *
     * @param type
     *         a int
     * @param message
     *         a {@link java.lang.String} object
     */
    public InvalidPacketTypeException(int type, String message) {
        super(message);
        this.type = type;
    }

    /**
     * <p>Getter for the field <code>type</code>.</p>
     *
     * @return a int
     */
    public final int getType() {
        return type;
    }
}
