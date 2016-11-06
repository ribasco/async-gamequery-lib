package org.ribasco.agql.protocols.supercell.coc.webapi.pojos;

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
