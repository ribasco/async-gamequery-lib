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

package com.ibasco.agql.protocols.valve.source.query.players;

import com.ibasco.agql.protocols.valve.source.query.SourceQuery;
import com.ibasco.agql.protocols.valve.source.query.common.handlers.SourceQueryAuthEncoder;

/**
 * <p>SourceQueryPlayersEncoder class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SourceQueryPlayersEncoder extends SourceQueryAuthEncoder<SourceQueryPlayerRequest> {

    /**
     * <p>Constructor for SourceQueryPlayersEncoder.</p>
     */
    public SourceQueryPlayersEncoder() {
        super(SourceQueryPlayerRequest.class, SourceQuery.SOURCE_QUERY_PLAYER_REQ);
    }
}
