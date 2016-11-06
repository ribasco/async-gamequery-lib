package org.ribasco.agql.protocols.valve.dota2.webapi.exceptions;

import org.ribasco.agql.core.exceptions.WebException;

public class Dota2WebException extends WebException {
    public Dota2WebException() {
        super();
    }

    public Dota2WebException(String message) {
        super(message);
    }

    public Dota2WebException(String message, Throwable cause) {
        super(message, cause);
    }

    public Dota2WebException(Throwable cause) {
        super(cause);
    }

    public Dota2WebException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
