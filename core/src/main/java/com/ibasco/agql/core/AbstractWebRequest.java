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

import io.netty.handler.codec.http.HttpMethod;
import org.asynchttpclient.Request;
import org.asynchttpclient.RequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;

/**
 * <p>Abstract AbstractWebRequest class.</p>
 *
 * @author Rafael Luis Ibasco
 */
@SuppressWarnings("all")
abstract public class AbstractWebRequest extends AbstractRequest {

    private static final Logger log = LoggerFactory.getLogger(AbstractWebRequest.class);

    private RequestBuilder requestBuilder;

    /**
     * <p>Constructor for AbstractWebRequest.</p>
     */
    public AbstractWebRequest() {
        //super(null);
        requestBuilder = new RequestBuilder();
    }

    /**
     * <p>build.</p>
     *
     * @param requestBuilder
     *         a {@link org.asynchttpclient.RequestBuilder} object
     */
    protected void build(RequestBuilder requestBuilder) {
        //no implementation
    }

    /**
     * <p>baseUrl.</p>
     *
     * @param url
     *         a {@link java.lang.String} object
     */
    protected void baseUrl(String url) {
        request().setUrl(url);
    }

    /**
     * <p>header.</p>
     *
     * @param header
     *         a {@link java.lang.CharSequence} object
     * @param value
     *         a {@link java.lang.String} object
     */
    protected void header(CharSequence header, String value) {
        request().addHeader(header, value);
    }

    /**
     * <p>method.</p>
     *
     * @param method
     *         a {@link io.netty.handler.codec.http.HttpMethod} object
     */
    protected void method(HttpMethod method) {
        request().setMethod(method.name());
    }

    /**
     * <p>urlParam.</p>
     *
     * @param name
     *         a {@link java.lang.String} object
     * @param value
     *         a {@link java.lang.Object} object
     */
    protected void urlParam(String name, Object value) {
        RequestBuilder builder = request();
        if (value == null)
            return;
        if (value instanceof Collection<?>) {
            Collection<Object> values = (Collection<Object>) value;
            int idx = 0;
            for (Object v : values) {
                String paramName = String.format("%s[%d]", name, idx++);
                builder.addQueryParam(paramName, String.valueOf(v));
            }
        } else {
            builder.addQueryParam(name, String.valueOf(value));
        }
    }

    /**
     * <p>encode.</p>
     *
     * @param element
     *         a {@link java.lang.String} object
     *
     * @return a {@link java.lang.String} object
     */
    protected String encode(String element) {
        return encode(element, "UTF-8");
    }

    /**
     * <p>encode.</p>
     *
     * @param element
     *         a {@link java.lang.String} object
     * @param encoding
     *         a {@link java.lang.String} object
     *
     * @return a {@link java.lang.String} object
     */
    protected String encode(String element, String encoding) {
        try {
            return URLEncoder.encode(element, encoding);
        } catch (UnsupportedEncodingException ignored) {
        }
        return null;
    }

    /**
     * <p>request.</p>
     *
     * @return a {@link org.asynchttpclient.RequestBuilder} object
     */
    public final RequestBuilder request() {
        return requestBuilder;
    }

    //@Override

    /**
     * <p>getMessage.</p>
     *
     * @return a {@link org.asynchttpclient.Request} object
     */
    public Request getMessage() {
        build(requestBuilder);
        Request webRequest = requestBuilder.build();
        log.debug("Request URL: {}, PARAMS: {}", webRequest.getUrl(), webRequest.getQueryParams());
        return requestBuilder.build();
    }
}
