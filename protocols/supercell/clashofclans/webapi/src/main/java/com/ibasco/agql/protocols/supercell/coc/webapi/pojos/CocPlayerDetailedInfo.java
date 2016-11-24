/*
 * MIT License
 *
 * Copyright (c) 2016 Asynchronous Game Query Library
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

package com.ibasco.agql.protocols.supercell.coc.webapi.pojos;

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
        return superStringBuilder().append("attackWins", attackWins)
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
    }
}
