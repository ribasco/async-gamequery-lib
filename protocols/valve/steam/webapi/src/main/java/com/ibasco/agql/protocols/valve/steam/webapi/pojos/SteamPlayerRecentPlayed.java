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

package com.ibasco.agql.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a player's recently played game
 *
 * @author Rafael Luis Ibasco
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

    /**
     * <p>Getter for the field <code>appId</code>.</p>
     *
     * @return a int
     */
    public int getAppId() {
        return appId;
    }

    /**
     * <p>Setter for the field <code>appId</code>.</p>
     *
     * @param appId
     *         a int
     */
    public void setAppId(int appId) {
        this.appId = appId;
    }

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name
     *         a {@link java.lang.String} object
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>Getter for the field <code>totalPlaytimeIn2Weeks</code>.</p>
     *
     * @return a int
     */
    public int getTotalPlaytimeIn2Weeks() {
        return totalPlaytimeIn2Weeks;
    }

    /**
     * <p>Setter for the field <code>totalPlaytimeIn2Weeks</code>.</p>
     *
     * @param totalPlaytimeIn2Weeks
     *         a int
     */
    public void setTotalPlaytimeIn2Weeks(int totalPlaytimeIn2Weeks) {
        this.totalPlaytimeIn2Weeks = totalPlaytimeIn2Weeks;
    }

    /**
     * <p>Getter for the field <code>totalPlaytime</code>.</p>
     *
     * @return a int
     */
    public int getTotalPlaytime() {
        return totalPlaytime;
    }

    /**
     * <p>Setter for the field <code>totalPlaytime</code>.</p>
     *
     * @param totalPlaytime
     *         a int
     */
    public void setTotalPlaytime(int totalPlaytime) {
        this.totalPlaytime = totalPlaytime;
    }

    /**
     * <p>Getter for the field <code>imgIconUrl</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getImgIconUrl() {
        return imgIconUrl;
    }

    /**
     * <p>Setter for the field <code>imgIconUrl</code>.</p>
     *
     * @param imgIconUrl
     *         a {@link java.lang.String} object
     */
    public void setImgIconUrl(String imgIconUrl) {
        this.imgIconUrl = imgIconUrl;
    }

    /**
     * <p>Getter for the field <code>imgLogoUrl</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getImgLogoUrl() {
        return imgLogoUrl;
    }

    /**
     * <p>Setter for the field <code>imgLogoUrl</code>.</p>
     *
     * @param imgLogoUrl
     *         a {@link java.lang.String} object
     */
    public void setImgLogoUrl(String imgLogoUrl) {
        this.imgLogoUrl = imgLogoUrl;
    }
}
