package org.ribasco.asyncgamequerylib.protocols.valve.csgo.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class CsgoAppServicesStatus {
    @SerializedName("SessionsLogon")
    private String sessionsLogonStatus;
    @SerializedName("SteamCommunity")
    private String steamCommunityStatus;
    @SerializedName("IEconItems")
    private String econItemsStatus;
    @SerializedName("Leaderboards")
    private String leaderboardsStatus;

    public String getSessionsLogonStatus() {
        return sessionsLogonStatus;
    }

    public void setSessionsLogonStatus(String sessionsLogonStatus) {
        this.sessionsLogonStatus = sessionsLogonStatus;
    }

    public String getSteamCommunityStatus() {
        return steamCommunityStatus;
    }

    public void setSteamCommunityStatus(String steamCommunityStatus) {
        this.steamCommunityStatus = steamCommunityStatus;
    }

    public String getEconItemsStatus() {
        return econItemsStatus;
    }

    public void setEconItemsStatus(String econItemsStatus) {
        this.econItemsStatus = econItemsStatus;
    }

    public String getLeaderboardsStatus() {
        return leaderboardsStatus;
    }

    public void setLeaderboardsStatus(String leaderboardsStatus) {
        this.leaderboardsStatus = leaderboardsStatus;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
