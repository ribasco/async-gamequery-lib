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

public class JsonElementNotFoundException extends JsonOperationException {
    public JsonElementNotFoundException(JsonObject jsonObject) {
        super(jsonObject);
    }

    public JsonElementNotFoundException(JsonObject jsonObject, Throwable cause) {
        super(jsonObject, cause);
    }

    public JsonElementNotFoundException(JsonObject jsonObject, String message) {
        super(jsonObject, message);
    }

    public JsonElementNotFoundException(JsonObject jsonObject, String message, Throwable cause) {
        super(jsonObject, message, cause);
    }

    public JsonElementNotFoundException(JsonObject jsonObject, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(jsonObject, message, cause, enableSuppression, writableStackTrace);
    }
}
