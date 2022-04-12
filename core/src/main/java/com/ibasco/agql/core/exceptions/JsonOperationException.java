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
 * <p>JsonOperationException class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class JsonOperationException extends WebException {

    private JsonObject jsonObject;

    /**
     * <p>Constructor for JsonOperationException.</p>
     *
     * @param jsonObject a {@link com.google.gson.JsonObject} object
     */
    public JsonOperationException(JsonObject jsonObject) {
        this(jsonObject, "");
    }

    /**
     * <p>Constructor for JsonOperationException.</p>
     *
     * @param jsonObject a {@link com.google.gson.JsonObject} object
     * @param cause a {@link java.lang.Throwable} object
     */
    public JsonOperationException(JsonObject jsonObject, Throwable cause) {
        this(jsonObject, null, cause);
    }

    /**
     * <p>Constructor for JsonOperationException.</p>
     *
     * @param jsonObject a {@link com.google.gson.JsonObject} object
     * @param message a {@link java.lang.String} object
     */
    public JsonOperationException(JsonObject jsonObject, String message) {
        this(jsonObject, message, null);
    }

    /**
     * <p>Constructor for JsonOperationException.</p>
     *
     * @param jsonObject a {@link com.google.gson.JsonObject} object
     * @param message a {@link java.lang.String} object
     * @param cause a {@link java.lang.Throwable} object
     */
    public JsonOperationException(JsonObject jsonObject, String message, Throwable cause) {
        this(jsonObject, message, cause, false, false);
    }

    /**
     * <p>Constructor for JsonOperationException.</p>
     *
     * @param jsonObject a {@link com.google.gson.JsonObject} object
     * @param message a {@link java.lang.String} object
     * @param cause a {@link java.lang.Throwable} object
     * @param enableSuppression a boolean
     * @param writableStackTrace a boolean
     */
    public JsonOperationException(JsonObject jsonObject, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.jsonObject = jsonObject;
    }

    /**
     * <p>Getter for the field <code>jsonObject</code>.</p>
     *
     * @return a {@link com.google.gson.JsonObject} object
     */
    public JsonObject getJsonObject() {
        return jsonObject;
    }

    /**
     * <p>Setter for the field <code>jsonObject</code>.</p>
     *
     * @param jsonObject a {@link com.google.gson.JsonObject} object
     */
    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
