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

package com.ibasco.agql.protocols.valve.csgo.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>CsgoAppServicesStatus class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class CsgoAppServicesStatus {
    @SerializedName("SessionsLogon")
    private String sessionsLogonStatus;
    @SerializedName("SteamCommunity")
    private String steamCommunityStatus;
    @SerializedName("IEconItems")
    private String econItemsStatus;
    @SerializedName("Leaderboards")
    private String leaderboardsStatus;

    /**
     * <p>Getter for the field <code>sessionsLogonStatus</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getSessionsLogonStatus() {
        return sessionsLogonStatus;
    }

    /**
     * <p>Setter for the field <code>sessionsLogonStatus</code>.</p>
     *
     * @param sessionsLogonStatus a {@link java.lang.String} object
     */
    public void setSessionsLogonStatus(String sessionsLogonStatus) {
        this.sessionsLogonStatus = sessionsLogonStatus;
    }

    /**
     * <p>Getter for the field <code>steamCommunityStatus</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getSteamCommunityStatus() {
        return steamCommunityStatus;
    }

    /**
     * <p>Setter for the field <code>steamCommunityStatus</code>.</p>
     *
     * @param steamCommunityStatus a {@link java.lang.String} object
     */
    public void setSteamCommunityStatus(String steamCommunityStatus) {
        this.steamCommunityStatus = steamCommunityStatus;
    }

    /**
     * <p>Getter for the field <code>econItemsStatus</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getEconItemsStatus() {
        return econItemsStatus;
    }

    /**
     * <p>Setter for the field <code>econItemsStatus</code>.</p>
     *
     * @param econItemsStatus a {@link java.lang.String} object
     */
    public void setEconItemsStatus(String econItemsStatus) {
        this.econItemsStatus = econItemsStatus;
    }

    /**
     * <p>Getter for the field <code>leaderboardsStatus</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getLeaderboardsStatus() {
        return leaderboardsStatus;
    }

    /**
     * <p>Setter for the field <code>leaderboardsStatus</code>.</p>
     *
     * @param leaderboardsStatus a {@link java.lang.String} object
     */
    public void setLeaderboardsStatus(String leaderboardsStatus) {
        this.leaderboardsStatus = leaderboardsStatus;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
