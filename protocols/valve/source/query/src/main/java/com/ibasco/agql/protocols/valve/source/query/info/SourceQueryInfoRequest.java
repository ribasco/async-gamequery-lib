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

package com.ibasco.agql.protocols.valve.source.query.info;

import com.ibasco.agql.protocols.valve.source.query.common.message.SourceQueryAuthRequest;

/**
 * <p>SourceQueryInfoRequest class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SourceQueryInfoRequest extends SourceQueryAuthRequest {

    private final boolean bypassChallenge;

    /**
     * <p>Constructor for SourceQueryInfoRequest.</p>
     */
    public SourceQueryInfoRequest() {
        this(null);
    }

    /**
     * <p>Constructor for SourceQueryInfoRequest.</p>
     *
     * @param challenge a {@link java.lang.Integer} object
     */
    public SourceQueryInfoRequest(Integer challenge) {
        super(challenge);
        this.bypassChallenge = challenge != null && challenge == -1;
    }

    /**
     * <p>isBypassChallenge.</p>
     *
     * @return a boolean
     */
    public final boolean isBypassChallenge() {
        return bypassChallenge;
    }
}
