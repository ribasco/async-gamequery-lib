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
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Dota2ServerStatsGraph class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class Dota2ServerStatsGraph {

    @SerializedName("graph_gold")
    private List<Integer> graphGold = new ArrayList<>();

    /**
     * <p>Getter for the field <code>graphGold</code>.</p>
     *
     * @return The graphGold
     */
    public List<Integer> getGraphGold() {
        return graphGold;
    }

    /**
     * <p>Setter for the field <code>graphGold</code>.</p>
     *
     * @param graphGold
     *         The graph_gold
     */
    public void setGraphGold(List<Integer> graphGold) {
        this.graphGold = graphGold;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
