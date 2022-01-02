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

public class Dota2RealtimeServerStats {

    @SerializedName("match")
    private Dota2ServerStatsMatch match;
    @SerializedName("teams")
    private List<Dota2ServerStatsTeam> teams = new ArrayList<>();
    @SerializedName("buildings")
    private List<Dota2ServerStatsBldg> buildings = new ArrayList<>();
    @SerializedName("graph_data")
    private Dota2ServerStatsGraph graphData;
    @SerializedName("delta_frame")
    private boolean deltaFrame;

    /**
     * @return A {@link Dota2ServerStatsMatch} instance
     */
    public Dota2ServerStatsMatch getMatch() {
        return match;
    }

    /**
     * @param match
     *         The match
     */
    public void setMatch(Dota2ServerStatsMatch match) {
        this.match = match;
    }

    /**
     * @return The teams
     */
    public List<Dota2ServerStatsTeam> getTeams() {
        return teams;
    }

    /**
     * @param teams
     *         The teams
     */
    public void setTeams(List<Dota2ServerStatsTeam> teams) {
        this.teams = teams;
    }

    /**
     * @return The buildings
     */
    public List<Dota2ServerStatsBldg> getBuildings() {
        return buildings;
    }

    /**
     * @param buildings
     *         The buildings
     */
    public void setBuildings(List<Dota2ServerStatsBldg> buildings) {
        this.buildings = buildings;
    }

    /**
     * @return The graphData
     */
    public Dota2ServerStatsGraph getGraphData() {
        return graphData;
    }

    /**
     * @param graphData
     *         The graph_data
     */
    public void setGraphData(Dota2ServerStatsGraph graphData) {
        this.graphData = graphData;
    }

    /**
     * @return The deltaFrame
     */
    public boolean isDeltaFrame() {
        return deltaFrame;
    }

    /**
     * @param deltaFrame
     *         The delta_frame
     */
    public void setDeltaFrame(boolean deltaFrame) {
        this.deltaFrame = deltaFrame;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

}
