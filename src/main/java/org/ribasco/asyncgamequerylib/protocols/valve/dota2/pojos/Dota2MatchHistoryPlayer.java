
package org.ribasco.asyncgamequerylib.protocols.valve.dota2.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Dota2MatchHistoryPlayer {

    @SerializedName("account_id")
    @Expose
    private long accountId;
    @SerializedName("player_slot")
    @Expose
    private int playerSlot;
    @SerializedName("hero_id")
    @Expose
    private int heroId;

    /**
     * @return The accountId
     */
    public long getAccountId() {
        return accountId;
    }

    /**
     * @param accountId The account_id
     */
    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    /**
     * @return The playerSlot
     */
    public int getPlayerSlot() {
        return playerSlot;
    }

    /**
     * @param playerSlot The player_slot
     */
    public void setPlayerSlot(int playerSlot) {
        this.playerSlot = playerSlot;
    }

    /**
     * @return The heroId
     */
    public int getHeroId() {
        return heroId;
    }

    /**
     * @param heroId The hero_id
     */
    public void setHeroId(int heroId) {
        this.heroId = heroId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

}
