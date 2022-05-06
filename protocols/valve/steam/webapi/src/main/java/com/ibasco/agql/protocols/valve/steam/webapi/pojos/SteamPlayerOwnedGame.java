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
 * Created by raffy on 10/27/2016.
 *
 * @author Rafael Luis Ibasco
 */
public class SteamPlayerOwnedGame {

    @SerializedName("appid")
    private int appId;

    private String name;

    @SerializedName("playtime_forever")
    private int totalPlaytime;

    @SerializedName("img_icon_url")
    private String iconImgUrl;

    @SerializedName("img_logo_url")
    private String logoImgUrl;

    @SerializedName("has_community_visible_stats")
    private boolean hasCommunityVisibleStats;

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
     * <p>Getter for the field <code>iconImgUrl</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getIconImgUrl() {
        return iconImgUrl;
    }

    /**
     * <p>Setter for the field <code>iconImgUrl</code>.</p>
     *
     * @param iconImgUrl
     *         a {@link java.lang.String} object
     */
    public void setIconImgUrl(String iconImgUrl) {
        this.iconImgUrl = iconImgUrl;
    }

    /**
     * <p>Getter for the field <code>logoImgUrl</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getLogoImgUrl() {
        return logoImgUrl;
    }

    /**
     * <p>Setter for the field <code>logoImgUrl</code>.</p>
     *
     * @param logoImgUrl
     *         a {@link java.lang.String} object
     */
    public void setLogoImgUrl(String logoImgUrl) {
        this.logoImgUrl = logoImgUrl;
    }

    /**
     * <p>isHasCommunityVisibleStats.</p>
     *
     * @return a boolean
     */
    public boolean isHasCommunityVisibleStats() {
        return hasCommunityVisibleStats;
    }

    /**
     * <p>Setter for the field <code>hasCommunityVisibleStats</code>.</p>
     *
     * @param hasCommunityVisibleStats
     *         a boolean
     */
    public void setHasCommunityVisibleStats(boolean hasCommunityVisibleStats) {
        this.hasCommunityVisibleStats = hasCommunityVisibleStats;
    }
}
