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

package com.ibasco.agql.protocols.valve.dota2.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

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

    public List<Integer> getLeagueIds() {
        return leagueIds;
    }

    public void setLeagueIds(List<Integer> leagueIds) {
        this.leagueIds = leagueIds;
    }

    public List<Long> getPlayerAccountIds() {
        return playerAccountIds;
    }

    public void setPlayerAccountIds(List<Long> playerAccountIds) {
        this.playerAccountIds = playerAccountIds;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *         The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * @param tag
     *         The tag
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * @return The timeCreated
     */
    public long getTimeCreated() {
        return timeCreated;
    }

    /**
     * @param timeCreated
     *         The time_created
     */
    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    /**
     * @return The calibrationGamesRemaining
     */
    public int getCalibrationGamesRemaining() {
        return calibrationGamesRemaining;
    }

    /**
     * @param calibrationGamesRemaining
     *         The calibration_games_remaining
     */
    public void setCalibrationGamesRemaining(int calibrationGamesRemaining) {
        this.calibrationGamesRemaining = calibrationGamesRemaining;
    }

    /**
     * @return The logo
     */
    public long getLogo() {
        return logo;
    }

    /**
     * @param logo
     *         The logo
     */
    public void setLogo(long logo) {
        this.logo = logo;
    }

    /**
     * @return The logoSponsor
     */
    public long getLogoSponsor() {
        return logoSponsor;
    }

    /**
     * @param logoSponsor
     *         The logo_sponsor
     */
    public void setLogoSponsor(long logoSponsor) {
        this.logoSponsor = logoSponsor;
    }

    /**
     * @return The countryCode
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * @param countryCode
     *         The country_code
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * @return The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     *         The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return The gamesPlayed
     */
    public int getGamesPlayed() {
        return gamesPlayed;
    }

    /**
     * @param gamesPlayed
     *         The games_played
     */
    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    /**
     * @return The adminAccountId
     */
    public long getAdminAccountId() {
        return adminAccountId;
    }

    /**
     * @param adminAccountId
     *         The admin_account_id
     */
    public void setAdminAccountId(long adminAccountId) {
        this.adminAccountId = adminAccountId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
