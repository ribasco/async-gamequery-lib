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

package com.ribasco.rglib.protocols.valve.steam.api.news;

import com.ribasco.rglib.protocols.valve.steam.SteamApiConstants;
import com.ribasco.rglib.protocols.valve.steam.SteamWebApiRequest;
import org.apache.commons.lang3.StringUtils;
import org.asynchttpclient.RequestBuilder;

/**
 * Created by raffy on 10/25/2016.
 */
public class GetNewsForApp extends SteamWebApiRequest {

    private int appId;
    private int maxLength = -1;
    private int endDate = -1;
    private int count = -1;
    private String feeds = null;

    public GetNewsForApp(int apiVersion, int appId) {
        super(SteamApiConstants.STEAM_NEWS, "GetNewsForApp", apiVersion);
        this.appId = appId;
    }

    @Override
    protected void buildRequest(RequestBuilder requestBuilder) {
        requestBuilder.addQueryParam("appid", String.valueOf(appId));
        if (maxLength > 0)
            requestBuilder.addQueryParam("maxlength", String.valueOf(maxLength));
        if (endDate > 0)
            requestBuilder.addQueryParam("enddate", String.valueOf(endDate));
        if (count > 0)
            requestBuilder.addQueryParam("count", String.valueOf(count));
        if (!StringUtils.isEmpty(feeds))
            requestBuilder.addQueryParam("feeds", feeds);
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public int getEndDate() {
        return endDate;
    }

    public void setEndDate(int endDate) {
        this.endDate = endDate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getFeeds() {
        return feeds;
    }

    public void setFeeds(String feeds) {
        this.feeds = feeds;
    }
}
