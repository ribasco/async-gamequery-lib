package org.ribasco.agql.protocols.supercell.coc.webapi.exceptions;

import org.ribasco.agql.core.exceptions.BadRequestException;

public class CocIncorrectParametersException extends BadRequestException {
    public CocIncorrectParametersException() {
        super();
    }

    public CocIncorrectParametersException(String message) {
        super(message);
    }

    public CocIncorrectParametersException(String message, Throwable cause) {
        super(message, cause);
    }

    public CocIncorrectParametersException(Throwable cause) {
        super(cause);
    }

    public CocIncorrectParametersException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
