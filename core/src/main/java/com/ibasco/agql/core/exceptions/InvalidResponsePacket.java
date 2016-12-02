package com.ibasco.agql.core.exceptions;

public class InvalidResponsePacket extends ResponseProcessingException {
    public InvalidResponsePacket() {
        super();
    }

    public InvalidResponsePacket(String message) {
        super(message);
    }

    public InvalidResponsePacket(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidResponsePacket(Throwable cause) {
        super(cause);
    }

    public InvalidResponsePacket(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
