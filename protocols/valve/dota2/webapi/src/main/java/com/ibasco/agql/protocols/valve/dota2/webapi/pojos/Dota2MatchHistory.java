/*
 * MIT License
 *
 * Copyright (c) 2018 Asynchronous Game Query Library
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
