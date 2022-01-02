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

package com.ibasco.agql.protocols.valve.source.query.protocols.rules;

import com.ibasco.agql.protocols.valve.source.query.message.SourceQueryResponse;

import java.util.Map;

public class SourceQueryRulesResponse extends SourceQueryResponse {

    private final Map<String, String> rules;

    public SourceQueryRulesResponse(Map<String, String> rules) {
        this.rules = rules;
    }

    public final Map<String, String> getRules() {
        return rules;
    }
}
