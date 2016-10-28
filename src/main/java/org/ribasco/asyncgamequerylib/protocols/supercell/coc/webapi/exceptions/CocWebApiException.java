package org.ribasco.asyncgamequerylib.protocols.supercell.coc.webapi.exceptions;

import org.ribasco.asyncgamequerylib.core.exceptions.WebException;

public class CocWebApiException extends WebException {
    public CocWebApiException() {
        super();
    }

    public CocWebApiException(String message) {
        super(message);
    }

    public CocWebApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public CocWebApiException(Throwable cause) {
        super(cause);
    }

    public CocWebApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
