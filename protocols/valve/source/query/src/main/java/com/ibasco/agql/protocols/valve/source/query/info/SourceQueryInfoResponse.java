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

import com.ibasco.agql.protocols.valve.source.query.common.message.SourceQueryResponse;

/**
 * <p>SourceQueryInfoResponse class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SourceQueryInfoResponse extends SourceQueryResponse<SourceServer> {

    /**
     * <p>Constructor for SourceQueryInfoResponse.</p>
     *
     * @param sourceServer a {@link com.ibasco.agql.protocols.valve.source.query.info.SourceServer} object
     */
    public SourceQueryInfoResponse(SourceServer sourceServer) {
        super(sourceServer);
    }
}
