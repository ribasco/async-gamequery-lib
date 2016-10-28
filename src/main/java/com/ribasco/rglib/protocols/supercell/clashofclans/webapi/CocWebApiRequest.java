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

package com.ribasco.rglib.protocols.supercell.clashofclans.webapi;

import com.ribasco.rglib.core.AbstractWebRequest;
import org.asynchttpclient.Request;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.uri.Uri;

/**
 * Created by raffy on 10/27/2016.
 */
public abstract class CocWebApiRequest extends AbstractWebRequest {

    private static final String URI_FORMAT = "https://api.clashofclans.com/v%d/%s%s";

    private int limit, before, after;
    private int apiVersion;
    private String apiInterface;
    private String apiMethod;

    public CocWebApiRequest(String apiInterface, String apiMethod, int apiVersion) {
        this(apiInterface, apiMethod, apiVersion, -1, -1, -1);
    }

    public CocWebApiRequest(String apiInterface, String apiMethod, int apiVersion, int limit, int before, int after) {
        this.apiInterface = apiInterface;
        this.apiVersion = apiVersion;
        this.apiMethod = apiMethod;
        this.limit = limit;
        this.before = before;
        this.after = after;
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

    @Override
    public Request getMessage() {
        //Retrieve our existing request builder
        RequestBuilder builder = getRequestBuilder();
        //Create our URI
        Uri steamUri = Uri.create(String.format(URI_FORMAT, apiVersion, apiInterface, apiMethod));
        //Pass the URI
        builder.setUri(steamUri);
        //Apply General Coc Params
        addParam("limit", this.limit);
        addParam("before", this.before);
        addParam("after", this.after);
        //Apply additional request parameters from the concrete class (if available)
        buildRequest(builder);
        //Let our super class build the request
        return super.getMessage();
    }
}
