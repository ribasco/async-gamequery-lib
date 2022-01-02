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

package com.ibasco.agql.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raffy on 10/27/2016.
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

    public List<SteamPlayerBadge> getPlayerBadges() {
        return playerBadges;
    }

    public void setPlayerBadges(List<SteamPlayerBadge> playerBadges) {
        this.playerBadges = playerBadges;
    }

    public int getPlayerXp() {
        return playerXp;
    }

    public void setPlayerXp(int playerXp) {
        this.playerXp = playerXp;
    }

    public int getPlayerLevel() {
        return playerLevel;
    }

    public void setPlayerLevel(int playerLevel) {
        this.playerLevel = playerLevel;
    }

    public int getXpNeededToLevelUp() {
        return xpNeededToLevelUp;
    }

    public void setXpNeededToLevelUp(int xpNeededToLevelUp) {
        this.xpNeededToLevelUp = xpNeededToLevelUp;
    }

    public int getXpNeededCurrentLevel() {
        return xpNeededCurrentLevel;
    }

    public void setXpNeededCurrentLevel(int xpNeededCurrentLevel) {
        this.xpNeededCurrentLevel = xpNeededCurrentLevel;
    }
}
