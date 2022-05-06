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

package com.ibasco.agql.protocols.valve.source.query.challenge;

import com.ibasco.agql.protocols.valve.source.query.common.enums.SourceChallengeType;
import com.ibasco.agql.protocols.valve.source.query.common.message.SourceQueryResponse;

/**
 * <p>SourceQueryChallengeResponse class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SourceQueryChallengeResponse extends SourceQueryResponse<Integer> {

    private final SourceChallengeType type;

    /**
     * <p>Constructor for SourceQueryChallengeResponse.</p>
     *
     * @param challenge
     *         a {@link java.lang.Integer} object
     * @param type
     *         a {@link com.ibasco.agql.protocols.valve.source.query.common.enums.SourceChallengeType} object
     */
    public SourceQueryChallengeResponse(Integer challenge, SourceChallengeType type) {
        super(challenge);
        this.type = type;
    }

    /**
     * <p>Getter for the field <code>type</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.source.query.common.enums.SourceChallengeType} object
     */
    public final SourceChallengeType getType() {
        return type;
    }
}
