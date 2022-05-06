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

import com.google.gson.annotations.SerializedName;
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
public class CocPlayerBasicInfo {

    private String tag;

    private String name;

    private String role;

    private String expLevel;

    private CocLeague league;

    private int trophies;

    private int clanRank;

    private int previousClanRank;

    @SerializedName("donations")
    private int totalDonations;

    @SerializedName("donationsReceived")
    private int totalDonationsReceived;

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

    /**
     * <p>Getter for the field <code>clanRank</code>.</p>
     *
     * @return a int
     */
    public int getClanRank() {
        return clanRank;
    }

    /**
     * <p>Setter for the field <code>clanRank</code>.</p>
     *
     * @param clanRank
     *         a int
     */
    public void setClanRank(int clanRank) {
        this.clanRank = clanRank;
    }

    /**
     * <p>Getter for the field <code>previousClanRank</code>.</p>
     *
     * @return a int
     */
    public int getPreviousClanRank() {
        return previousClanRank;
    }

    /**
     * <p>Setter for the field <code>previousClanRank</code>.</p>
     *
     * @param previousClanRank
     *         a int
     */
    public void setPreviousClanRank(int previousClanRank) {
        this.previousClanRank = previousClanRank;
    }

    /**
     * <p>Getter for the field <code>totalDonations</code>.</p>
     *
     * @return a int
     */
    public int getTotalDonations() {
        return totalDonations;
    }

    /**
     * <p>Setter for the field <code>totalDonations</code>.</p>
     *
     * @param totalDonations
     *         a int
     */
    public void setTotalDonations(int totalDonations) {
        this.totalDonations = totalDonations;
    }

    /**
     * <p>Getter for the field <code>totalDonationsReceived</code>.</p>
     *
     * @return a int
     */
    public int getTotalDonationsReceived() {
        return totalDonationsReceived;
    }

    /**
     * <p>Setter for the field <code>totalDonationsReceived</code>.</p>
     *
     * @param totalDonationsReceived
     *         a int
     */
    public void setTotalDonationsReceived(int totalDonationsReceived) {
        this.totalDonationsReceived = totalDonationsReceived;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return superStringBuilder().toString();
    }

    /**
     * <p>superStringBuilder.</p>
     *
     * @return a {@link org.apache.commons.lang3.builder.ToStringBuilder} object
     */
    protected ToStringBuilder superStringBuilder() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("name", getName())
                .append("tag", getTag())
                .append("role", getRole())
                .append("trophies", getTrophies())
                .append("expLevel", getExpLevel());
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
     * <p>Getter for the field <code>role</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getRole() {
        return role;
    }

    /**
     * <p>Setter for the field <code>role</code>.</p>
     *
     * @param role
     *         a {@link java.lang.String} object
     */
    public void setRole(String role) {
        this.role = role;
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
     * <p>Getter for the field <code>expLevel</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getExpLevel() {
        return expLevel;
    }

    /**
     * <p>Setter for the field <code>expLevel</code>.</p>
     *
     * @param expLevel
     *         a {@link java.lang.String} object
     */
    public void setExpLevel(String expLevel) {
        this.expLevel = expLevel;
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
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name
     *         a {@link java.lang.String} object
     */
    public void setName(String name) {
        this.name = name;
    }
}
