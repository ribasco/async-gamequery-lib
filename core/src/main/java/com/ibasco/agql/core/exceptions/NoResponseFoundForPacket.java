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

import com.ibasco.agql.core.AbstractPacket;

/**
 * <p>NoResponseFoundForPacket class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class NoResponseFoundForPacket extends ResponseProcessingException {

    private AbstractPacket responsePacket;

    /**
     * <p>Constructor for NoResponseFoundForPacket.</p>
     */
    public NoResponseFoundForPacket() {
    }

    /**
     * <p>Constructor for NoResponseFoundForPacket.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param packet a {@link com.ibasco.agql.core.AbstractPacket} object
     */
    public NoResponseFoundForPacket(String message, AbstractPacket packet) {
        super(message);
        this.responsePacket = packet;
    }

    /**
     * <p>Constructor for NoResponseFoundForPacket.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param cause a {@link java.lang.Throwable} object
     */
    public NoResponseFoundForPacket(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * <p>Constructor for NoResponseFoundForPacket.</p>
     *
     * @param cause a {@link java.lang.Throwable} object
     */
    public NoResponseFoundForPacket(Throwable cause) {
        super(cause);
    }

    /**
     * <p>Constructor for NoResponseFoundForPacket.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param cause a {@link java.lang.Throwable} object
     * @param enableSuppression a boolean
     * @param writableStackTrace a boolean
     */
    public NoResponseFoundForPacket(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * <p>Getter for the field <code>responsePacket</code>.</p>
     *
     * @return a {@link com.ibasco.agql.core.AbstractPacket} object
     */
    public AbstractPacket getResponsePacket() {
        return responsePacket;
    }

    /**
     * <p>Setter for the field <code>responsePacket</code>.</p>
     *
     * @param responsePacket a {@link com.ibasco.agql.core.AbstractPacket} object
     */
    public void setResponsePacket(AbstractPacket responsePacket) {
        this.responsePacket = responsePacket;
    }
}
