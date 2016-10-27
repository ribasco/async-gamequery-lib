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

package com.ribasco.rglib.protocols.valve.steam.webapi.interfaces.user;

import com.ribasco.rglib.protocols.valve.steam.SteamApiConstants;
import com.ribasco.rglib.protocols.valve.steam.SteamWebApiRequest;
import org.apache.commons.lang3.StringUtils;
import org.asynchttpclient.RequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by raffy on 10/27/2016.
 */
public class GetPlayerBans extends SteamWebApiRequest {

    private static final Logger log = LoggerFactory.getLogger(GetPlayerBans.class);
    private Long[] steamIds;

    public GetPlayerBans(int apiVersion, List<Long> steamIds) {
        this(apiVersion, steamIds.toArray(new Long[0]));
    }

    public GetPlayerBans(int apiVersion, Long... steamIds) {
        super(SteamApiConstants.STEAM_USER, "GetPlayerBans", apiVersion);
        this.steamIds = steamIds;
    }

    @Override
    protected void buildRequest(RequestBuilder requestBuilder) {
        //List<String> strSteamIds = ListUtils.convertList(Arrays.asList(steamIds), l -> Long.toString(l));
        //log.info("Building Request for SteamIds: {}", StringUtils.join(strSteamIds, ","));
        addParam("steamids", StringUtils.join(this.steamIds, ","));
    }
}
