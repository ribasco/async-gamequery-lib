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
 * <p>Dota2ServerStatsTeam class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class Dota2ServerStatsTeam {

    @SerializedName("team_number")
    private int teamNumber;

    @SerializedName("team_id")
    private int teamId;

    @SerializedName("team_name")
    private String teamName;

    @SerializedName("team_logo")
    private String teamLogo;

    @SerializedName("score")
    private int score;

    @SerializedName("players")
    private List<Dota2ServerStatsPlayer> players = new ArrayList<Dota2ServerStatsPlayer>();

    /**
     * <p>Getter for the field <code>teamNumber</code>.</p>
     *
     * @return The teamNumber
     */
    public int getTeamNumber() {
        return teamNumber;
    }

    /**
     * <p>Setter for the field <code>teamNumber</code>.</p>
     *
     * @param teamNumber
     *         The team_number
     */
    public void setTeamNumber(int teamNumber) {
        this.teamNumber = teamNumber;
    }

    /**
     * <p>Getter for the field <code>teamId</code>.</p>
     *
     * @return The teamId
     */
    public int getTeamId() {
        return teamId;
    }

    /**
     * <p>Setter for the field <code>teamId</code>.</p>
     *
     * @param teamId
     *         The team_id
     */
    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    /**
     * <p>Getter for the field <code>teamName</code>.</p>
     *
     * @return The teamName
     */
    public String getTeamName() {
        return teamName;
    }

    /**
     * <p>Setter for the field <code>teamName</code>.</p>
     *
     * @param teamName
     *         The team_name
     */
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    /**
     * <p>Getter for the field <code>teamLogo</code>.</p>
     *
     * @return The teamLogo
     */
    public String getTeamLogo() {
        return teamLogo;
    }

    /**
     * <p>Setter for the field <code>teamLogo</code>.</p>
     *
     * @param teamLogo
     *         The team_logo
     */
    public void setTeamLogo(String teamLogo) {
        this.teamLogo = teamLogo;
    }

    /**
     * <p>Getter for the field <code>score</code>.</p>
     *
     * @return The score
     */
    public int getScore() {
        return score;
    }

    /**
     * <p>Setter for the field <code>score</code>.</p>
     *
     * @param score
     *         The score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * <p>Getter for the field <code>players</code>.</p>
     *
     * @return The players
     */
    public List<Dota2ServerStatsPlayer> getPlayers() {
        return players;
    }

    /**
     * <p>Setter for the field <code>players</code>.</p>
     *
     * @param players
     *         The players
     */
    public void setPlayers(List<Dota2ServerStatsPlayer> players) {
        this.players = players;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

}
