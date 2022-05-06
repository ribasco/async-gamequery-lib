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
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Dota2MatchDetails class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class Dota2MatchDetails {

    @SerializedName("players")
    private List<Dota2MatchPlayer> players = new ArrayList<>();

    @SerializedName("radiant_win")
    private boolean radiantWin;

    @SerializedName("duration")
    private int duration;

    @SerializedName("pre_game_duration")
    private int preGameDuration;

    @SerializedName("start_time")
    private int startTime;

    @SerializedName("match_id")
    private long matchId;

    @SerializedName("match_seq_num")
    private long matchSeqNum;

    @SerializedName("tower_status_radiant")
    private int towerStatusRadiant;

    @SerializedName("tower_status_dire")
    private int towerStatusDire;

    @SerializedName("barracks_status_radiant")
    private int barracksStatusRadiant;

    @SerializedName("barracks_status_dire")
    private int barracksStatusDire;

    @SerializedName("cluster")
    private int cluster;

    @SerializedName("first_blood_time")
    private int firstBloodTime;

    @SerializedName("lobby_type")
    private int lobbyType;

    @SerializedName("human_players")
    private int humanPlayers;

    @SerializedName("leagueId")
    private int leagueId;

    @SerializedName("positive_votes")
    private int positiveVotes;

    @SerializedName("negative_votes")
    private int negativeVotes;

    @SerializedName("game_mode")
    private int gameMode;

    @SerializedName("flags")
    private int flags;

    @SerializedName("engine")
    private int engine;

    @SerializedName("radiant_score")
    private int radiantScore;

    @SerializedName("dire_score")
    private int direScore;

    /**
     * <p>Getter for the field <code>players</code>.</p>
     *
     * @return The players
     */
    public List<Dota2MatchPlayer> getPlayers() {
        return players;
    }

    /**
     * <p>Setter for the field <code>players</code>.</p>
     *
     * @param players
     *         The players
     */
    public void setPlayers(List<Dota2MatchPlayer> players) {
        this.players = players;
    }

    /**
     * <p>isRadiantWin.</p>
     *
     * @return The radiantWin
     */
    public boolean isRadiantWin() {
        return radiantWin;
    }

    /**
     * <p>Setter for the field <code>radiantWin</code>.</p>
     *
     * @param radiantWin
     *         The radiant_win
     */
    public void setRadiantWin(boolean radiantWin) {
        this.radiantWin = radiantWin;
    }

    /**
     * <p>Getter for the field <code>duration</code>.</p>
     *
     * @return The duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * <p>Setter for the field <code>duration</code>.</p>
     *
     * @param duration
     *         The duration
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * <p>Getter for the field <code>preGameDuration</code>.</p>
     *
     * @return The preGameDuration
     */
    public int getPreGameDuration() {
        return preGameDuration;
    }

    /**
     * <p>Setter for the field <code>preGameDuration</code>.</p>
     *
     * @param preGameDuration
     *         The pre_game_duration
     */
    public void setPreGameDuration(int preGameDuration) {
        this.preGameDuration = preGameDuration;
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
     * <p>Getter for the field <code>towerStatusRadiant</code>.</p>
     *
     * @return The towerStatusRadiant
     */
    public int getTowerStatusRadiant() {
        return towerStatusRadiant;
    }

    /**
     * <p>Setter for the field <code>towerStatusRadiant</code>.</p>
     *
     * @param towerStatusRadiant
     *         The tower_status_radiant
     */
    public void setTowerStatusRadiant(int towerStatusRadiant) {
        this.towerStatusRadiant = towerStatusRadiant;
    }

    /**
     * <p>Getter for the field <code>towerStatusDire</code>.</p>
     *
     * @return The towerStatusDire
     */
    public int getTowerStatusDire() {
        return towerStatusDire;
    }

    /**
     * <p>Setter for the field <code>towerStatusDire</code>.</p>
     *
     * @param towerStatusDire
     *         The tower_status_dire
     */
    public void setTowerStatusDire(int towerStatusDire) {
        this.towerStatusDire = towerStatusDire;
    }

    /**
     * <p>Getter for the field <code>barracksStatusRadiant</code>.</p>
     *
     * @return The barracksStatusRadiant
     */
    public int getBarracksStatusRadiant() {
        return barracksStatusRadiant;
    }

    /**
     * <p>Setter for the field <code>barracksStatusRadiant</code>.</p>
     *
     * @param barracksStatusRadiant
     *         The barracks_status_radiant
     */
    public void setBarracksStatusRadiant(int barracksStatusRadiant) {
        this.barracksStatusRadiant = barracksStatusRadiant;
    }

    /**
     * <p>Getter for the field <code>barracksStatusDire</code>.</p>
     *
     * @return The barracksStatusDire
     */
    public int getBarracksStatusDire() {
        return barracksStatusDire;
    }

    /**
     * <p>Setter for the field <code>barracksStatusDire</code>.</p>
     *
     * @param barracksStatusDire
     *         The barracks_status_dire
     */
    public void setBarracksStatusDire(int barracksStatusDire) {
        this.barracksStatusDire = barracksStatusDire;
    }

    /**
     * <p>Getter for the field <code>cluster</code>.</p>
     *
     * @return The cluster
     */
    public int getCluster() {
        return cluster;
    }

    /**
     * <p>Setter for the field <code>cluster</code>.</p>
     *
     * @param cluster
     *         The cluster
     */
    public void setCluster(int cluster) {
        this.cluster = cluster;
    }

    /**
     * <p>Getter for the field <code>firstBloodTime</code>.</p>
     *
     * @return The firstBloodTime
     */
    public int getFirstBloodTime() {
        return firstBloodTime;
    }

    /**
     * <p>Setter for the field <code>firstBloodTime</code>.</p>
     *
     * @param firstBloodTime
     *         The first_blood_time
     */
    public void setFirstBloodTime(int firstBloodTime) {
        this.firstBloodTime = firstBloodTime;
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
     * <p>Getter for the field <code>humanPlayers</code>.</p>
     *
     * @return The humanPlayers
     */
    public int getHumanPlayers() {
        return humanPlayers;
    }

    /**
     * <p>Setter for the field <code>humanPlayers</code>.</p>
     *
     * @param humanPlayers
     *         The human_players
     */
    public void setHumanPlayers(int humanPlayers) {
        this.humanPlayers = humanPlayers;
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
     *         The leagueId
     */
    public void setLeagueId(int leagueId) {
        this.leagueId = leagueId;
    }

    /**
     * <p>Getter for the field <code>positiveVotes</code>.</p>
     *
     * @return The positiveVotes
     */
    public int getPositiveVotes() {
        return positiveVotes;
    }

    /**
     * <p>Setter for the field <code>positiveVotes</code>.</p>
     *
     * @param positiveVotes
     *         The positive_votes
     */
    public void setPositiveVotes(int positiveVotes) {
        this.positiveVotes = positiveVotes;
    }

    /**
     * <p>Getter for the field <code>negativeVotes</code>.</p>
     *
     * @return The negativeVotes
     */
    public int getNegativeVotes() {
        return negativeVotes;
    }

    /**
     * <p>Setter for the field <code>negativeVotes</code>.</p>
     *
     * @param negativeVotes
     *         The negative_votes
     */
    public void setNegativeVotes(int negativeVotes) {
        this.negativeVotes = negativeVotes;
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
     * <p>Getter for the field <code>flags</code>.</p>
     *
     * @return The flags
     */
    public int getFlags() {
        return flags;
    }

    /**
     * <p>Setter for the field <code>flags</code>.</p>
     *
     * @param flags
     *         The flags
     */
    public void setFlags(int flags) {
        this.flags = flags;
    }

    /**
     * <p>Getter for the field <code>engine</code>.</p>
     *
     * @return The engine
     */
    public int getEngine() {
        return engine;
    }

    /**
     * <p>Setter for the field <code>engine</code>.</p>
     *
     * @param engine
     *         The engine
     */
    public void setEngine(int engine) {
        this.engine = engine;
    }

    /**
     * <p>Getter for the field <code>radiantScore</code>.</p>
     *
     * @return The radiantScore
     */
    public int getRadiantScore() {
        return radiantScore;
    }

    /**
     * <p>Setter for the field <code>radiantScore</code>.</p>
     *
     * @param radiantScore
     *         The radiant_score
     */
    public void setRadiantScore(int radiantScore) {
        this.radiantScore = radiantScore;
    }

    /**
     * <p>Getter for the field <code>direScore</code>.</p>
     *
     * @return The direScore
     */
    public int getDireScore() {
        return direScore;
    }

    /**
     * <p>Setter for the field <code>direScore</code>.</p>
     *
     * @param direScore
     *         The dire_score
     */
    public void setDireScore(int direScore) {
        this.direScore = direScore;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

}
