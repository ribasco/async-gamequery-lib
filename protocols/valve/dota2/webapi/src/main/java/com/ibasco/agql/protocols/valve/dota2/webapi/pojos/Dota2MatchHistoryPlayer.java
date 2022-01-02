/*
 * Copyright 2018-2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.protocols.valve.dota2.webapi.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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
     * @param accountId
     *         The account_id
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
     * @param playerSlot
     *         The player_slot
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
     * @param heroId
     *         The hero_id
     */
    public void setHeroId(int heroId) {
        this.heroId = heroId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

}
