/*
 * Copyright 2018-2022 Asynchronous Game Query Library
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

package com.ibasco.agql.core.transport.http.processors;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.ibasco.agql.core.transport.http.ContentTypeProcessor;

public class JsonContentTypeProcessor extends ContentTypeProcessor<JsonElement> {
    private final JsonParser parser = new JsonParser();

    @Override
    protected JsonElement processContent(String body) {
        return parser.parse(body);
    }
}
