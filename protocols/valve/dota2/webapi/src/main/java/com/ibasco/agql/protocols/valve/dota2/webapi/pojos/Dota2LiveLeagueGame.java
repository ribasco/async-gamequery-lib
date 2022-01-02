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

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

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

    public Dota2ScoreboardStats getScoreboard() {
        return scoreboard;
    }

    public void setScoreboard(Dota2ScoreboardStats scoreboard) {
        this.scoreboard = scoreboard;
    }

    public List<Dota2GamePlayer> getPlayers() {
        return players;
    }

    public void setPlayers(List<Dota2GamePlayer> players) {
        this.players = players;
    }

    public Dota2LiveLeagueTeamInfo getRadiantTeam() {
        return radiantTeam;
    }

    public void setRadiantTeam(Dota2LiveLeagueTeamInfo radiantTeam) {
        this.radiantTeam = radiantTeam;
    }

    public Dota2LiveLeagueTeamInfo getDireTeam() {
        return direTeam;
    }

    public void setDireTeam(Dota2LiveLeagueTeamInfo direTeam) {
        this.direTeam = direTeam;
    }

    public long getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(long lobbyId) {
        this.lobbyId = lobbyId;
    }

    public long getMatchId() {
        return matchId;
    }

    public void setMatchId(long matchId) {
        this.matchId = matchId;
    }

    public int getNumOfSpectators() {
        return numOfSpectators;
    }

    public void setNumOfSpectators(int numOfSpectators) {
        this.numOfSpectators = numOfSpectators;
    }

    public int getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(int seriesId) {
        this.seriesId = seriesId;
    }

    public int getGameNumber() {
        return gameNumber;
    }

    public void setGameNumber(int gameNumber) {
        this.gameNumber = gameNumber;
    }

    public int getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(int leagueId) {
        this.leagueId = leagueId;
    }

    public int getStreamDelay() {
        return streamDelay;
    }

    public void setStreamDelay(int streamDelay) {
        this.streamDelay = streamDelay;
    }

    public int getRadiantSeriesWins() {
        return radiantSeriesWins;
    }

    public void setRadiantSeriesWins(int radiantSeriesWins) {
        this.radiantSeriesWins = radiantSeriesWins;
    }

    public int getDireSeriesWins() {
        return direSeriesWins;
    }

    public void setDireSeriesWins(int direSeriesWins) {
        this.direSeriesWins = direSeriesWins;
    }

    public int getSeriesType() {
        return seriesType;
    }

    public void setSeriesType(int seriesType) {
        this.seriesType = seriesType;
    }

    public int getLeagueSeriesId() {
        return leagueSeriesId;
    }

    public void setLeagueSeriesId(int leagueSeriesId) {
        this.leagueSeriesId = leagueSeriesId;
    }

    public int getLeagueGameId() {
        return leagueGameId;
    }

    public void setLeagueGameId(int leagueGameId) {
        this.leagueGameId = leagueGameId;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public int getLeagueTier() {
        return leagueTier;
    }

    public void setLeagueTier(int leagueTier) {
        this.leagueTier = leagueTier;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
