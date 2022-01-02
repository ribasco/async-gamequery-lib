/*
 * Copyright 2018-2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a player's recently played game
 */
public class SteamPlayerRecentPlayed {
    @SerializedName("appid")
    private int appId;
    private String name;
    @SerializedName("playtime_2weeks")
    private int totalPlaytimeIn2Weeks;
    @SerializedName("playtime_forever")
    private int totalPlaytime;
    @SerializedName("img_icon_url")
    private String imgIconUrl;
    @SerializedName("img_logo_url")
    private String imgLogoUrl;

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalPlaytimeIn2Weeks() {
        return totalPlaytimeIn2Weeks;
    }

    public void setTotalPlaytimeIn2Weeks(int totalPlaytimeIn2Weeks) {
        this.totalPlaytimeIn2Weeks = totalPlaytimeIn2Weeks;
    }

    public int getTotalPlaytime() {
        return totalPlaytime;
    }

    public void setTotalPlaytime(int totalPlaytime) {
        this.totalPlaytime = totalPlaytime;
    }

    public String getImgIconUrl() {
        return imgIconUrl;
    }

    public void setImgIconUrl(String imgIconUrl) {
        this.imgIconUrl = imgIconUrl;
    }

    public String getImgLogoUrl() {
        return imgLogoUrl;
    }

    public void setImgLogoUrl(String imgLogoUrl) {
        this.imgLogoUrl = imgLogoUrl;
    }
}
