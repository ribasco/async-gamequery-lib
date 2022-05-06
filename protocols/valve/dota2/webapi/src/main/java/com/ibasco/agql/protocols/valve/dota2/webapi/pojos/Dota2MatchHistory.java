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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Dota2MatchHistory class.</p>
 *
 * @author Rafael Luis Ibasco
 */
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
    private List<Dota2MatchHistoryInfo> matches = new ArrayList<>();

    /**
     * <p>Getter for the field <code>status</code>.</p>
     *
     * @return The status
     */
    public int getStatus() {
        return status;
    }

    /**
     * <p>Setter for the field <code>status</code>.</p>
     *
     * @param status
     *         The status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * <p>Getter for the field <code>numResults</code>.</p>
     *
     * @return The numResults
     */
    public int getNumResults() {
        return numResults;
    }

    /**
     * <p>Setter for the field <code>numResults</code>.</p>
     *
     * @param numResults
     *         The num_results
     */
    public void setNumResults(int numResults) {
        this.numResults = numResults;
    }

    /**
     * <p>Getter for the field <code>totalResults</code>.</p>
     *
     * @return The totalResults
     */
    public int getTotalResults() {
        return totalResults;
    }

    /**
     * <p>Setter for the field <code>totalResults</code>.</p>
     *
     * @param totalResults
     *         The total_results
     */
    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    /**
     * <p>Getter for the field <code>resultsRemaining</code>.</p>
     *
     * @return The resultsRemaining
     */
    public int getResultsRemaining() {
        return resultsRemaining;
    }

    /**
     * <p>Setter for the field <code>resultsRemaining</code>.</p>
     *
     * @param resultsRemaining
     *         The results_remaining
     */
    public void setResultsRemaining(int resultsRemaining) {
        this.resultsRemaining = resultsRemaining;
    }

    /**
     * <p>Getter for the field <code>matches</code>.</p>
     *
     * @return The matches
     */
    public List<Dota2MatchHistoryInfo> getMatches() {
        return matches;
    }

    /**
     * <p>Setter for the field <code>matches</code>.</p>
     *
     * @param matches
     *         The matches
     */
    public void setMatches(List<Dota2MatchHistoryInfo> matches) {
        this.matches = matches;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
