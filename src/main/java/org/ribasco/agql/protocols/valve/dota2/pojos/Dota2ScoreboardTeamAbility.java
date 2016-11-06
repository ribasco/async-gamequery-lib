
package org.ribasco.agql.protocols.valve.dota2.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Dota2ScoreboardTeamAbility {

    @SerializedName("ability_id")
    @Expose
    private int abilityId;
    @SerializedName("ability_level")
    @Expose
    private int abilityLevel;

    /**
     * @return The abilityId
     */
    public int getAbilityId() {
        return abilityId;
    }

    /**
     * @param abilityId The ability_id
     */
    public void setAbilityId(int abilityId) {
        this.abilityId = abilityId;
    }

    /**
     * @return The abilityLevel
     */
    public int getAbilityLevel() {
        return abilityLevel;
    }

    /**
     * @param abilityLevel The ability_level
     */
    public void setAbilityLevel(int abilityLevel) {
        this.abilityLevel = abilityLevel;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
