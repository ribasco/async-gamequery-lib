package org.ribasco.agql.core;

import org.asynchttpclient.Response;

public class AbstractWebApiResponse<T> extends AbstractWebResponse {
    private T processedContent;

    public AbstractWebApiResponse(Response response) {
        super(response);
    }

    /**
     * Returns the parsed content of the response body. It can be in any form (e.g. json, xml, vdf etc)
     *
     * @return
     */
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
