package org.ribasco.asyncgamequerylib.protocols.supercell.coc.webapi.pojos;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

public class CocPlayerDetailedInfo extends CocPlayerBasicInfo {

    private int attackWins;
    private int defenseWins;
    private CocClanBasicInfo clan;
    private int bestTrophies;
    private int warStars;
    private int townHallLevel;
    private CocLegendStatistics legendStatistics;
    private List<CocAchievements> achievements;
    private List<CocTroop> troops;
    private List<CocTroop> heroes;
    private List<CocTroop> spells;

    public int getAttackWins() {
        return attackWins;
    }

    public void setAttackWins(int attackWins) {
        this.attackWins = attackWins;
    }

    public int getDefenseWins() {
        return defenseWins;
    }

    public void setDefenseWins(int defenseWins) {
        this.defenseWins = defenseWins;
    }

    public CocClanBasicInfo getClan() {
        return clan;
    }

    public void setClan(CocClanBasicInfo clan) {
        this.clan = clan;
    }

    public int getBestTrophies() {
        return bestTrophies;
    }

    public void setBestTrophies(int bestTrophies) {
        this.bestTrophies = bestTrophies;
    }

    public int getWarStars() {
        return warStars;
    }

    public void setWarStars(int warStars) {
        this.warStars = warStars;
    }

    public int getTownHallLevel() {
        return townHallLevel;
    }

    public void setTownHallLevel(int townHallLevel) {
        this.townHallLevel = townHallLevel;
    }

    public CocLegendStatistics getLegendStatistics() {
        return legendStatistics;
    }

    public void setLegendStatistics(CocLegendStatistics legendStatistics) {
        this.legendStatistics = legendStatistics;
    }

    public List<CocAchievements> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<CocAchievements> achievements) {
        this.achievements = achievements;
    }

    public List<CocTroop> getTroops() {
        return troops;
    }

    public void setTroops(List<CocTroop> troops) {
        this.troops = troops;
    }

    public List<CocTroop> getHeroes() {
        return heroes;
    }

    public void setHeroes(List<CocTroop> heroes) {
        this.heroes = heroes;
    }

    public List<CocTroop> getSpells() {
        return spells;
    }

    public void setSpells(List<CocTroop> spells) {
        this.spells = spells;
    }

    @Override
    public String toString() {
        String superClassStr = super.toString();
        String detailedClass = new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("attackWins", attackWins)
                .append("defenseWins", defenseWins)
                .append("clan", clan)
                .append("bestTrophies", bestTrophies)
                .append("warStars", warStars)
                .append("townHallLevel", townHallLevel)
                .append("legendStatistics", legendStatistics)
                .append("achievements", achievements)
                .append("troops", troops)
                .append("heroes", heroes)
                .append("spells", spells)
                .toString();
        return superClassStr + "," + detailedClass;
    }
}
