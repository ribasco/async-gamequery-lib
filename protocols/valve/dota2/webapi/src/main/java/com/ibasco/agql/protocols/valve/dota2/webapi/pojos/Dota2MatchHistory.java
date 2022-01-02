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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

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
     * @return The status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status
     *         The status
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
     * @param numResults
     *         The num_results
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
     * @param totalResults
     *         The total_results
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
     * @param resultsRemaining
     *         The results_remaining
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
     * @param matches
     *         The matches
     */
    public void setMatches(List<Dota2MatchHistoryInfo> matches) {
        this.matches = matches;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
