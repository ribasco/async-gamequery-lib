package org.ribasco.asyncgamequerylib.protocols.supercell.coc.webapi.pojos;

public class CocLeagueSeason {
    private String seasonDate;

    public String getSeasonDate() {
        return seasonDate;
    }

    public void setSeasonDate(String seasonDate) {
        this.seasonDate = seasonDate;
    }

    @Override
    public String toString() {
        return seasonDate;
    }
}
