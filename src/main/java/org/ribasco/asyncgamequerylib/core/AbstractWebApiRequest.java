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

import org.asynchttpclient.Request;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.uri.Uri;

public abstract class AbstractWebApiRequest extends AbstractWebRequest {
    private int apiVersion;
    private String apiInterface;
    private String apiMethod;
    private String uriFormat;

    public AbstractWebApiRequest(int apiVersion, String apiInterface, String apiMethod) {
        this.apiVersion = apiVersion;
        this.apiInterface = apiInterface;
        this.apiMethod = apiMethod;
    }

    public String getApiInterface() {
        return apiInterface;
    }

    public void setApiInterface(String apiInterface) {
        this.apiInterface = apiInterface;
    }

    public int getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(int apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getApiMethod() {
        return apiMethod;
    }

    public void setApiMethod(String apiMethod) {
        this.apiMethod = apiMethod;
    }

    public String getUriFormat() {
        return uriFormat;
    }

    public void setUriFormat(String uriFormat) {
        this.uriFormat = uriFormat;
    }

    @Override
    public Request getMessage() {
        //Retrieve our existing request builder
        RequestBuilder builder = getRequestBuilder();
        //Create our URI
        Uri steamUri = Uri.create(String.format(getUriFormat(), apiVersion, apiInterface, apiMethod));
        //Pass the URI
        builder.setUri(steamUri);
        //Apply additional request parameters from the concrete class (if available)
        buildRequest(builder);
        //Let our super class build the request
        return super.getMessage();
    }
}
