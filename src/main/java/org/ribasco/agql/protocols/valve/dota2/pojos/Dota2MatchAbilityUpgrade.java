
package org.ribasco.agql.protocols.valve.dota2.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Dota2MatchAbilityUpgrade {

    @SerializedName("ability")
    private int ability;
    @SerializedName("time")
    private int time;
    @SerializedName("level")
    private int level;

    /**
     * @return The ability
     */
    public int getAbility() {
        return ability;
    }

    /**
     * @param ability The ability
     */
    public void setAbility(int ability) {
        this.ability = ability;
    }

    /**
     * @return The time
     */
    public int getTime() {
        return time;
    }

    /**
     * @param time The time
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * @return The level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @param level The level
     */
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

}
