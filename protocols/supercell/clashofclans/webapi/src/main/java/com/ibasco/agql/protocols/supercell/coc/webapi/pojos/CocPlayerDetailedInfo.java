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

import org.jetbrains.annotations.ApiStatus;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>CocPlayerDetailedInfo class.</p>
 *
 * @author Rafael Luis Ibasco
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
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

    /**
     * <p>Getter for the field <code>attackWins</code>.</p>
     *
     * @return a int
     */
    public int getAttackWins() {
        return attackWins;
    }

    /**
     * <p>Setter for the field <code>attackWins</code>.</p>
     *
     * @param attackWins
     *         a int
     */
    public void setAttackWins(int attackWins) {
        this.attackWins = attackWins;
    }

    /**
     * <p>Getter for the field <code>defenseWins</code>.</p>
     *
     * @return a int
     */
    public int getDefenseWins() {
        return defenseWins;
    }

    /**
     * <p>Setter for the field <code>defenseWins</code>.</p>
     *
     * @param defenseWins
     *         a int
     */
    public void setDefenseWins(int defenseWins) {
        this.defenseWins = defenseWins;
    }

    /**
     * <p>Getter for the field <code>clan</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocClanBasicInfo} object
     */
    public CocClanBasicInfo getClan() {
        return clan;
    }

    /**
     * <p>Setter for the field <code>clan</code>.</p>
     *
     * @param clan
     *         a {@link com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocClanBasicInfo} object
     */
    public void setClan(CocClanBasicInfo clan) {
        this.clan = clan;
    }

    /**
     * <p>Getter for the field <code>bestTrophies</code>.</p>
     *
     * @return a int
     */
    public int getBestTrophies() {
        return bestTrophies;
    }

    /**
     * <p>Setter for the field <code>bestTrophies</code>.</p>
     *
     * @param bestTrophies
     *         a int
     */
    public void setBestTrophies(int bestTrophies) {
        this.bestTrophies = bestTrophies;
    }

    /**
     * <p>Getter for the field <code>warStars</code>.</p>
     *
     * @return a int
     */
    public int getWarStars() {
        return warStars;
    }

    /**
     * <p>Setter for the field <code>warStars</code>.</p>
     *
     * @param warStars
     *         a int
     */
    public void setWarStars(int warStars) {
        this.warStars = warStars;
    }

    /**
     * <p>Getter for the field <code>townHallLevel</code>.</p>
     *
     * @return a int
     */
    public int getTownHallLevel() {
        return townHallLevel;
    }

    /**
     * <p>Setter for the field <code>townHallLevel</code>.</p>
     *
     * @param townHallLevel
     *         a int
     */
    public void setTownHallLevel(int townHallLevel) {
        this.townHallLevel = townHallLevel;
    }

    /**
     * <p>Getter for the field <code>legendStatistics</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocLegendStatistics} object
     */
    public CocLegendStatistics getLegendStatistics() {
        return legendStatistics;
    }

    /**
     * <p>Setter for the field <code>legendStatistics</code>.</p>
     *
     * @param legendStatistics
     *         a {@link com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocLegendStatistics} object
     */
    public void setLegendStatistics(CocLegendStatistics legendStatistics) {
        this.legendStatistics = legendStatistics;
    }

    /**
     * <p>Getter for the field <code>achievements</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<CocAchievements> getAchievements() {
        return achievements;
    }

    /**
     * <p>Setter for the field <code>achievements</code>.</p>
     *
     * @param achievements
     *         a {@link java.util.List} object
     */
    public void setAchievements(List<CocAchievements> achievements) {
        this.achievements = achievements;
    }

    /**
     * <p>Getter for the field <code>troops</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<CocTroop> getTroops() {
        return troops;
    }

    /**
     * <p>Setter for the field <code>troops</code>.</p>
     *
     * @param troops
     *         a {@link java.util.List} object
     */
    public void setTroops(List<CocTroop> troops) {
        this.troops = troops;
    }

    /**
     * <p>Getter for the field <code>heroes</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<CocTroop> getHeroes() {
        return heroes;
    }

    /**
     * <p>Setter for the field <code>heroes</code>.</p>
     *
     * @param heroes
     *         a {@link java.util.List} object
     */
    public void setHeroes(List<CocTroop> heroes) {
        this.heroes = heroes;
    }

    /**
     * <p>Getter for the field <code>spells</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<CocTroop> getSpells() {
        return spells;
    }

    /**
     * <p>Setter for the field <code>spells</code>.</p>
     *
     * @param spells
     *         a {@link java.util.List} object
     */
    public void setSpells(List<CocTroop> spells) {
        this.spells = spells;
    }

    /** {@inheritDoc} */
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
