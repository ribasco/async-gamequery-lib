package org.ribasco.asyncgamequerylib.protocols.valve.source.query.exceptions;

import org.ribasco.asyncgamequerylib.core.exceptions.AsyncGameLibCheckedException;

public class SourceRconExecutionException extends AsyncGameLibCheckedException {
    public SourceRconExecutionException() {
    }

    public SourceRconExecutionException(String message) {
        super(message);
    }

    public SourceRconExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public SourceRconExecutionException(Throwable cause) {
        super(cause);
    }

    public SourceRconExecutionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
