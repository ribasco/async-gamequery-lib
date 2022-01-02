/*
 * Copyright (c) 2018-2022 Asynchronous Game Query Library
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
 * @see Steam
 */
public class SteamPlayerStats {
    private String steamId;
    private String gameName;
    private List<SteamPlayerAchievement> achievements = new ArrayList<>();
    private List<SteamKeyValuePair<String, Integer>> stats = new ArrayList<>();

    public String getSteamId() {
        return steamId;
    }

    public void setSteamId(String steamId) {
        this.steamId = steamId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public List<SteamPlayerAchievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<SteamPlayerAchievement> achievements) {
        this.achievements = achievements;
    }

    public List<SteamKeyValuePair<String, Integer>> getStats() {
        return stats;
    }

    public void setStats(List<SteamKeyValuePair<String, Integer>> stats) {
        this.stats = stats;
    }
}
