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

package com.ibasco.agql.protocols.valve.dota2.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>Dota2ServerStatsMatch class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class Dota2ServerStatsMatch {

    @SerializedName("server_steam_id")
    private long serverSteamId;
    @SerializedName("matchId")
    private long matchId;
    @SerializedName("timestamp")
    private long timestamp;
    @SerializedName("game_time")
    private long gameTime;
    @SerializedName("game_mode")
    private int gameMode;
    @SerializedName("league_id")
    private int leagueId;

    /**
     * <p>Getter for the field <code>serverSteamId</code>.</p>
     *
     * @return The serverSteamId
     */
    public long getServerSteamId() {
        return serverSteamId;
    }

    /**
     * <p>Setter for the field <code>serverSteamId</code>.</p>
     *
     * @param serverSteamId
     *         The server_steam_id
     */
    public void setServerSteamId(long serverSteamId) {
        this.serverSteamId = serverSteamId;
    }

    /**
     * <p>Getter for the field <code>matchId</code>.</p>
     *
     * @return The matchId
     */
    public long getMatchId() {
        return matchId;
    }

    /**
     * <p>Setter for the field <code>matchId</code>.</p>
     *
     * @param matchId
     *         The matchId
     */
    public void setMatchId(long matchId) {
        this.matchId = matchId;
    }

    /**
     * <p>Getter for the field <code>timestamp</code>.</p>
     *
     * @return The timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * <p>Setter for the field <code>timestamp</code>.</p>
     *
     * @param timestamp
     *         The timestamp
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * <p>Getter for the field <code>gameTime</code>.</p>
     *
     * @return The gameTime
     */
    public long getGameTime() {
        return gameTime;
    }

    /**
     * <p>Setter for the field <code>gameTime</code>.</p>
     *
     * @param gameTime
     *         The game_time
     */
    public void setGameTime(long gameTime) {
        this.gameTime = gameTime;
    }

    /**
     * <p>Getter for the field <code>gameMode</code>.</p>
     *
     * @return The gameMode
     */
    public int getGameMode() {
        return gameMode;
    }

    /**
     * <p>Setter for the field <code>gameMode</code>.</p>
     *
     * @param gameMode
     *         The game_mode
     */
    public void setGameMode(int gameMode) {
        this.gameMode = gameMode;
    }

    /**
     * <p>Getter for the field <code>leagueId</code>.</p>
     *
     * @return The leagueId
     */
    public int getLeagueId() {
        return leagueId;
    }

    /**
     * <p>Setter for the field <code>leagueId</code>.</p>
     *
     * @param leagueId
     *         The league_id
     */
    public void setLeagueId(int leagueId) {
        this.leagueId = leagueId;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

}
