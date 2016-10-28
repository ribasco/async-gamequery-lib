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

package org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.interfaces.economy;

import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.interfaces.SteamEconomyRequest;

import java.util.List;

public class GetAssetClassInfo extends SteamEconomyRequest {
    public GetAssetClassInfo(int apiVersion, int appId, List<Long> classIds) {
        this(apiVersion, appId, null, classIds.toArray(new Long[0]));
    }

    public GetAssetClassInfo(int apiVersion, int appId, String language, Long... classIds) {
        super("GetAssetClassInfo", apiVersion);
        param("appid", appId);
        //Add class id params
        if (classIds != null) {
            param("class_count", classIds.length);
            param("language", language);
            for (int i = 0; i < classIds.length; i++)
                param(String.format("classid%d", i), classIds[i]);
        }
    }
}
