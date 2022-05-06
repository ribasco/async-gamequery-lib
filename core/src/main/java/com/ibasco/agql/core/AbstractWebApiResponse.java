/*
 * Copyright (c) 2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.core;

import org.asynchttpclient.Response;

/**
 * <p>AbstractWebApiResponse class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class AbstractWebApiResponse<T> extends AbstractWebResponse {

    private T processedContent;

    /**
     * <p>Constructor for AbstractWebApiResponse.</p>
     *
     * @param response
     *         a {@link org.asynchttpclient.Response} object
     */
    public AbstractWebApiResponse(Response response) {
        super(response);
    }

    /**
     * Returns the parsed content of the response body. It can be in any form (e.g. json, xml, vdf etc)
     *
     * @return The processed body content
     */
    public T getProcessedContent() {
        return processedContent;
    }

    /**
     * <p>Setter for the field <code>processedContent</code>.</p>
     *
     * @param processedContent
     *         a V object
     * @param <V>
     *         a V class
     */
    public <V extends T> void setProcessedContent(V processedContent) {
        this.processedContent = processedContent;
    }

    /** {@inheritDoc} */
    @Override
    public Response getMessage() {
        return super.getMessage();
    }
}
