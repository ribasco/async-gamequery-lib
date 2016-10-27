/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Luis Ibasco
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **************************************************************************************************/

package com.ribasco.rglib.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by raffy on 10/27/2016.
 */
public class SteamPlayerBadgeInfo {
    @SerializedName("badges")
    private List<SteamPlayerBadge> playerBadges;

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
