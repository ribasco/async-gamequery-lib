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

package org.ribasco.asyncgamequerylib.core;

import io.netty.handler.codec.http.HttpMethod;
import org.apache.commons.lang3.math.NumberUtils;
import org.asynchttpclient.Request;
import org.asynchttpclient.RequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@SuppressWarnings("all")
abstract public class AbstractWebRequest extends AbstractRequest<Request> {

    private static final Logger log = LoggerFactory.getLogger(AbstractWebRequest.class);

    private RequestBuilder requestBuilder;

    public AbstractWebRequest() {
        super(null);
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

    protected void param(String name, Object value) {
        RequestBuilder builder = request();
        if (value == null)
            return;
        String strValue = String.valueOf(value);
        if (NumberUtils.isNumber(strValue)) {
            Double nVal = Double.valueOf(strValue);
            if (nVal > 0)
                builder.addQueryParam(name, String.valueOf(value));
            return;
        }
        builder.addQueryParam(name, String.valueOf(value));
    }

    protected static String encode(String element) {
        return encode(element, "UTF-8");
    }

    protected static String encode(String element, String encoding) {
        try {
            return URLEncoder.encode(element, encoding);
        } catch (UnsupportedEncodingException ignored) {
        }
        return null;
    }

    public final RequestBuilder request() {
        return requestBuilder;
    }

    @Override
    public Request getMessage() {
        build(requestBuilder);
        Request webRequest = requestBuilder.build();
        log.debug("Request URL: {}, PARAMS: {}", webRequest.getUrl(), webRequest.getQueryParams());
        return requestBuilder.build();
    }
}
