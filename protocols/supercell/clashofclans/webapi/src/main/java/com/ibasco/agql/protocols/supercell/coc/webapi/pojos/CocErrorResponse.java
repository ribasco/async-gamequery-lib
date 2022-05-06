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

package com.ibasco.agql.protocols.supercell.coc.webapi.pojos;

import org.jetbrains.annotations.ApiStatus;

/**
 * <p>CocErrorResponse class.</p>
 *
 * @author Rafael Luis Ibasco
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
public class CocErrorResponse {

    private String reason;

    private String message;

    /**
     * <p>Getter for the field <code>reason</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getReason() {
        return reason;
    }

    /**
     * <p>Setter for the field <code>reason</code>.</p>
     *
     * @param reason
     *         a {@link java.lang.String} object
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * <p>Getter for the field <code>message</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getMessage() {
        return message;
    }

    /**
     * <p>Setter for the field <code>message</code>.</p>
     *
     * @param message
     *         a {@link java.lang.String} object
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
