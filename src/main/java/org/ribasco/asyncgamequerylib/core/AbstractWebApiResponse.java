package org.ribasco.asyncgamequerylib.core;

import org.asynchttpclient.Response;

public class AbstractWebApiResponse<T> extends AbstractWebResponse {
    private T processedContent;

    public AbstractWebApiResponse(Response response) {
        super(response);
    }

    public T getProcessedContent() {
        return processedContent;
    }

    public <V extends T> void setProcessedContent(V processedContent) {
        this.processedContent = processedContent;
    }

    @Override
    public Response getMessage() {
        return super.getMessage();
    }
}
