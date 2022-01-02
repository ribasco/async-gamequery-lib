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
     * @return The teamNumber
     */
    public int getTeamNumber() {
        return teamNumber;
    }

    /**
     * @param teamNumber
     *         The team_number
     */
    public void setTeamNumber(int teamNumber) {
        this.teamNumber = teamNumber;
    }

    /**
     * @return The teamId
     */
    public int getTeamId() {
        return teamId;
    }

    /**
     * @param teamId
     *         The team_id
     */
    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    /**
     * @return The teamName
     */
    public String getTeamName() {
        return teamName;
    }

    /**
     * @param teamName
     *         The team_name
     */
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    /**
     * @return The teamLogo
     */
    public String getTeamLogo() {
        return teamLogo;
    }

    /**
     * @param teamLogo
     *         The team_logo
     */
    public void setTeamLogo(String teamLogo) {
        this.teamLogo = teamLogo;
    }

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
     * @return The players
     */
    public List<Dota2ServerStatsPlayer> getPlayers() {
        return players;
    }

    /**
     * @param players
     *         The players
     */
    public void setPlayers(List<Dota2ServerStatsPlayer> players) {
        this.players = players;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

}
