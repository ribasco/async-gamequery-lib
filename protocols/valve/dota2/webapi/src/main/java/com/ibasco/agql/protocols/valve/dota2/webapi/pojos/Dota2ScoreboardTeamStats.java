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

import java.util.ArrayList;
import java.util.List;

public class Dota2ScoreboardTeamStats {

    @SerializedName("score")
    private int score;

    @SerializedName("tower_state")
    private int towerState;

    @SerializedName("barracks_state")
    private int barracksState;

    @SerializedName("picks")
    private List<Dota2ScoreboardTeamPicks> picks = new ArrayList<>();

    @SerializedName("bans")
    private List<Dota2ScoreboardTeamBan> bans = new ArrayList<>();

    @SerializedName("players")
    private List<Dota2ScoreboardPlayerStats> players = new ArrayList<>();

    @SerializedName("abilities")
    private List<Dota2ScoreboardTeamAbility> abilities = new ArrayList<>();

    /**
     * @return The score
     */
    public int getScore() {
        return score;
    }

    /**
     * @param score
     *         The score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * @return The towerState
     */
    public int getTowerState() {
        return towerState;
    }

    /**
     * @param towerState
     *         The tower_state
     */
    public void setTowerState(int towerState) {
        this.towerState = towerState;
    }

    /**
     * @return The barracksState
     */
    public int getBarracksState() {
        return barracksState;
    }

    /**
     * @param barracksState
     *         The barracks_state
     */
    public void setBarracksState(int barracksState) {
        this.barracksState = barracksState;
    }

    /**
     * @return The picks
     */
    public List<Dota2ScoreboardTeamPicks> getPicks() {
        return picks;
    }

    /**
     * @param picks
     *         The picks
     */
    public void setPicks(List<Dota2ScoreboardTeamPicks> picks) {
        this.picks = picks;
    }

    /**
     * @return The bans
     */
    public List<Dota2ScoreboardTeamBan> getBans() {
        return bans;
    }

    /**
     * @param bans
     *         The bans
     */
    public void setBans(List<Dota2ScoreboardTeamBan> bans) {
        this.bans = bans;
    }

    /**
     * @return The players
     */
    public List<Dota2ScoreboardPlayerStats> getPlayers() {
        return players;
    }

    /**
     * @param players
     *         The players
     */
    public void setPlayers(List<Dota2ScoreboardPlayerStats> players) {
        this.players = players;
    }

    /**
     * @return The abilities
     */
    public List<Dota2ScoreboardTeamAbility> getAbilities() {
        return abilities;
    }

    /**
     * @param abilities
     *         The abilities
     */
    public void setAbilities(List<Dota2ScoreboardTeamAbility> abilities) {
        this.abilities = abilities;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
