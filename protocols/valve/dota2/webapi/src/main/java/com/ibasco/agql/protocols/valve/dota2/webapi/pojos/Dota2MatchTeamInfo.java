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
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Dota2MatchTeamInfo class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class Dota2MatchTeamInfo {
    @SerializedName("name")
    private String name;

    @SerializedName("tag")
    private String tag;

    @SerializedName("time_created")
    private long timeCreated;

    @SerializedName("calibration_games_remaining")
    private int calibrationGamesRemaining;

    @SerializedName("logo")
    private long logo;

    @SerializedName("logo_sponsor")
    private long logoSponsor;

    @SerializedName("country_code")
    private String countryCode;

    @SerializedName("url")
    private String url;

    @SerializedName("games_played")
    private int gamesPlayed;

    @SerializedName("admin_account_id")
    private long adminAccountId;

    private List<Integer> leagueIds = new ArrayList<>();

    private List<Long> playerAccountIds = new ArrayList<>();

    /**
     * <p>Getter for the field <code>leagueIds</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<Integer> getLeagueIds() {
        return leagueIds;
    }

    /**
     * <p>Setter for the field <code>leagueIds</code>.</p>
     *
     * @param leagueIds a {@link java.util.List} object
     */
    public void setLeagueIds(List<Integer> leagueIds) {
        this.leagueIds = leagueIds;
    }

    /**
     * <p>Getter for the field <code>playerAccountIds</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<Long> getPlayerAccountIds() {
        return playerAccountIds;
    }

    /**
     * <p>Setter for the field <code>playerAccountIds</code>.</p>
     *
     * @param playerAccountIds a {@link java.util.List} object
     */
    public void setPlayerAccountIds(List<Long> playerAccountIds) {
        this.playerAccountIds = playerAccountIds;
    }

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name
     *         The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>Getter for the field <code>tag</code>.</p>
     *
     * @return The tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * <p>Setter for the field <code>tag</code>.</p>
     *
     * @param tag
     *         The tag
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * <p>Getter for the field <code>timeCreated</code>.</p>
     *
     * @return The timeCreated
     */
    public long getTimeCreated() {
        return timeCreated;
    }

    /**
     * <p>Setter for the field <code>timeCreated</code>.</p>
     *
     * @param timeCreated
     *         The time_created
     */
    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    /**
     * <p>Getter for the field <code>calibrationGamesRemaining</code>.</p>
     *
     * @return The calibrationGamesRemaining
     */
    public int getCalibrationGamesRemaining() {
        return calibrationGamesRemaining;
    }

    /**
     * <p>Setter for the field <code>calibrationGamesRemaining</code>.</p>
     *
     * @param calibrationGamesRemaining
     *         The calibration_games_remaining
     */
    public void setCalibrationGamesRemaining(int calibrationGamesRemaining) {
        this.calibrationGamesRemaining = calibrationGamesRemaining;
    }

    /**
     * <p>Getter for the field <code>logo</code>.</p>
     *
     * @return The logo
     */
    public long getLogo() {
        return logo;
    }

    /**
     * <p>Setter for the field <code>logo</code>.</p>
     *
     * @param logo
     *         The logo
     */
    public void setLogo(long logo) {
        this.logo = logo;
    }

    /**
     * <p>Getter for the field <code>logoSponsor</code>.</p>
     *
     * @return The logoSponsor
     */
    public long getLogoSponsor() {
        return logoSponsor;
    }

    /**
     * <p>Setter for the field <code>logoSponsor</code>.</p>
     *
     * @param logoSponsor
     *         The logo_sponsor
     */
    public void setLogoSponsor(long logoSponsor) {
        this.logoSponsor = logoSponsor;
    }

    /**
     * <p>Getter for the field <code>countryCode</code>.</p>
     *
     * @return The countryCode
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * <p>Setter for the field <code>countryCode</code>.</p>
     *
     * @param countryCode
     *         The country_code
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * <p>Getter for the field <code>url</code>.</p>
     *
     * @return The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * <p>Setter for the field <code>url</code>.</p>
     *
     * @param url
     *         The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * <p>Getter for the field <code>gamesPlayed</code>.</p>
     *
     * @return The gamesPlayed
     */
    public int getGamesPlayed() {
        return gamesPlayed;
    }

    /**
     * <p>Setter for the field <code>gamesPlayed</code>.</p>
     *
     * @param gamesPlayed
     *         The games_played
     */
    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    /**
     * <p>Getter for the field <code>adminAccountId</code>.</p>
     *
     * @return The adminAccountId
     */
    public long getAdminAccountId() {
        return adminAccountId;
    }

    /**
     * <p>Setter for the field <code>adminAccountId</code>.</p>
     *
     * @param adminAccountId
     *         The admin_account_id
     */
    public void setAdminAccountId(long adminAccountId) {
        this.adminAccountId = adminAccountId;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
