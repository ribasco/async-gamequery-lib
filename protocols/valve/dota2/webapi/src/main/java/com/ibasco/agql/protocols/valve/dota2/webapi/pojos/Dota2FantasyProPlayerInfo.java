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

package com.ibasco.agql.protocols.valve.dota2.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>Dota2FantasyProPlayerInfo class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class Dota2FantasyProPlayerInfo {
    @SerializedName("account_id")
    private int accountId;
    private String name;
    @SerializedName("country_code")
    private String countryCode;
    @SerializedName("fantasy_role")
    private int fantasyRole;
    @SerializedName("team_id")
    private int teamId;
    @SerializedName("team_name")
    private String teamName;
    @SerializedName("team_tag")
    private String teamTag;
    @SerializedName("is_locked")
    private boolean isLocked;
    @SerializedName("is_pro")
    private boolean isPro;
    @SerializedName("locked_until")
    private long lockedUntil;
    private long timestamp;

    /**
     * <p>Getter for the field <code>accountId</code>.</p>
     *
     * @return a int
     */
    public int getAccountId() {
        return accountId;
    }

    /**
     * <p>Setter for the field <code>accountId</code>.</p>
     *
     * @param accountId a int
     */
    public void setAccountId(int accountId) {
        this.accountId = accountId;
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
     * @param name a {@link java.lang.String} object
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>Getter for the field <code>countryCode</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * <p>Setter for the field <code>countryCode</code>.</p>
     *
     * @param countryCode a {@link java.lang.String} object
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * <p>Getter for the field <code>fantasyRole</code>.</p>
     *
     * @return a int
     */
    public int getFantasyRole() {
        return fantasyRole;
    }

    /**
     * <p>Setter for the field <code>fantasyRole</code>.</p>
     *
     * @param fantasyRole a int
     */
    public void setFantasyRole(int fantasyRole) {
        this.fantasyRole = fantasyRole;
    }

    /**
     * <p>Getter for the field <code>teamId</code>.</p>
     *
     * @return a int
     */
    public int getTeamId() {
        return teamId;
    }

    /**
     * <p>Setter for the field <code>teamId</code>.</p>
     *
     * @param teamId a int
     */
    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    /**
     * <p>Getter for the field <code>teamName</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getTeamName() {
        return teamName;
    }

    /**
     * <p>Setter for the field <code>teamName</code>.</p>
     *
     * @param teamName a {@link java.lang.String} object
     */
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    /**
     * <p>Getter for the field <code>teamTag</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getTeamTag() {
        return teamTag;
    }

    /**
     * <p>Setter for the field <code>teamTag</code>.</p>
     *
     * @param teamTag a {@link java.lang.String} object
     */
    public void setTeamTag(String teamTag) {
        this.teamTag = teamTag;
    }

    /**
     * <p>isLocked.</p>
     *
     * @return a boolean
     */
    public boolean isLocked() {
        return isLocked;
    }

    /**
     * <p>setLocked.</p>
     *
     * @param locked a boolean
     */
    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    /**
     * <p>isPro.</p>
     *
     * @return a boolean
     */
    public boolean isPro() {
        return isPro;
    }

    /**
     * <p>setPro.</p>
     *
     * @param pro a boolean
     */
    public void setPro(boolean pro) {
        isPro = pro;
    }

    /**
     * <p>Getter for the field <code>lockedUntil</code>.</p>
     *
     * @return a long
     */
    public long getLockedUntil() {
        return lockedUntil;
    }

    /**
     * <p>Setter for the field <code>lockedUntil</code>.</p>
     *
     * @param lockedUntil a long
     */
    public void setLockedUntil(long lockedUntil) {
        this.lockedUntil = lockedUntil;
    }

    /**
     * <p>Getter for the field <code>timestamp</code>.</p>
     *
     * @return a long
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * <p>Setter for the field <code>timestamp</code>.</p>
     *
     * @param timestamp a long
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
