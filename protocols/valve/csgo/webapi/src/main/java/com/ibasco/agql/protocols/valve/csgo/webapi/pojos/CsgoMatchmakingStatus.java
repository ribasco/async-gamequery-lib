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

package com.ibasco.agql.protocols.valve.csgo.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>CsgoMatchmakingStatus class.</p>
 *
 * @author Rafael Luis Ibasco
 */
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

    /**
     * <p>Getter for the field <code>scheduler</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getScheduler() {
        return scheduler;
    }

    /**
     * <p>Setter for the field <code>scheduler</code>.</p>
     *
     * @param scheduler
     *         a {@link java.lang.String} object
     */
    public void setScheduler(String scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * <p>Getter for the field <code>onlineServerCount</code>.</p>
     *
     * @return a int
     */
    public int getOnlineServerCount() {
        return onlineServerCount;
    }

    /**
     * <p>Setter for the field <code>onlineServerCount</code>.</p>
     *
     * @param onlineServerCount
     *         a int
     */
    public void setOnlineServerCount(int onlineServerCount) {
        this.onlineServerCount = onlineServerCount;
    }

    /**
     * <p>Getter for the field <code>onlinePlayerCount</code>.</p>
     *
     * @return a int
     */
    public int getOnlinePlayerCount() {
        return onlinePlayerCount;
    }

    /**
     * <p>Setter for the field <code>onlinePlayerCount</code>.</p>
     *
     * @param onlinePlayerCount
     *         a int
     */
    public void setOnlinePlayerCount(int onlinePlayerCount) {
        this.onlinePlayerCount = onlinePlayerCount;
    }

    /**
     * <p>Getter for the field <code>searchingPlayersCount</code>.</p>
     *
     * @return a int
     */
    public int getSearchingPlayersCount() {
        return searchingPlayersCount;
    }

    /**
     * <p>Setter for the field <code>searchingPlayersCount</code>.</p>
     *
     * @param searchingPlayersCount
     *         a int
     */
    public void setSearchingPlayersCount(int searchingPlayersCount) {
        this.searchingPlayersCount = searchingPlayersCount;
    }

    /**
     * <p>Getter for the field <code>searchSecondsAvg</code>.</p>
     *
     * @return a int
     */
    public int getSearchSecondsAvg() {
        return searchSecondsAvg;
    }

    /**
     * <p>Setter for the field <code>searchSecondsAvg</code>.</p>
     *
     * @param searchSecondsAvg
     *         a int
     */
    public void setSearchSecondsAvg(int searchSecondsAvg) {
        this.searchSecondsAvg = searchSecondsAvg;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
