package org.ribasco.agql.protocols.valve.dota2.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Dota2LiveLeagueTeamInfo {
    @SerializedName("team_name")
    private String teamName;
    @SerializedName("team_id")
    private long teamId;
    @SerializedName("team_logo")
    private long teamLogo;
    private boolean complete;

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public long getTeamLogo() {
        return teamLogo;
    }

    public void setTeamLogo(long teamLogo) {
        this.teamLogo = teamLogo;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
