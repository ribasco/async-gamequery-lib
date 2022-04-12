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

package com.ibasco.agql.core.exceptions;

import com.google.gson.JsonObject;

/**
 * <p>JsonElementNotFoundException class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class JsonElementNotFoundException extends JsonOperationException {
    /**
     * <p>Constructor for JsonElementNotFoundException.</p>
     *
     * @param jsonObject a {@link com.google.gson.JsonObject} object
     */
    public JsonElementNotFoundException(JsonObject jsonObject) {
        super(jsonObject);
    }

    /**
     * <p>Constructor for JsonElementNotFoundException.</p>
     *
     * @param jsonObject a {@link com.google.gson.JsonObject} object
     * @param cause a {@link java.lang.Throwable} object
     */
    public JsonElementNotFoundException(JsonObject jsonObject, Throwable cause) {
        super(jsonObject, cause);
    }

    /**
     * <p>Constructor for JsonElementNotFoundException.</p>
     *
     * @param jsonObject a {@link com.google.gson.JsonObject} object
     * @param message a {@link java.lang.String} object
     */
    public JsonElementNotFoundException(JsonObject jsonObject, String message) {
        super(jsonObject, message);
    }

    /**
     * <p>Constructor for JsonElementNotFoundException.</p>
     *
     * @param jsonObject a {@link com.google.gson.JsonObject} object
     * @param message a {@link java.lang.String} object
     * @param cause a {@link java.lang.Throwable} object
     */
    public JsonElementNotFoundException(JsonObject jsonObject, String message, Throwable cause) {
        super(jsonObject, message, cause);
    }

    /**
     * <p>Constructor for JsonElementNotFoundException.</p>
     *
     * @param jsonObject a {@link com.google.gson.JsonObject} object
     * @param message a {@link java.lang.String} object
     * @param cause a {@link java.lang.Throwable} object
     * @param enableSuppression a boolean
     * @param writableStackTrace a boolean
     */
    public JsonElementNotFoundException(JsonObject jsonObject, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(jsonObject, message, cause, enableSuppression, writableStackTrace);
    }
}
