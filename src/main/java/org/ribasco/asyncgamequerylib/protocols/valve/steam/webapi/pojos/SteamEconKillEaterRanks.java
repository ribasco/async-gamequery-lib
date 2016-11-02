package org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.pojos;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @see <a href="https://wiki.teamfortress.com/wiki/Talk:WebAPI/GetSchema">Talk:WebAPI/GetSchema</a>
 * @deprecated
 */
@Deprecated
public class SteamEconKillEaterRanks {
    private int level;
    private int requiredScore;
    private String name;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getRequiredScore() {
        return requiredScore;
    }

    public void setRequiredScore(int requiredScore) {
        this.requiredScore = requiredScore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
