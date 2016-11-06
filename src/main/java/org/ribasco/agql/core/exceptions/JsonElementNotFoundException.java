package org.ribasco.agql.core.exceptions;

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
