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

package com.ibasco.agql.protocols.supercell.coc.webapi.pojos;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class CocLegendStatistics {
    private int legendTrophies;
    private CocSeason currentSeason;
    private CocSeason previousSeason;
    private CocSeason bestSeason;

    public int getLegendTrophies() {
        return legendTrophies;
    }

    public void setLegendTrophies(int legendTrophies) {
        this.legendTrophies = legendTrophies;
    }

    public CocSeason getCurrentSeason() {
        return currentSeason;
    }

    public void setCurrentSeason(CocSeason currentSeason) {
        this.currentSeason = currentSeason;
    }

    public CocSeason getPreviousSeason() {
        return previousSeason;
    }

    public void setPreviousSeason(CocSeason previousSeason) {
        this.previousSeason = previousSeason;
    }

    public CocSeason getBestSeason() {
        return bestSeason;
    }

    public void setBestSeason(CocSeason bestSeason) {
        this.bestSeason = bestSeason;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("legendTrophies", getLegendTrophies())
                .append("currentSeason", getCurrentSeason())
                .append("previousSeason", getPreviousSeason())
                .append("bestSeason", getBestSeason())
                .toString();

    }
}
