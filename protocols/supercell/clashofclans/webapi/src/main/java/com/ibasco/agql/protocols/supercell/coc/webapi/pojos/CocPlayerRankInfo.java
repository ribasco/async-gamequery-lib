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
 * Created by raffy on 10/28/2016.
 *
 * @author Rafael Luis Ibasco
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
public class CocPlayerRankInfo {

    private String tag;

    private String name;

    private int expLevel;

    private int trophies;

    private int attackWins;

    private int defenseWins;

    private int rank;

    private int previousRank;

    private CocClanBasicInfo clan;

    private CocLeague league;

    /**
     * <p>Getter for the field <code>expLevel</code>.</p>
     *
     * @return a int
     */
    public int getExpLevel() {
        return expLevel;
    }

    /**
     * <p>Setter for the field <code>expLevel</code>.</p>
     *
     * @param expLevel
     *         a int
     */
    public void setExpLevel(int expLevel) {
        this.expLevel = expLevel;
    }

    /**
     * <p>Getter for the field <code>trophies</code>.</p>
     *
     * @return a int
     */
    public int getTrophies() {
        return trophies;
    }

    /**
     * <p>Setter for the field <code>trophies</code>.</p>
     *
     * @param trophies
     *         a int
     */
    public void setTrophies(int trophies) {
        this.trophies = trophies;
    }

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
     * <p>Getter for the field <code>league</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocLeague} object
     */
    public CocLeague getLeague() {
        return league;
    }

    /**
     * <p>Setter for the field <code>league</code>.</p>
     *
     * @param league
     *         a {@link com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocLeague} object
     */
    public void setLeague(CocLeague league) {
        this.league = league;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("tag", getTag())
                .append("name", getName())
                .append("clan", getClan())
                .append("rank", getRank())
                .append("previousRank", getPreviousRank())
                .toString();
    }

    /**
     * <p>Getter for the field <code>tag</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getTag() {
        return tag;
    }

    /**
     * <p>Setter for the field <code>tag</code>.</p>
     *
     * @param tag
     *         a {@link java.lang.String} object
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name
     *         a {@link java.lang.String} object
     */
    public void setName(String name) {
        this.name = name;
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
     * <p>Getter for the field <code>rank</code>.</p>
     *
     * @return a int
     */
    public int getRank() {
        return rank;
    }

    /**
     * <p>Setter for the field <code>rank</code>.</p>
     *
     * @param rank
     *         a int
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * <p>Getter for the field <code>previousRank</code>.</p>
     *
     * @return a int
     */
    public int getPreviousRank() {
        return previousRank;
    }

    /**
     * <p>Setter for the field <code>previousRank</code>.</p>
     *
     * @param previousRank
     *         a int
     */
    public void setPreviousRank(int previousRank) {
        this.previousRank = previousRank;
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
}
