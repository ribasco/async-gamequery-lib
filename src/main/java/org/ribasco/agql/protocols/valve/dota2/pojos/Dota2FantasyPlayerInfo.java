package org.ribasco.agql.protocols.valve.dota2.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Dota2FantasyPlayerInfo {
    @SerializedName("Name")
    private String name;
    @SerializedName("TeamName")
    private String teamName;
    @SerializedName("TeamTag")
    private String teamTag;
    @SerializedName("Sponsor")
    private String sponsor;
    @SerializedName("FantasyRole")
    private int fantasyRole;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamTag() {
        return teamTag;
    }

    public void setTeamTag(String teamTag) {
        this.teamTag = teamTag;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public int getFantasyRole() {
        return fantasyRole;
    }

    public void setFantasyRole(int fantasyRole) {
        this.fantasyRole = fantasyRole;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
