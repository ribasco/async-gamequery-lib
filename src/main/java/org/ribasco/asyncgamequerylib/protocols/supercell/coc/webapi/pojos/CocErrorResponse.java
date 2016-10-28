package org.ribasco.asyncgamequerylib.protocols.supercell.coc.webapi.pojos;

public class CocErrorResponse {
    private String reason;
    private String message;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
