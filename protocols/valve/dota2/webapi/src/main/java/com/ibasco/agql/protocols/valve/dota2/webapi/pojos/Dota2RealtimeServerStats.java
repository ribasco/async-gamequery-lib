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
 * <p>Dota2RealtimeServerStats class.</p>
 *
 * @author Rafael Luis Ibasco
 */
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
     * <p>Getter for the field <code>match</code>.</p>
     *
     * @return A {@link com.ibasco.agql.protocols.valve.dota2.webapi.pojos.Dota2ServerStatsMatch} instance
     */
    public Dota2ServerStatsMatch getMatch() {
        return match;
    }

    /**
     * <p>Setter for the field <code>match</code>.</p>
     *
     * @param match
     *         The match
     */
    public void setMatch(Dota2ServerStatsMatch match) {
        this.match = match;
    }

    /**
     * <p>Getter for the field <code>teams</code>.</p>
     *
     * @return The teams
     */
    public List<Dota2ServerStatsTeam> getTeams() {
        return teams;
    }

    /**
     * <p>Setter for the field <code>teams</code>.</p>
     *
     * @param teams
     *         The teams
     */
    public void setTeams(List<Dota2ServerStatsTeam> teams) {
        this.teams = teams;
    }

    /**
     * <p>Getter for the field <code>buildings</code>.</p>
     *
     * @return The buildings
     */
    public List<Dota2ServerStatsBldg> getBuildings() {
        return buildings;
    }

    /**
     * <p>Setter for the field <code>buildings</code>.</p>
     *
     * @param buildings
     *         The buildings
     */
    public void setBuildings(List<Dota2ServerStatsBldg> buildings) {
        this.buildings = buildings;
    }

    /**
     * <p>Getter for the field <code>graphData</code>.</p>
     *
     * @return The graphData
     */
    public Dota2ServerStatsGraph getGraphData() {
        return graphData;
    }

    /**
     * <p>Setter for the field <code>graphData</code>.</p>
     *
     * @param graphData
     *         The graph_data
     */
    public void setGraphData(Dota2ServerStatsGraph graphData) {
        this.graphData = graphData;
    }

    /**
     * <p>isDeltaFrame.</p>
     *
     * @return The deltaFrame
     */
    public boolean isDeltaFrame() {
        return deltaFrame;
    }

    /**
     * <p>Setter for the field <code>deltaFrame</code>.</p>
     *
     * @param deltaFrame
     *         The delta_frame
     */
    public void setDeltaFrame(boolean deltaFrame) {
        this.deltaFrame = deltaFrame;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

}
