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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Dota2MatchHistoryInfo class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class Dota2MatchHistoryInfo {

    @SerializedName("match_id")
    @Expose
    private long matchId;

    @SerializedName("match_seq_num")
    @Expose
    private long matchSeqNum;

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
     *         The match_id
     */
    public void setMatchId(long matchId) {
        this.matchId = matchId;
    }

    /**
     * <p>Getter for the field <code>matchSeqNum</code>.</p>
     *
     * @return The matchSeqNum
     */
    public long getMatchSeqNum() {
        return matchSeqNum;
    }

    /**
     * <p>Setter for the field <code>matchSeqNum</code>.</p>
     *
     * @param matchSeqNum
     *         The match_seq_num
     */
    public void setMatchSeqNum(long matchSeqNum) {
        this.matchSeqNum = matchSeqNum;
    }

    /**
     * <p>Getter for the field <code>startTime</code>.</p>
     *
     * @return The startTime
     */
    public int getStartTime() {
        return startTime;
    }

    /**
     * <p>Setter for the field <code>startTime</code>.</p>
     *
     * @param startTime
     *         The start_time
     */
    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    /**
     * <p>Getter for the field <code>lobbyType</code>.</p>
     *
     * @return The lobbyType
     */
    public int getLobbyType() {
        return lobbyType;
    }

    /**
     * <p>Setter for the field <code>lobbyType</code>.</p>
     *
     * @param lobbyType
     *         The lobby_type
     */
    public void setLobbyType(int lobbyType) {
        this.lobbyType = lobbyType;
    }

    /**
     * <p>Getter for the field <code>radiantTeamId</code>.</p>
     *
     * @return The radiantTeamId
     */
    public int getRadiantTeamId() {
        return radiantTeamId;
    }

    /**
     * <p>Setter for the field <code>radiantTeamId</code>.</p>
     *
     * @param radiantTeamId
     *         The radiant_team_id
     */
    public void setRadiantTeamId(int radiantTeamId) {
        this.radiantTeamId = radiantTeamId;
    }

    /**
     * <p>Getter for the field <code>direTeamId</code>.</p>
     *
     * @return The direTeamId
     */
    public int getDireTeamId() {
        return direTeamId;
    }

    /**
     * <p>Setter for the field <code>direTeamId</code>.</p>
     *
     * @param direTeamId
     *         The dire_team_id
     */
    public void setDireTeamId(int direTeamId) {
        this.direTeamId = direTeamId;
    }

    /**
     * <p>Getter for the field <code>players</code>.</p>
     *
     * @return The players
     */
    public List<Dota2MatchHistoryPlayer> getPlayers() {
        return players;
    }

    /**
     * <p>Setter for the field <code>players</code>.</p>
     *
     * @param players
     *         The players
     */
    public void setPlayers(List<Dota2MatchHistoryPlayer> players) {
        this.players = players;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

}
