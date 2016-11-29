package com.ibasco.agql.core.exceptions;

public class ResponseProcessingException extends AsyncGameLibUncheckedException {
    public ResponseProcessingException() {
    }

    public ResponseProcessingException(String message) {
        super(message);
    }

    public ResponseProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResponseProcessingException(Throwable cause) {
        super(cause);
    }

    public ResponseProcessingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
