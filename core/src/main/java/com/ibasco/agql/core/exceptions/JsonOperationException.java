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

package com.ibasco.agql.core.exceptions;

import com.google.gson.JsonObject;

public class JsonOperationException extends WebException {

    private JsonObject jsonObject;

    public JsonOperationException(JsonObject jsonObject) {
        this(jsonObject, "");
    }

    public JsonOperationException(JsonObject jsonObject, Throwable cause) {
        this(jsonObject, null, cause);
    }

    public JsonOperationException(JsonObject jsonObject, String message) {
        this(jsonObject, message, null);
    }

    public JsonOperationException(JsonObject jsonObject, String message, Throwable cause) {
        this(jsonObject, message, cause, false, false);
    }

    public JsonOperationException(JsonObject jsonObject, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.jsonObject = jsonObject;
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
