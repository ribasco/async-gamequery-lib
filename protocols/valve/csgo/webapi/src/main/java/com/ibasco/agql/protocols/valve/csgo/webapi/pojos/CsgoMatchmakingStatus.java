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

package com.ibasco.agql.protocols.valve.csgo.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class CsgoMatchmakingStatus {
    private String scheduler;
    @SerializedName("online_servers")
    private int onlineServerCount;
    @SerializedName("online_players")
    private int onlinePlayerCount;
    @SerializedName("searching_players")
    private int searchingPlayersCount;
    @SerializedName("search_seconds_avg")
    private int searchSecondsAvg;

    public String getScheduler() {
        return scheduler;
    }

    public void setScheduler(String scheduler) {
        this.scheduler = scheduler;
    }

    public int getOnlineServerCount() {
        return onlineServerCount;
    }

    public void setOnlineServerCount(int onlineServerCount) {
        this.onlineServerCount = onlineServerCount;
    }

    public int getOnlinePlayerCount() {
        return onlinePlayerCount;
    }

    public void setOnlinePlayerCount(int onlinePlayerCount) {
        this.onlinePlayerCount = onlinePlayerCount;
    }

    public int getSearchingPlayersCount() {
        return searchingPlayersCount;
    }

    public void setSearchingPlayersCount(int searchingPlayersCount) {
        this.searchingPlayersCount = searchingPlayersCount;
    }

    public int getSearchSecondsAvg() {
        return searchSecondsAvg;
    }

    public void setSearchSecondsAvg(int searchSecondsAvg) {
        this.searchSecondsAvg = searchSecondsAvg;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
