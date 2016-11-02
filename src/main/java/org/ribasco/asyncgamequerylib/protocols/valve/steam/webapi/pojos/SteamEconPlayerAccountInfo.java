package org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SteamEconPlayerAccountInfo {
    @SerializedName("steamid")
    private long steamId;
    @SerializedName("personaname")
    private String personaName;

    public long getSteamId() {
        return steamId;
    }

    public void setSteamId(long steamId) {
        this.steamId = steamId;
    }

    public String getPersonaName() {
        return personaName;
    }

    public void setPersonaName(String personaName) {
        this.personaName = personaName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("steamId", getSteamId())
                .append("personaName", getPersonaName())
                .toString();
    }
}
