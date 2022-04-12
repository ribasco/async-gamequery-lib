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
public class SteamGameStatsSchemaInfo {
    private List<SteamGameAchievementSchema> achievementSchemaList = new ArrayList<>();
    private List<SteamGameStatsSchema> statsSchemaList = new ArrayList<>();

    /**
     * <p>Getter for the field <code>achievementSchemaList</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<SteamGameAchievementSchema> getAchievementSchemaList() {
        return achievementSchemaList;
    }

    /**
     * <p>Setter for the field <code>achievementSchemaList</code>.</p>
     *
     * @param achievementSchemaList a {@link java.util.List} object
     */
    public void setAchievementSchemaList(List<SteamGameAchievementSchema> achievementSchemaList) {
        this.achievementSchemaList = achievementSchemaList;
    }

    /**
     * <p>Getter for the field <code>statsSchemaList</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<SteamGameStatsSchema> getStatsSchemaList() {
        return statsSchemaList;
    }

    /**
     * <p>Setter for the field <code>statsSchemaList</code>.</p>
     *
     * @param statsSchemaList a {@link java.util.List} object
     */
    public void setStatsSchemaList(List<SteamGameStatsSchema> statsSchemaList) {
        this.statsSchemaList = statsSchemaList;
    }
}
