package org.ribasco.asyncgamequerylib.core.exceptions;

public class UnknownWebException extends WebException {
    public UnknownWebException() {
        super();
    }

    public UnknownWebException(String message) {
        super(message);
    }

    public UnknownWebException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownWebException(Throwable cause) {
        super(cause);
    }

    public UnknownWebException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
