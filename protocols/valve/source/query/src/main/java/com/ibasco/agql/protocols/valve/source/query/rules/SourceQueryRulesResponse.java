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

package com.ibasco.agql.protocols.valve.source.query.rules;

import com.ibasco.agql.protocols.valve.source.query.common.message.SourceQueryResponse;

import java.util.Map;

/**
 * <p>SourceQueryRulesResponse class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SourceQueryRulesResponse extends SourceQueryResponse<Map<String, String>> {

    private final int expectedCount;

    /**
     * <p>Constructor for SourceQueryRulesResponse.</p>
     *
     * @param rules a {@link java.util.Map} object
     * @param expectedCount a int
     */
    public SourceQueryRulesResponse(Map<String, String> rules, int expectedCount) {
        super(rules);
        this.expectedCount = expectedCount;
    }

    /**
     * <p>Getter for the field <code>expectedCount</code>.</p>
     *
     * @return The total number of rules expected to be received.
     */
    public final int getExpectedCount() {
        return expectedCount;
    }
}
