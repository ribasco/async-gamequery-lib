
package org.ribasco.asyncgamequerylib.protocols.valve.dota2.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Dota2TopLiveGamePlayer {

    @SerializedName("account_id")
    private int accountId;
    @SerializedName("hero_id")
    private int heroId;

    /**
     * @return The accountId
     */
    public int getAccountId() {
        return accountId;
    }

    /**
     * @param accountId The account_id
     */
    public void setAccountId(int accountId) {
        this.accountId = accountId;
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
