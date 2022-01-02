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

import java.util.ArrayList;
import java.util.List;

public class CocPlayerDetailedInfo extends CocPlayerBasicInfo {

    private int attackWins;
    private int defenseWins;
    private CocClanBasicInfo clan;
    private int bestTrophies;
    private int warStars;
    private int townHallLevel;
    private CocLegendStatistics legendStatistics;
    private List<CocAchievements> achievements = new ArrayList<>();
    private List<CocTroop> troops = new ArrayList<>();
    private List<CocTroop> heroes = new ArrayList<>();
    private List<CocTroop> spells = new ArrayList<>();

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
