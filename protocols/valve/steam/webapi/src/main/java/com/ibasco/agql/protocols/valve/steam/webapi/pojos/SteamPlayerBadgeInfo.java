/*
 * Copyright (c) 2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by raffy on 10/27/2016.
 *
 * @author Rafael Luis Ibasco
 */
public class SteamPlayerBadgeInfo {

    @SerializedName("badges")
    private List<SteamPlayerBadge> playerBadges = new ArrayList<>();

    @SerializedName("player_xp")
    private int playerXp;

    @SerializedName("player_level")
    private int playerLevel;

    @SerializedName("player_xp_needed_to_level_up")
    private int xpNeededToLevelUp;

    @SerializedName("player_xp_needed_current_level")
    private int xpNeededCurrentLevel;

    /**
     * <p>Getter for the field <code>playerBadges</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<SteamPlayerBadge> getPlayerBadges() {
        return playerBadges;
    }

    /**
     * <p>Setter for the field <code>playerBadges</code>.</p>
     *
     * @param playerBadges
     *         a {@link java.util.List} object
     */
    public void setPlayerBadges(List<SteamPlayerBadge> playerBadges) {
        this.playerBadges = playerBadges;
    }

    /**
     * <p>Getter for the field <code>playerXp</code>.</p>
     *
     * @return a int
     */
    public int getPlayerXp() {
        return playerXp;
    }

    /**
     * <p>Setter for the field <code>playerXp</code>.</p>
     *
     * @param playerXp
     *         a int
     */
    public void setPlayerXp(int playerXp) {
        this.playerXp = playerXp;
    }

    /**
     * <p>Getter for the field <code>playerLevel</code>.</p>
     *
     * @return a int
     */
    public int getPlayerLevel() {
        return playerLevel;
    }

    /**
     * <p>Setter for the field <code>playerLevel</code>.</p>
     *
     * @param playerLevel
     *         a int
     */
    public void setPlayerLevel(int playerLevel) {
        this.playerLevel = playerLevel;
    }

    /**
     * <p>Getter for the field <code>xpNeededToLevelUp</code>.</p>
     *
     * @return a int
     */
    public int getXpNeededToLevelUp() {
        return xpNeededToLevelUp;
    }

    /**
     * <p>Setter for the field <code>xpNeededToLevelUp</code>.</p>
     *
     * @param xpNeededToLevelUp
     *         a int
     */
    public void setXpNeededToLevelUp(int xpNeededToLevelUp) {
        this.xpNeededToLevelUp = xpNeededToLevelUp;
    }

    /**
     * <p>Getter for the field <code>xpNeededCurrentLevel</code>.</p>
     *
     * @return a int
     */
    public int getXpNeededCurrentLevel() {
        return xpNeededCurrentLevel;
    }

    /**
     * <p>Setter for the field <code>xpNeededCurrentLevel</code>.</p>
     *
     * @param xpNeededCurrentLevel
     *         a int
     */
    public void setXpNeededCurrentLevel(int xpNeededCurrentLevel) {
        this.xpNeededCurrentLevel = xpNeededCurrentLevel;
    }
}
