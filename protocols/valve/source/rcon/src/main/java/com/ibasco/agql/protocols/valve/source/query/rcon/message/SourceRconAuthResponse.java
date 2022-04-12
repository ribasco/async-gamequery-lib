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

package com.ibasco.agql.protocols.valve.source.query.rcon.message;

import com.ibasco.agql.protocols.valve.source.query.rcon.enums.SourceRconAuthReason;

/**
 * <p>SourceRconAuthResponse class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public final class SourceRconAuthResponse extends SourceRconResponse<Boolean> {

    private final String reason;

    private final SourceRconAuthReason reasonCode;

    /**
     * <p>Constructor for SourceRconAuthResponse.</p>
     *
     * @param authenticated a boolean
     */
    public SourceRconAuthResponse(boolean authenticated) {
        this(authenticated, null, null);
    }

    /**
     * <p>Constructor for SourceRconAuthResponse.</p>
     *
     * @param authenticated a boolean
     * @param reason a {@link java.lang.String} object
     * @param reasonCode a {@link com.ibasco.agql.protocols.valve.source.query.rcon.enums.SourceRconAuthReason} object
     */
    public SourceRconAuthResponse(boolean authenticated, String reason, SourceRconAuthReason reasonCode) {
        super(authenticated);
        this.reason = reason;
        this.reasonCode = reasonCode;
    }

    /**
     * <p>isAuthenticated.</p>
     *
     * @return a boolean
     */
    public boolean isAuthenticated() {
        return super.getResult();
    }

    /**
     * <p>Getter for the field <code>reason</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getReason() {
        return reason;
    }

    /**
     * <p>Getter for the field <code>reasonCode</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.source.query.rcon.enums.SourceRconAuthReason} object
     */
    public SourceRconAuthReason getReasonCode() {
        return reasonCode;
    }
}
