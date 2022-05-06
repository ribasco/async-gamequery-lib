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

package com.ibasco.agql.protocols.supercell.coc.webapi.pojos;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jetbrains.annotations.ApiStatus;

/**
 * <p>CocLegendStatistics class.</p>
 *
 * @author Rafael Luis Ibasco
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
public class CocLegendStatistics {

    private int legendTrophies;

    private CocSeason currentSeason;

    private CocSeason previousSeason;

    private CocSeason bestSeason;

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("legendTrophies", getLegendTrophies())
                .append("currentSeason", getCurrentSeason())
                .append("previousSeason", getPreviousSeason())
                .append("bestSeason", getBestSeason())
                .toString();

    }

    /**
     * <p>Getter for the field <code>legendTrophies</code>.</p>
     *
     * @return a int
     */
    public int getLegendTrophies() {
        return legendTrophies;
    }

    /**
     * <p>Setter for the field <code>legendTrophies</code>.</p>
     *
     * @param legendTrophies
     *         a int
     */
    public void setLegendTrophies(int legendTrophies) {
        this.legendTrophies = legendTrophies;
    }

    /**
     * <p>Getter for the field <code>currentSeason</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocSeason} object
     */
    public CocSeason getCurrentSeason() {
        return currentSeason;
    }

    /**
     * <p>Setter for the field <code>currentSeason</code>.</p>
     *
     * @param currentSeason
     *         a {@link com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocSeason} object
     */
    public void setCurrentSeason(CocSeason currentSeason) {
        this.currentSeason = currentSeason;
    }

    /**
     * <p>Getter for the field <code>previousSeason</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocSeason} object
     */
    public CocSeason getPreviousSeason() {
        return previousSeason;
    }

    /**
     * <p>Setter for the field <code>previousSeason</code>.</p>
     *
     * @param previousSeason
     *         a {@link com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocSeason} object
     */
    public void setPreviousSeason(CocSeason previousSeason) {
        this.previousSeason = previousSeason;
    }

    /**
     * <p>Getter for the field <code>bestSeason</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocSeason} object
     */
    public CocSeason getBestSeason() {
        return bestSeason;
    }

    /**
     * <p>Setter for the field <code>bestSeason</code>.</p>
     *
     * @param bestSeason
     *         a {@link com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocSeason} object
     */
    public void setBestSeason(CocSeason bestSeason) {
        this.bestSeason = bestSeason;
    }
}
