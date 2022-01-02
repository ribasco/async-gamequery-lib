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
 * Created by raffy on 10/27/2016.
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

    public long getSteamIdAsLong() {
        return Long.valueOf(steamId);
    }

    public String getSteamId() {
        return steamId;
    }

    public void setSteamId(String steamId) {
        this.steamId = steamId;
    }

    public boolean isCommunityBanned() {
        return communityBanned;
    }

    public void setCommunityBanned(boolean communityBanned) {
        this.communityBanned = communityBanned;
    }

    public boolean isVacBanned() {
        return vacBanned;
    }

    public void setVacBanned(boolean vacBanned) {
        this.vacBanned = vacBanned;
    }

    public int getTotalVacBansOnRecord() {
        return totalVacBansOnRecord;
    }

    public void setTotalVacBansOnRecord(int totalVacBansOnRecord) {
        this.totalVacBansOnRecord = totalVacBansOnRecord;
    }

    public int getTotalDaysSinceLastBan() {
        return totalDaysSinceLastBan;
    }

    public void setTotalDaysSinceLastBan(int totalDaysSinceLastBan) {
        this.totalDaysSinceLastBan = totalDaysSinceLastBan;
    }

    public int getTotalGameBans() {
        return totalGameBans;
    }

    public void setTotalGameBans(int totalGameBans) {
        this.totalGameBans = totalGameBans;
    }

    public String getEconomyBan() {
        return economyBan;
    }

    public void setEconomyBan(String economyBan) {
        this.economyBan = economyBan;
    }
}
