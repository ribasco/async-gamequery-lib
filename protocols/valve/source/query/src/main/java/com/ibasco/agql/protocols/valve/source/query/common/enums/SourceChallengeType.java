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

package com.ibasco.agql.protocols.valve.source.query.common.enums;

import com.ibasco.agql.protocols.valve.source.query.SourceQuery;

/**
 * Enumeration identifying the type of query of a challenge request
 *
 * @author Rafael Luis Ibasco
 */
public enum SourceChallengeType {
    INFO(SourceQuery.SOURCE_QUERY_INFO_REQ),
    PLAYER(SourceQuery.SOURCE_QUERY_PLAYER_REQ),
    RULES(SourceQuery.SOURCE_QUERY_RULES_REQ),
    CHALLENGE(SourceQuery.SOURCE_QUERY_CHALLENGE_REQ);

    private final int header;

    SourceChallengeType(int header) {
        this.header = header;
    }

    /**
     * <p>Getter for the field <code>header</code>.</p>
     *
     * @return a int
     */
    public int getHeader() {
        return header;
    }
}
