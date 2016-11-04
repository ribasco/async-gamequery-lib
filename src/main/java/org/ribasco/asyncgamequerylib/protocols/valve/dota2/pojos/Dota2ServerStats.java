
package org.ribasco.asyncgamequerylib.protocols.valve.dota2.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.List;

@Generated("org.jsonschema2pojo")
public class Dota2ServerStats {

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
     * @return The match
     */
    public Dota2ServerStatsMatch getMatch() {
        return match;
    }

    /**
     * @param match The match
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
     * @param teams The teams
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
     * @param buildings The buildings
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
     * @param graphData The graph_data
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
     * @param deltaFrame The delta_frame
     */
    public void setDeltaFrame(boolean deltaFrame) {
        this.deltaFrame = deltaFrame;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

}
