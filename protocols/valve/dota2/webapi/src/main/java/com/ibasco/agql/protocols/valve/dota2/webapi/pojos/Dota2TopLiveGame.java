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

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Dota2TopLiveGame class.</p>
 *
 * @author Rafael Luis Ibasco
 */
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
     * <p>Getter for the field <code>activateTime</code>.</p>
     *
     * @return The activateTime
     */
    public int getActivateTime() {
        return activateTime;
    }

    /**
     * <p>Setter for the field <code>activateTime</code>.</p>
     *
     * @param activateTime
     *         The activate_time
     */
    public void setActivateTime(int activateTime) {
        this.activateTime = activateTime;
    }

    /**
     * <p>Getter for the field <code>deactivateTime</code>.</p>
     *
     * @return The deactivateTime
     */
    public int getDeactivateTime() {
        return deactivateTime;
    }

    /**
     * <p>Setter for the field <code>deactivateTime</code>.</p>
     *
     * @param deactivateTime
     *         The deactivate_time
     */
    public void setDeactivateTime(int deactivateTime) {
        this.deactivateTime = deactivateTime;
    }

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
     * <p>Getter for the field <code>lobbyId</code>.</p>
     *
     * @return The lobbyId
     */
    public long getLobbyId() {
        return lobbyId;
    }

    /**
     * <p>Setter for the field <code>lobbyId</code>.</p>
     *
     * @param lobbyId
     *         The lobby_id
     */
    public void setLobbyId(long lobbyId) {
        this.lobbyId = lobbyId;
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
     * <p>Getter for the field <code>gameTime</code>.</p>
     *
     * @return The gameTime
     */
    public int getGameTime() {
        return gameTime;
    }

    /**
     * <p>Setter for the field <code>gameTime</code>.</p>
     *
     * @param gameTime
     *         The game_time
     */
    public void setGameTime(int gameTime) {
        this.gameTime = gameTime;
    }

    /**
     * <p>Getter for the field <code>delay</code>.</p>
     *
     * @return The delay
     */
    public int getDelay() {
        return delay;
    }

    /**
     * <p>Setter for the field <code>delay</code>.</p>
     *
     * @param delay
     *         The delay
     */
    public void setDelay(int delay) {
        this.delay = delay;
    }

    /**
     * <p>Getter for the field <code>spectators</code>.</p>
     *
     * @return The spectators
     */
    public int getSpectators() {
        return spectators;
    }

    /**
     * <p>Setter for the field <code>spectators</code>.</p>
     *
     * @param spectators
     *         The spectators
     */
    public void setSpectators(int spectators) {
        this.spectators = spectators;
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
     * <p>Getter for the field <code>averageMmr</code>.</p>
     *
     * @return The averageMmr
     */
    public int getAverageMmr() {
        return averageMmr;
    }

    /**
     * <p>Setter for the field <code>averageMmr</code>.</p>
     *
     * @param averageMmr
     *         The average_mmr
     */
    public void setAverageMmr(int averageMmr) {
        this.averageMmr = averageMmr;
    }

    /**
     * <p>Getter for the field <code>sortScore</code>.</p>
     *
     * @return The sortScore
     */
    public int getSortScore() {
        return sortScore;
    }

    /**
     * <p>Setter for the field <code>sortScore</code>.</p>
     *
     * @param sortScore
     *         The sort_score
     */
    public void setSortScore(int sortScore) {
        this.sortScore = sortScore;
    }

    /**
     * <p>Getter for the field <code>lastUpdateTime</code>.</p>
     *
     * @return The lastUpdateTime
     */
    public int getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * <p>Setter for the field <code>lastUpdateTime</code>.</p>
     *
     * @param lastUpdateTime
     *         The last_update_time
     */
    public void setLastUpdateTime(int lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    /**
     * <p>Getter for the field <code>radiantLead</code>.</p>
     *
     * @return The radiantLead
     */
    public int getRadiantLead() {
        return radiantLead;
    }

    /**
     * <p>Setter for the field <code>radiantLead</code>.</p>
     *
     * @param radiantLead
     *         The radiant_lead
     */
    public void setRadiantLead(int radiantLead) {
        this.radiantLead = radiantLead;
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

    /**
     * <p>Getter for the field <code>players</code>.</p>
     *
     * @return The players
     */
    public List<Dota2TopLiveGamePlayer> getPlayers() {
        return players;
    }

    /**
     * <p>Setter for the field <code>players</code>.</p>
     *
     * @param players
     *         The players
     */
    public void setPlayers(List<Dota2TopLiveGamePlayer> players) {
        this.players = players;
    }

    /**
     * <p>Getter for the field <code>buildingState</code>.</p>
     *
     * @return The buildingState
     */
    public int getBuildingState() {
        return buildingState;
    }

    /**
     * <p>Setter for the field <code>buildingState</code>.</p>
     *
     * @param buildingState
     *         The building_state
     */
    public void setBuildingState(int buildingState) {
        this.buildingState = buildingState;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

}
