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
