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

package com.ibasco.agql.protocols.valve.source.query.common.message;

/**
 * A special {@link com.ibasco.agql.protocols.valve.source.query.common.message.SourceQueryRequest} which supports challenge based protocols
 *
 * @author Rafael Luis Ibasco
 */
abstract public class SourceQueryAuthRequest extends SourceQueryRequest {

    private Integer challenge;

    private boolean autoUpdate;

    /**
     * <p>Constructor for SourceQueryAuthRequest.</p>
     */
    protected SourceQueryAuthRequest() {
        this(null);
    }

    /**
     * <p>Constructor for SourceQueryAuthRequest.</p>
     *
     * @param challenge
     *         a {@link java.lang.Integer} object
     */
    protected SourceQueryAuthRequest(Integer challenge) {
        this.challenge = challenge;
        this.autoUpdate = challenge == null;
    }

    /**
     * <p>Getter for the field <code>challenge</code>.</p>
     *
     * @return a {@link java.lang.Integer} object
     */
    public Integer getChallenge() {
        return challenge;
    }

    /**
     * <p>Setter for the field <code>challenge</code>.</p>
     *
     * @param challenge
     *         a {@link java.lang.Integer} object
     */
    public void setChallenge(Integer challenge) {
        this.challenge = challenge;
    }

    /**
     * <p>isAutoUpdate.</p>
     *
     * @return a boolean
     */
    public boolean isAutoUpdate() {
        return autoUpdate;
    }

    /**
     * <p>Setter for the field <code>autoUpdate</code>.</p>
     *
     * @param autoUpdate
     *         a boolean
     */
    public void setAutoUpdate(boolean autoUpdate) {
        this.autoUpdate = autoUpdate;
    }
}
