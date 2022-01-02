/*
 * Copyright 2022-2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.protocols.valve.source.query.message;

/**
 * A special {@link SourceQueryRequest} which supports challenge based protocols
 *
 * @author Rafael Luis Ibasco
 */
abstract public class SourceQueryAuthRequest extends SourceQueryRequest {

    private Integer challenge;

    private boolean autoUpdate = true;

    protected SourceQueryAuthRequest() {
        this(null);
    }

    protected SourceQueryAuthRequest(Integer challenge) {
        this.challenge = challenge;
    }

    public Integer getChallenge() {
        return challenge;
    }

    public void setChallenge(Integer challenge) {
        this.challenge = challenge;
    }

    public boolean isAutoUpdate() {
        return autoUpdate;
    }

    public void setAutoUpdate(boolean autoUpdate) {
        this.autoUpdate = autoUpdate;
    }
}
