/*
 * Copyright (c) 2018-2022 Asynchronous Game Query Library
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

import io.netty.handler.codec.http.HttpMethod;
import org.asynchttpclient.Request;
import org.asynchttpclient.RequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@SuppressWarnings("all")
abstract public class AbstractWebRequest extends AbstractRequest {

    private static final Logger log = LoggerFactory.getLogger(AbstractWebRequest.class);

    private RequestBuilder requestBuilder;

    public AbstractWebRequest() {
        //super(null);
        requestBuilder = new RequestBuilder();
    }

    protected void build(RequestBuilder requestBuilder) {
        //no implementation
    }

    protected void baseUrl(String url) {
        request().setUrl(url);
    }

    protected void header(CharSequence header, String value) {
        request().addHeader(header, value);
    }

    protected void method(HttpMethod method) {
        request().setMethod(method.name());
    }

    protected void urlParam(String name, Object value) {
        RequestBuilder builder = request();
        if (value == null)
            return;
        builder.addQueryParam(name, String.valueOf(value));
    }

    protected String encode(String element) {
        return encode(element, "UTF-8");
    }

    protected String encode(String element, String encoding) {
        try {
            return URLEncoder.encode(element, encoding);
        } catch (UnsupportedEncodingException ignored) {
        }
        return null;
    }

    public final RequestBuilder request() {
        return requestBuilder;
    }

    //@Override
    public Request getMessage() {
        build(requestBuilder);
        Request webRequest = requestBuilder.build();
        log.debug("Request URL: {}, PARAMS: {}", webRequest.getUrl(), webRequest.getQueryParams());
        return requestBuilder.build();
    }
}
