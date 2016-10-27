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

package com.ribasco.rglib.protocols.valve.steam.webapi.interfaces.apps;

import com.ribasco.rglib.protocols.valve.steam.SteamApiConstants;
import com.ribasco.rglib.protocols.valve.steam.SteamWebApiRequest;
import org.asynchttpclient.RequestBuilder;

/**
 * Created by raffy on 10/25/2016.
 */
public class UpToDateCheck extends SteamWebApiRequest {

    private int appId;
    private int serverVersion;

    public UpToDateCheck(int version, int serverVersion, int appId) {
        super(SteamApiConstants.STEAM_APPS, "UpToDateCheck", version);
        this.appId = appId;
        this.serverVersion = serverVersion;
    }

    @Override
    protected void buildRequest(RequestBuilder requestBuilder) {
        requestBuilder.addQueryParam("appid", String.valueOf(getAppId()));
        requestBuilder.addQueryParam("version", String.valueOf(getServerVersion()));
    }

    public int getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(int serverVersion) {
        this.serverVersion = serverVersion;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }
}
