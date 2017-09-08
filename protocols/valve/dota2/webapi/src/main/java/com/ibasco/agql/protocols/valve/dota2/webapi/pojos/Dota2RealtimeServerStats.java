/*
 * MIT License
 *
 * Copyright (c) 2016 Asynchronous Game Query Library
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
