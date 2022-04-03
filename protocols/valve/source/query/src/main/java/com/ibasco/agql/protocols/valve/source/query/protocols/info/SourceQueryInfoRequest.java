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

package com.ibasco.agql.protocols.valve.source.query.protocols.info;

import com.ibasco.agql.protocols.valve.source.query.message.SourceQueryAuthRequest;
import org.jetbrains.annotations.ApiStatus;

public class SourceQueryInfoRequest extends SourceQueryAuthRequest {

    private boolean bypassChallenge;

    public SourceQueryInfoRequest() {
        this(null);
    }

    public SourceQueryInfoRequest(Integer challenge) {
        super(challenge);
    }

    public final boolean isBypassChallenge() {
        return bypassChallenge;
    }

    @ApiStatus.Experimental
    public final void setBypassChallenge(boolean bypassChallenge) {
        this.bypassChallenge = bypassChallenge;
    }
}
