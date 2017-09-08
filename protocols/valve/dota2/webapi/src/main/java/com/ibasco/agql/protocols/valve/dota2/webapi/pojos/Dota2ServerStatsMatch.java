/*
 * MIT License
 *
 * Copyright (c) 2016 Asynchronous Game Query Library
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ibasco.agql.protocols.valve.dota2.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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
     * @return The serverSteamId
     */
    public long getServerSteamId() {
        return serverSteamId;
    }

    /**
     * @param serverSteamId
     *         The server_steam_id
     */
    public void setServerSteamId(long serverSteamId) {
        this.serverSteamId = serverSteamId;
    }

    /**
     * @return The matchId
     */
    public long getMatchId() {
        return matchId;
    }

    /**
     * @param matchId
     *         The matchId
     */
    public void setMatchId(long matchId) {
        this.matchId = matchId;
    }

    /**
     * @return The timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp
     *         The timestamp
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return The gameTime
     */
    public long getGameTime() {
        return gameTime;
    }

    /**
     * @param gameTime
     *         The game_time
     */
    public void setGameTime(long gameTime) {
        this.gameTime = gameTime;
    }

    /**
     * @return The gameMode
     */
    public int getGameMode() {
        return gameMode;
    }

    /**
     * @param gameMode
     *         The game_mode
     */
    public void setGameMode(int gameMode) {
        this.gameMode = gameMode;
    }

    /**
     * @return The leagueId
     */
    public int getLeagueId() {
        return leagueId;
    }

    /**
     * @param leagueId
     *         The league_id
     */
    public void setLeagueId(int leagueId) {
        this.leagueId = leagueId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

}
