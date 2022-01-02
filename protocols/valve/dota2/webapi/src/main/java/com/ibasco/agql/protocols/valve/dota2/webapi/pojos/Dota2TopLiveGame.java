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
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

public class Dota2TopLiveGame {

    @SerializedName("activate_time")
    private int activateTime;
    @SerializedName("deactivate_time")
    private int deactivateTime;
    @SerializedName("server_steam_id")
    private long serverSteamId;
    @SerializedName("lobby_id")
    private long lobbyId;
    @SerializedName("league_id")
    private int leagueId;
    @SerializedName("lobby_type")
    private int lobbyType;
    @SerializedName("game_time")
    private int gameTime;
    @SerializedName("delay")
    private int delay;
    @SerializedName("spectators")
    private int spectators;
    @SerializedName("game_mode")
    private int gameMode;
    @SerializedName("average_mmr")
    private int averageMmr;
    @SerializedName("sort_score")
    private int sortScore;
    @SerializedName("last_update_time")
    private int lastUpdateTime;
    @SerializedName("radiant_lead")
    private int radiantLead;
    @SerializedName("radiant_score")
    private int radiantScore;
    @SerializedName("dire_score")
    private int direScore;
    @SerializedName("players")
    private List<Dota2TopLiveGamePlayer> players = new ArrayList<>();
    @SerializedName("building_state")
    private int buildingState;

    /**
     * @return The activateTime
     */
    public int getActivateTime() {
        return activateTime;
    }

    /**
     * @param activateTime
     *         The activate_time
     */
    public void setActivateTime(int activateTime) {
        this.activateTime = activateTime;
    }

    /**
     * @return The deactivateTime
     */
    public int getDeactivateTime() {
        return deactivateTime;
    }

    /**
     * @param deactivateTime
     *         The deactivate_time
     */
    public void setDeactivateTime(int deactivateTime) {
        this.deactivateTime = deactivateTime;
    }

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
     * @return The lobbyId
     */
    public long getLobbyId() {
        return lobbyId;
    }

    /**
     * @param lobbyId
     *         The lobby_id
     */
    public void setLobbyId(long lobbyId) {
        this.lobbyId = lobbyId;
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
     * @return The gameTime
     */
    public int getGameTime() {
        return gameTime;
    }

    /**
     * @param gameTime
     *         The game_time
     */
    public void setGameTime(int gameTime) {
        this.gameTime = gameTime;
    }

    /**
     * @return The delay
     */
    public int getDelay() {
        return delay;
    }

    /**
     * @param delay
     *         The delay
     */
    public void setDelay(int delay) {
        this.delay = delay;
    }

    /**
     * @return The spectators
     */
    public int getSpectators() {
        return spectators;
    }

    /**
     * @param spectators
     *         The spectators
     */
    public void setSpectators(int spectators) {
        this.spectators = spectators;
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
     * @return The averageMmr
     */
    public int getAverageMmr() {
        return averageMmr;
    }

    /**
     * @param averageMmr
     *         The average_mmr
     */
    public void setAverageMmr(int averageMmr) {
        this.averageMmr = averageMmr;
    }

    /**
     * @return The sortScore
     */
    public int getSortScore() {
        return sortScore;
    }

    /**
     * @param sortScore
     *         The sort_score
     */
    public void setSortScore(int sortScore) {
        this.sortScore = sortScore;
    }

    /**
     * @return The lastUpdateTime
     */
    public int getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * @param lastUpdateTime
     *         The last_update_time
     */
    public void setLastUpdateTime(int lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    /**
     * @return The radiantLead
     */
    public int getRadiantLead() {
        return radiantLead;
    }

    /**
     * @param radiantLead
     *         The radiant_lead
     */
    public void setRadiantLead(int radiantLead) {
        this.radiantLead = radiantLead;
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

    /**
     * @return The players
     */
    public List<Dota2TopLiveGamePlayer> getPlayers() {
        return players;
    }

    /**
     * @param players
     *         The players
     */
    public void setPlayers(List<Dota2TopLiveGamePlayer> players) {
        this.players = players;
    }

    /**
     * @return The buildingState
     */
    public int getBuildingState() {
        return buildingState;
    }

    /**
     * @param buildingState
     *         The building_state
     */
    public void setBuildingState(int buildingState) {
        this.buildingState = buildingState;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

}
