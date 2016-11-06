package org.ribasco.agql.protocols.valve.csgo.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class CsgoMatchmakingStatus {
    private String scheduler;
    @SerializedName("online_servers")
    private int onlineServerCount;
    @SerializedName("online_players")
    private int onlinePlayerCount;
    @SerializedName("searching_players")
    private int searchingPlayersCount;
    @SerializedName("search_seconds_avg")
    private int searchSecondsAvg;

    public String getScheduler() {
        return scheduler;
    }

    public void setScheduler(String scheduler) {
        this.scheduler = scheduler;
    }

    public int getOnlineServerCount() {
        return onlineServerCount;
    }

    public void setOnlineServerCount(int onlineServerCount) {
        this.onlineServerCount = onlineServerCount;
    }

    public int getOnlinePlayerCount() {
        return onlinePlayerCount;
    }

    public void setOnlinePlayerCount(int onlinePlayerCount) {
        this.onlinePlayerCount = onlinePlayerCount;
    }

    public int getSearchingPlayersCount() {
        return searchingPlayersCount;
    }

    public void setSearchingPlayersCount(int searchingPlayersCount) {
        this.searchingPlayersCount = searchingPlayersCount;
    }

    public int getSearchSecondsAvg() {
        return searchSecondsAvg;
    }

    public void setSearchSecondsAvg(int searchSecondsAvg) {
        this.searchSecondsAvg = searchSecondsAvg;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
