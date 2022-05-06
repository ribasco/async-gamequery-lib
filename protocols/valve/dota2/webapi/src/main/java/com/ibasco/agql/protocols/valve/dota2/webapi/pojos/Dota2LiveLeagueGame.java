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
 * <p>Dota2LiveLeagueGame class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class Dota2LiveLeagueGame {

    private List<Dota2GamePlayer> players = new ArrayList<>();

    @SerializedName("radiant_team")
    private Dota2LiveLeagueTeamInfo radiantTeam = new Dota2LiveLeagueTeamInfo();

    @SerializedName("dire_team")
    private Dota2LiveLeagueTeamInfo direTeam = new Dota2LiveLeagueTeamInfo();

    @SerializedName("lobby_id")
    private long lobbyId;

    @SerializedName("match_id")
    private long matchId;

    @SerializedName("spectators")
    private int numOfSpectators;

    @SerializedName("series_id")
    private int seriesId;

    @SerializedName("game_number")
    private int gameNumber;

    @SerializedName("league_id")
    private int leagueId;

    @SerializedName("stream_delay_s")
    private int streamDelay;

    @SerializedName("radiant_series_wins")
    private int radiantSeriesWins;

    @SerializedName("dire_series_wins")
    private int direSeriesWins;

    @SerializedName("series_type")
    private int seriesType;

    @SerializedName("league_series_id")
    private int leagueSeriesId;

    @SerializedName("league_game_id")
    private int leagueGameId;

    @SerializedName("stage_name")
    private String stageName;

    @SerializedName("league_tier")
    private int leagueTier;

    @SerializedName("scoreboard")
    private Dota2ScoreboardStats scoreboard = new Dota2ScoreboardStats();

    /**
     * <p>Getter for the field <code>scoreboard</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.dota2.webapi.pojos.Dota2ScoreboardStats} object
     */
    public Dota2ScoreboardStats getScoreboard() {
        return scoreboard;
    }

    /**
     * <p>Setter for the field <code>scoreboard</code>.</p>
     *
     * @param scoreboard
     *         a {@link com.ibasco.agql.protocols.valve.dota2.webapi.pojos.Dota2ScoreboardStats} object
     */
    public void setScoreboard(Dota2ScoreboardStats scoreboard) {
        this.scoreboard = scoreboard;
    }

    /**
     * <p>Getter for the field <code>players</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<Dota2GamePlayer> getPlayers() {
        return players;
    }

    /**
     * <p>Setter for the field <code>players</code>.</p>
     *
     * @param players
     *         a {@link java.util.List} object
     */
    public void setPlayers(List<Dota2GamePlayer> players) {
        this.players = players;
    }

    /**
     * <p>Getter for the field <code>radiantTeam</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.dota2.webapi.pojos.Dota2LiveLeagueTeamInfo} object
     */
    public Dota2LiveLeagueTeamInfo getRadiantTeam() {
        return radiantTeam;
    }

    /**
     * <p>Setter for the field <code>radiantTeam</code>.</p>
     *
     * @param radiantTeam
     *         a {@link com.ibasco.agql.protocols.valve.dota2.webapi.pojos.Dota2LiveLeagueTeamInfo} object
     */
    public void setRadiantTeam(Dota2LiveLeagueTeamInfo radiantTeam) {
        this.radiantTeam = radiantTeam;
    }

    /**
     * <p>Getter for the field <code>direTeam</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.dota2.webapi.pojos.Dota2LiveLeagueTeamInfo} object
     */
    public Dota2LiveLeagueTeamInfo getDireTeam() {
        return direTeam;
    }

    /**
     * <p>Setter for the field <code>direTeam</code>.</p>
     *
     * @param direTeam
     *         a {@link com.ibasco.agql.protocols.valve.dota2.webapi.pojos.Dota2LiveLeagueTeamInfo} object
     */
    public void setDireTeam(Dota2LiveLeagueTeamInfo direTeam) {
        this.direTeam = direTeam;
    }

    /**
     * <p>Getter for the field <code>lobbyId</code>.</p>
     *
     * @return a long
     */
    public long getLobbyId() {
        return lobbyId;
    }

    /**
     * <p>Setter for the field <code>lobbyId</code>.</p>
     *
     * @param lobbyId
     *         a long
     */
    public void setLobbyId(long lobbyId) {
        this.lobbyId = lobbyId;
    }

    /**
     * <p>Getter for the field <code>matchId</code>.</p>
     *
     * @return a long
     */
    public long getMatchId() {
        return matchId;
    }

    /**
     * <p>Setter for the field <code>matchId</code>.</p>
     *
     * @param matchId
     *         a long
     */
    public void setMatchId(long matchId) {
        this.matchId = matchId;
    }

    /**
     * <p>Getter for the field <code>numOfSpectators</code>.</p>
     *
     * @return a int
     */
    public int getNumOfSpectators() {
        return numOfSpectators;
    }

    /**
     * <p>Setter for the field <code>numOfSpectators</code>.</p>
     *
     * @param numOfSpectators
     *         a int
     */
    public void setNumOfSpectators(int numOfSpectators) {
        this.numOfSpectators = numOfSpectators;
    }

    /**
     * <p>Getter for the field <code>seriesId</code>.</p>
     *
     * @return a int
     */
    public int getSeriesId() {
        return seriesId;
    }

    /**
     * <p>Setter for the field <code>seriesId</code>.</p>
     *
     * @param seriesId
     *         a int
     */
    public void setSeriesId(int seriesId) {
        this.seriesId = seriesId;
    }

    /**
     * <p>Getter for the field <code>gameNumber</code>.</p>
     *
     * @return a int
     */
    public int getGameNumber() {
        return gameNumber;
    }

    /**
     * <p>Setter for the field <code>gameNumber</code>.</p>
     *
     * @param gameNumber
     *         a int
     */
    public void setGameNumber(int gameNumber) {
        this.gameNumber = gameNumber;
    }

    /**
     * <p>Getter for the field <code>leagueId</code>.</p>
     *
     * @return a int
     */
    public int getLeagueId() {
        return leagueId;
    }

    /**
     * <p>Setter for the field <code>leagueId</code>.</p>
     *
     * @param leagueId
     *         a int
     */
    public void setLeagueId(int leagueId) {
        this.leagueId = leagueId;
    }

    /**
     * <p>Getter for the field <code>streamDelay</code>.</p>
     *
     * @return a int
     */
    public int getStreamDelay() {
        return streamDelay;
    }

    /**
     * <p>Setter for the field <code>streamDelay</code>.</p>
     *
     * @param streamDelay
     *         a int
     */
    public void setStreamDelay(int streamDelay) {
        this.streamDelay = streamDelay;
    }

    /**
     * <p>Getter for the field <code>radiantSeriesWins</code>.</p>
     *
     * @return a int
     */
    public int getRadiantSeriesWins() {
        return radiantSeriesWins;
    }

    /**
     * <p>Setter for the field <code>radiantSeriesWins</code>.</p>
     *
     * @param radiantSeriesWins
     *         a int
     */
    public void setRadiantSeriesWins(int radiantSeriesWins) {
        this.radiantSeriesWins = radiantSeriesWins;
    }

    /**
     * <p>Getter for the field <code>direSeriesWins</code>.</p>
     *
     * @return a int
     */
    public int getDireSeriesWins() {
        return direSeriesWins;
    }

    /**
     * <p>Setter for the field <code>direSeriesWins</code>.</p>
     *
     * @param direSeriesWins
     *         a int
     */
    public void setDireSeriesWins(int direSeriesWins) {
        this.direSeriesWins = direSeriesWins;
    }

    /**
     * <p>Getter for the field <code>seriesType</code>.</p>
     *
     * @return a int
     */
    public int getSeriesType() {
        return seriesType;
    }

    /**
     * <p>Setter for the field <code>seriesType</code>.</p>
     *
     * @param seriesType
     *         a int
     */
    public void setSeriesType(int seriesType) {
        this.seriesType = seriesType;
    }

    /**
     * <p>Getter for the field <code>leagueSeriesId</code>.</p>
     *
     * @return a int
     */
    public int getLeagueSeriesId() {
        return leagueSeriesId;
    }

    /**
     * <p>Setter for the field <code>leagueSeriesId</code>.</p>
     *
     * @param leagueSeriesId
     *         a int
     */
    public void setLeagueSeriesId(int leagueSeriesId) {
        this.leagueSeriesId = leagueSeriesId;
    }

    /**
     * <p>Getter for the field <code>leagueGameId</code>.</p>
     *
     * @return a int
     */
    public int getLeagueGameId() {
        return leagueGameId;
    }

    /**
     * <p>Setter for the field <code>leagueGameId</code>.</p>
     *
     * @param leagueGameId
     *         a int
     */
    public void setLeagueGameId(int leagueGameId) {
        this.leagueGameId = leagueGameId;
    }

    /**
     * <p>Getter for the field <code>stageName</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getStageName() {
        return stageName;
    }

    /**
     * <p>Setter for the field <code>stageName</code>.</p>
     *
     * @param stageName
     *         a {@link java.lang.String} object
     */
    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    /**
     * <p>Getter for the field <code>leagueTier</code>.</p>
     *
     * @return a int
     */
    public int getLeagueTier() {
        return leagueTier;
    }

    /**
     * <p>Setter for the field <code>leagueTier</code>.</p>
     *
     * @param leagueTier
     *         a int
     */
    public void setLeagueTier(int leagueTier) {
        this.leagueTier = leagueTier;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
