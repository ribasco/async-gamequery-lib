
package org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class Dota2ServerStatsGraph {

    @SerializedName("graph_gold")
    private List<Integer> graphGold = new ArrayList<>();

    /**
     * @return The graphGold
     */
    public List<Integer> getGraphGold() {
        return graphGold;
    }

    /**
     * @param graphGold The graph_gold
     */
    public void setGraphGold(List<Integer> graphGold) {
        this.graphGold = graphGold;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
