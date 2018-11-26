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

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

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
    private int matchSeqNum;

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
     * @return The players
     */
    public List<Dota2MatchPlayer> getPlayers() {
        return players;
    }

    /**
     * @param players
     *         The players
     */
    public void setPlayers(List<Dota2MatchPlayer> players) {
        this.players = players;
    }

    /**
     * @return The radiantWin
     */
    public boolean isRadiantWin() {
        return radiantWin;
    }

    /**
     * @param radiantWin
     *         The radiant_win
     */
    public void setRadiantWin(boolean radiantWin) {
        this.radiantWin = radiantWin;
    }

    /**
     * @return The duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * @param duration
     *         The duration
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * @return The preGameDuration
     */
    public int getPreGameDuration() {
        return preGameDuration;
    }

    /**
     * @param preGameDuration
     *         The pre_game_duration
     */
    public void setPreGameDuration(int preGameDuration) {
        this.preGameDuration = preGameDuration;
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
     * @return The towerStatusRadiant
     */
    public int getTowerStatusRadiant() {
        return towerStatusRadiant;
    }

    /**
     * @param towerStatusRadiant
     *         The tower_status_radiant
     */
    public void setTowerStatusRadiant(int towerStatusRadiant) {
        this.towerStatusRadiant = towerStatusRadiant;
    }

    /**
     * @return The towerStatusDire
     */
    public int getTowerStatusDire() {
        return towerStatusDire;
    }

    /**
     * @param towerStatusDire
     *         The tower_status_dire
     */
    public void setTowerStatusDire(int towerStatusDire) {
        this.towerStatusDire = towerStatusDire;
    }

    /**
     * @return The barracksStatusRadiant
     */
    public int getBarracksStatusRadiant() {
        return barracksStatusRadiant;
    }

    /**
     * @param barracksStatusRadiant
     *         The barracks_status_radiant
     */
    public void setBarracksStatusRadiant(int barracksStatusRadiant) {
        this.barracksStatusRadiant = barracksStatusRadiant;
    }

    /**
     * @return The barracksStatusDire
     */
    public int getBarracksStatusDire() {
        return barracksStatusDire;
    }

    /**
     * @param barracksStatusDire
     *         The barracks_status_dire
     */
    public void setBarracksStatusDire(int barracksStatusDire) {
        this.barracksStatusDire = barracksStatusDire;
    }

    /**
     * @return The cluster
     */
    public int getCluster() {
        return cluster;
    }

    /**
     * @param cluster
     *         The cluster
     */
    public void setCluster(int cluster) {
        this.cluster = cluster;
    }

    /**
     * @return The firstBloodTime
     */
    public int getFirstBloodTime() {
        return firstBloodTime;
    }

    /**
     * @param firstBloodTime
     *         The first_blood_time
     */
    public void setFirstBloodTime(int firstBloodTime) {
        this.firstBloodTime = firstBloodTime;
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
     * @return The humanPlayers
     */
    public int getHumanPlayers() {
        return humanPlayers;
    }

    /**
     * @param humanPlayers
     *         The human_players
     */
    public void setHumanPlayers(int humanPlayers) {
        this.humanPlayers = humanPlayers;
    }

    /**
     * @return The leagueId
     */
    public int getLeagueId() {
        return leagueId;
    }

    /**
     * @param leagueId
     *         The leagueId
     */
    public void setLeagueId(int leagueId) {
        this.leagueId = leagueId;
    }

    /**
     * @return The positiveVotes
     */
    public int getPositiveVotes() {
        return positiveVotes;
    }

    /**
     * @param positiveVotes
     *         The positive_votes
     */
    public void setPositiveVotes(int positiveVotes) {
        this.positiveVotes = positiveVotes;
    }

    /**
     * @return The negativeVotes
     */
    public int getNegativeVotes() {
        return negativeVotes;
    }

    /**
     * @param negativeVotes
     *         The negative_votes
     */
    public void setNegativeVotes(int negativeVotes) {
        this.negativeVotes = negativeVotes;
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
     * @return The flags
     */
    public int getFlags() {
        return flags;
    }

    /**
     * @param flags
     *         The flags
     */
    public void setFlags(int flags) {
        this.flags = flags;
    }

    /**
     * @return The engine
     */
    public int getEngine() {
        return engine;
    }

    /**
     * @param engine
     *         The engine
     */
    public void setEngine(int engine) {
        this.engine = engine;
    }

    /**
     * @return The radiantScore
     */
    public int getRadiantScore() {
        return radiantScore;
    }

    /**
     * @param radiantScore
     *         The radiant_score
     */
    public void setRadiantScore(int radiantScore) {
        this.radiantScore = radiantScore;
    }

    /**
     * @return The direScore
     */
    public int getDireScore() {
        return direScore;
    }

    /**
     * @param direScore
     *         The dire_score
     */
    public void setDireScore(int direScore) {
        this.direScore = direScore;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

}
