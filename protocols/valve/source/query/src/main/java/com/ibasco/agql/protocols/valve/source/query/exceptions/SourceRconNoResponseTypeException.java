package com.ibasco.agql.protocols.valve.source.query.exceptions;

import com.ibasco.agql.core.exceptions.ResponseProcessingException;

/**
 * Created by raffy on 12/5/16.
 */
public class SourceRconNoResponseTypeException extends ResponseProcessingException {
    public SourceRconNoResponseTypeException() {
    }

    public SourceRconNoResponseTypeException(String message) {
        super(message);
    }

    public SourceRconNoResponseTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SourceRconNoResponseTypeException(Throwable cause) {
        super(cause);
    }

    public SourceRconNoResponseTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
