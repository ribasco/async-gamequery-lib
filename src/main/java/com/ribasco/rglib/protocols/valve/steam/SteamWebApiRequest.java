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

package com.ribasco.rglib.protocols.valve.steam;

import com.ribasco.rglib.core.AbstractWebRequest;
import org.apache.commons.lang3.math.NumberUtils;
import org.asynchttpclient.Request;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.uri.Uri;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a Steam API Request
 */
public abstract class SteamWebApiRequest extends AbstractWebRequest {

    private static final Logger log = LoggerFactory.getLogger(SteamWebApiRequest.class);

    private static final String URI_FORMAT = "https://api.steampowered.com/%s/%s/v%d";

    private int apiVersion;
    private String apiInterface;
    private String apiMethod;

    public SteamWebApiRequest(String apiInterface, String apiMethod, int apiVersion) {
        this.apiInterface = apiInterface;
        this.apiVersion = apiVersion;
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

    protected void addParam(String name, Object value) {
        RequestBuilder builder = getRequestBuilder();
        String strValue = String.valueOf(value);
        if (NumberUtils.isNumber(strValue)) {
            Double nVal = Double.valueOf(strValue);
            if (nVal > 0)
                builder.addQueryParam(name, String.valueOf(value));
            return;
        }
        builder.addQueryParam(name, String.valueOf(value));
    }

    @Override
    public Request getMessage() {
        //Retrieve our existing request builder
        RequestBuilder builder = getRequestBuilder();
        //Create our URI
        Uri steamUri = Uri.create(String.format(URI_FORMAT, apiInterface, apiMethod, apiVersion));
        log.debug("Request URI : {}", steamUri);
        //Pass the URI
        builder.setUri(steamUri);
        //Apply additional request parameters from the concrete class (if available)
        buildRequest(builder);
        //Let our super class build the request
        return super.getMessage();
    }
}
