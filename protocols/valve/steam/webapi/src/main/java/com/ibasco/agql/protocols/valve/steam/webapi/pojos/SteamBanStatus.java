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
public class SteamBanStatus {

    @SerializedName("SteamId")
    private String steamId;

    @SerializedName("CommunityBanned")
    private boolean communityBanned;

    @SerializedName("VACBanned")
    private boolean vacBanned;

    @SerializedName("NumberOfVACBans")
    private int totalVacBansOnRecord;

    @SerializedName("DaysSinceLastBan")
    private int totalDaysSinceLastBan;

    @SerializedName("NumberOfGameBans")
    private int totalGameBans;

    @SerializedName("EconomyBan")
    private String economyBan;

    /**
     * <p>getSteamIdAsLong.</p>
     *
     * @return a long
     */
    public long getSteamIdAsLong() {
        return Long.valueOf(steamId);
    }

    /**
     * <p>Getter for the field <code>steamId</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getSteamId() {
        return steamId;
    }

    /**
     * <p>Setter for the field <code>steamId</code>.</p>
     *
     * @param steamId
     *         a {@link java.lang.String} object
     */
    public void setSteamId(String steamId) {
        this.steamId = steamId;
    }

    /**
     * <p>isCommunityBanned.</p>
     *
     * @return a boolean
     */
    public boolean isCommunityBanned() {
        return communityBanned;
    }

    /**
     * <p>Setter for the field <code>communityBanned</code>.</p>
     *
     * @param communityBanned
     *         a boolean
     */
    public void setCommunityBanned(boolean communityBanned) {
        this.communityBanned = communityBanned;
    }

    /**
     * <p>isVacBanned.</p>
     *
     * @return a boolean
     */
    public boolean isVacBanned() {
        return vacBanned;
    }

    /**
     * <p>Setter for the field <code>vacBanned</code>.</p>
     *
     * @param vacBanned
     *         a boolean
     */
    public void setVacBanned(boolean vacBanned) {
        this.vacBanned = vacBanned;
    }

    /**
     * <p>Getter for the field <code>totalVacBansOnRecord</code>.</p>
     *
     * @return a int
     */
    public int getTotalVacBansOnRecord() {
        return totalVacBansOnRecord;
    }

    /**
     * <p>Setter for the field <code>totalVacBansOnRecord</code>.</p>
     *
     * @param totalVacBansOnRecord
     *         a int
     */
    public void setTotalVacBansOnRecord(int totalVacBansOnRecord) {
        this.totalVacBansOnRecord = totalVacBansOnRecord;
    }

    /**
     * <p>Getter for the field <code>totalDaysSinceLastBan</code>.</p>
     *
     * @return a int
     */
    public int getTotalDaysSinceLastBan() {
        return totalDaysSinceLastBan;
    }

    /**
     * <p>Setter for the field <code>totalDaysSinceLastBan</code>.</p>
     *
     * @param totalDaysSinceLastBan
     *         a int
     */
    public void setTotalDaysSinceLastBan(int totalDaysSinceLastBan) {
        this.totalDaysSinceLastBan = totalDaysSinceLastBan;
    }

    /**
     * <p>Getter for the field <code>totalGameBans</code>.</p>
     *
     * @return a int
     */
    public int getTotalGameBans() {
        return totalGameBans;
    }

    /**
     * <p>Setter for the field <code>totalGameBans</code>.</p>
     *
     * @param totalGameBans
     *         a int
     */
    public void setTotalGameBans(int totalGameBans) {
        this.totalGameBans = totalGameBans;
    }

    /**
     * <p>Getter for the field <code>economyBan</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getEconomyBan() {
        return economyBan;
    }

    /**
     * <p>Setter for the field <code>economyBan</code>.</p>
     *
     * @param economyBan
     *         a {@link java.lang.String} object
     */
    public void setEconomyBan(String economyBan) {
        this.economyBan = economyBan;
    }
}
