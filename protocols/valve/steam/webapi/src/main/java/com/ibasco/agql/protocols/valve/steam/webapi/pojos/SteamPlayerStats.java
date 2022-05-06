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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raffy on 10/27/2016.
 *
 * @author Rafael Luis Ibasco
 */
public class SteamPlayerStats {

    private String steamId;

    private String gameName;

    private List<SteamPlayerAchievement> achievements = new ArrayList<>();

    private List<SteamKeyValuePair<String, Integer>> stats = new ArrayList<>();

    /**
     * <p>Getter for the field <code>steamId</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getSteamId() {
        return steamId;
    }

    /**
     * <p>Setter for the field <code>steamId</code>.</p>
     *
     * @param steamId
     *         a {@link java.lang.String} object
     */
    public void setSteamId(String steamId) {
        this.steamId = steamId;
    }

    /**
     * <p>Getter for the field <code>gameName</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getGameName() {
        return gameName;
    }

    /**
     * <p>Setter for the field <code>gameName</code>.</p>
     *
     * @param gameName
     *         a {@link java.lang.String} object
     */
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    /**
     * <p>Getter for the field <code>achievements</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<SteamPlayerAchievement> getAchievements() {
        return achievements;
    }

    /**
     * <p>Setter for the field <code>achievements</code>.</p>
     *
     * @param achievements
     *         a {@link java.util.List} object
     */
    public void setAchievements(List<SteamPlayerAchievement> achievements) {
        this.achievements = achievements;
    }

    /**
     * <p>Getter for the field <code>stats</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<SteamKeyValuePair<String, Integer>> getStats() {
        return stats;
    }

    /**
     * <p>Setter for the field <code>stats</code>.</p>
     *
     * @param stats
     *         a {@link java.util.List} object
     */
    public void setStats(List<SteamKeyValuePair<String, Integer>> stats) {
        this.stats = stats;
    }
}
