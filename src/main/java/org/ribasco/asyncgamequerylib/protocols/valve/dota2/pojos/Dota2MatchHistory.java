
package org.ribasco.asyncgamequerylib.protocols.valve.dota2.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.List;

@Generated("org.jsonschema2pojo")
public class Dota2MatchHistory {
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("num_results")
    @Expose
    private int numResults;
    @SerializedName("total_results")
    @Expose
    private int totalResults;
    @SerializedName("results_remaining")
    @Expose
    private int resultsRemaining;
    @SerializedName("matches")
    @Expose
    private List<Dota2MatchHistoryInfo> matches = new ArrayList<Dota2MatchHistoryInfo>();

    /**
     * @return The status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return The numResults
     */
    public int getNumResults() {
        return numResults;
    }

    /**
     * @param numResults The num_results
     */
    public void setNumResults(int numResults) {
        this.numResults = numResults;
    }

    /**
     * @return The totalResults
     */
    public int getTotalResults() {
        return totalResults;
    }

    /**
     * @param totalResults The total_results
     */
    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    /**
     * @return The resultsRemaining
     */
    public int getResultsRemaining() {
        return resultsRemaining;
    }

    /**
     * @param resultsRemaining The results_remaining
     */
    public void setResultsRemaining(int resultsRemaining) {
        this.resultsRemaining = resultsRemaining;
    }

    /**
     * @return The matches
     */
    public List<Dota2MatchHistoryInfo> getMatches() {
        return matches;
    }

    /**
     * @param matches The matches
     */
    public void setMatches(List<Dota2MatchHistoryInfo> matches) {
        this.matches = matches;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
