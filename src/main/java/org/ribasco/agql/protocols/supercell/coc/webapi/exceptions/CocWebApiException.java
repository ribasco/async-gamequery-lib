package org.ribasco.agql.protocols.supercell.coc.webapi.exceptions;

import org.ribasco.agql.core.exceptions.WebException;

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
