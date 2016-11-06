package org.ribasco.agql.core.exceptions;

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
