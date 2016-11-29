package com.ibasco.agql.core.exceptions;

import com.ibasco.agql.core.AbstractPacket;

public class NoResponseFoundForPacket extends ResponseProcessingException {

    private AbstractPacket responsePacket;

    public NoResponseFoundForPacket() {
    }

    public NoResponseFoundForPacket(String message, AbstractPacket packet) {
        super(message);
        this.responsePacket = packet;
    }

    public NoResponseFoundForPacket(String message, Throwable cause) {
        super(message, cause);
    }

    public NoResponseFoundForPacket(Throwable cause) {
        super(cause);
    }

    public NoResponseFoundForPacket(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public AbstractPacket getResponsePacket() {
        return responsePacket;
    }

    public void setResponsePacket(AbstractPacket responsePacket) {
        this.responsePacket = responsePacket;
    }
}
