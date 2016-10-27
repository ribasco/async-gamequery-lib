/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Luis Ibasco
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **************************************************************************************************/

package com.ribasco.rglib.core;

import org.asynchttpclient.Request;
import org.asynchttpclient.RequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by raffy on 9/15/2016.
 */
public abstract class AbstractWebRequest extends AbstractRequest<Request> {

    private static final Logger log = LoggerFactory.getLogger(AbstractWebRequest.class);

    private RequestBuilder requestBuilder;

    public AbstractWebRequest() {
        super(null);
        requestBuilder = new RequestBuilder();
    }

    public final RequestBuilder getRequestBuilder() {
        return requestBuilder;
    }

    protected abstract void buildRequest(RequestBuilder requestBuilder);

    @Override
    public Request getMessage() {
        Request webRequest = requestBuilder.build();
        log.debug("Request URL: {}, PARAMS: {}", webRequest.getUrl(), webRequest.getQueryParams());
        return requestBuilder.build();
    }
}
