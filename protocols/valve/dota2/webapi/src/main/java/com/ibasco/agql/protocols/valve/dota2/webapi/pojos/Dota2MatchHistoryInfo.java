/*
 * MIT License
 *
 * Copyright (c) 2018 Asynchronous Game Query Library
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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

public class Dota2MatchHistoryInfo {

    @SerializedName("match_id")
    @Expose
    private long matchId;
    @SerializedName("match_seq_num")
    @Expose
    private int matchSeqNum;
    @SerializedName("start_time")
    @Expose
    private int startTime;
    @SerializedName("lobby_type")
    @Expose
    private int lobbyType;
    @SerializedName("radiant_team_id")
    @Expose
    private int radiantTeamId;
    @SerializedName("dire_team_id")
    @Expose
    private int direTeamId;
    @SerializedName("players")
    @Expose
    private List<Dota2MatchHistoryPlayer> players = new ArrayList<>();

    /**
     * @return The matchId
     */
    public long getMatchId() {
        return matchId;
    }

    /**
     * @param matchId
     *         The match_id
     */
    public void setMatchId(long matchId) {
        this.matchId = matchId;
    }

    /**
     * @return The matchSeqNum
     */
    public int getMatchSeqNum() {
        return matchSeqNum;
    }

    /**
     * @param matchSeqNum
     *         The match_seq_num
     */
    public void setMatchSeqNum(int matchSeqNum) {
        this.matchSeqNum = matchSeqNum;
    }

    /**
     * @return The startTime
     */
    public int getStartTime() {
        return startTime;
    }

    /**
     * @param startTime
     *         The start_time
     */
    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    /**
     * @return The lobbyType
     */
    public int getLobbyType() {
        return lobbyType;
    }

    /**
     * @param lobbyType
     *         The lobby_type
     */
    public void setLobbyType(int lobbyType) {
        this.lobbyType = lobbyType;
    }

    /**
     * @return The radiantTeamId
     */
    public int getRadiantTeamId() {
        return radiantTeamId;
    }

    /**
     * @param radiantTeamId
     *         The radiant_team_id
     */
    public void setRadiantTeamId(int radiantTeamId) {
        this.radiantTeamId = radiantTeamId;
    }

    /**
     * @return The direTeamId
     */
    public int getDireTeamId() {
        return direTeamId;
    }

    /**
     * @param direTeamId
     *         The dire_team_id
     */
    public void setDireTeamId(int direTeamId) {
        this.direTeamId = direTeamId;
    }

    /**
     * @return The players
     */
    public List<Dota2MatchHistoryPlayer> getPlayers() {
        return players;
    }

    /**
     * @param players
     *         The players
     */
    public void setPlayers(List<Dota2MatchHistoryPlayer> players) {
        this.players = players;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

}
