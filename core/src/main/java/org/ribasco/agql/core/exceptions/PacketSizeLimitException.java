package org.ribasco.agql.core.exceptions;

public class PacketSizeLimitException extends AsyncGameLibUncheckedException {
    public PacketSizeLimitException() {
        super();
    }

    public PacketSizeLimitException(String message) {
        super(message);
    }

    public PacketSizeLimitException(String message, Throwable cause) {
        super(message, cause);
    }

    public PacketSizeLimitException(Throwable cause) {
        super(cause);
    }

    public PacketSizeLimitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
