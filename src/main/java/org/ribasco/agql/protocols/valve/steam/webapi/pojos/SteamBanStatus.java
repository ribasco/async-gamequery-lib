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

package org.ribasco.agql.protocols.valve.steam.webapi.pojos;

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
