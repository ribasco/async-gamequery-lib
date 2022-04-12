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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Dota2TeamDetails class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class Dota2TeamDetails {

    @SerializedName("members")
    @Expose
    private List<Dota2TeamMemberDetails> members = new ArrayList<>();
    @SerializedName("team_id")
    @Expose
    private int teamId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("tag")
    @Expose
    private String tag;
    @SerializedName("time_created")
    @Expose
    private int timeCreated;
    @SerializedName("pro")
    @Expose
    private boolean pro;
    @SerializedName("locked")
    @Expose
    private boolean locked;
    @SerializedName("ugc_logo")
    @Expose
    private String ugcLogo;
    @SerializedName("ugc_base_logo")
    @Expose
    private String ugcBaseLogo;
    @SerializedName("ugc_banner_logo")
    @Expose
    private String ugcBannerLogo;
    @SerializedName("ugc_sponsor_logo")
    @Expose
    private String ugcSponsorLogo;
    @SerializedName("country_code")
    @Expose
    private String countryCode;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("wins")
    @Expose
    private int wins;
    @SerializedName("losses")
    @Expose
    private int losses;
    @SerializedName("rank")
    @Expose
    private int rank;
    @SerializedName("calibration_games_remaining")
    @Expose
    private int calibrationGamesRemaining;
    @SerializedName("games_played_total")
    @Expose
    private int gamesPlayedTotal;
    @SerializedName("games_played_matchmaking")
    @Expose
    private int gamesPlayedMatchmaking;
    @SerializedName("leagues_participated")
    @Expose
    private List<Integer> leaguesParticipated = new ArrayList<>();
    @SerializedName("top_match_ids")
    @Expose
    private List<String> topMatchIds = new ArrayList<>();
    @SerializedName("recent_match_ids")
    @Expose
    private List<String> recentMatchIds = new ArrayList<>();

    /**
     * <p>Getter for the field <code>members</code>.</p>
     *
     * @return The members
     */
    public List<Dota2TeamMemberDetails> getMembers() {
        return members;
    }

    /**
     * <p>Setter for the field <code>members</code>.</p>
     *
     * @param members
     *         The members
     */
    public void setMembers(List<Dota2TeamMemberDetails> members) {
        this.members = members;
    }

    /**
     * <p>Getter for the field <code>teamId</code>.</p>
     *
     * @return The teamId
     */
    public int getTeamId() {
        return teamId;
    }

    /**
     * <p>Setter for the field <code>teamId</code>.</p>
     *
     * @param teamId
     *         The team_id
     */
    public void setTeamId(int teamId) {
        this.teamId = teamId;
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
    public int getTimeCreated() {
        return timeCreated;
    }

    /**
     * <p>Setter for the field <code>timeCreated</code>.</p>
     *
     * @param timeCreated
     *         The time_created
     */
    public void setTimeCreated(int timeCreated) {
        this.timeCreated = timeCreated;
    }

    /**
     * <p>isPro.</p>
     *
     * @return The pro
     */
    public boolean isPro() {
        return pro;
    }

    /**
     * <p>Setter for the field <code>pro</code>.</p>
     *
     * @param pro
     *         The pro
     */
    public void setPro(boolean pro) {
        this.pro = pro;
    }

    /**
     * <p>isLocked.</p>
     *
     * @return The locked
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * <p>Setter for the field <code>locked</code>.</p>
     *
     * @param locked
     *         The locked
     */
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    /**
     * <p>Getter for the field <code>ugcLogo</code>.</p>
     *
     * @return The ugcLogo
     */
    public String getUgcLogo() {
        return ugcLogo;
    }

    /**
     * <p>Setter for the field <code>ugcLogo</code>.</p>
     *
     * @param ugcLogo
     *         The ugc_logo
     */
    public void setUgcLogo(String ugcLogo) {
        this.ugcLogo = ugcLogo;
    }

    /**
     * <p>Getter for the field <code>ugcBaseLogo</code>.</p>
     *
     * @return The ugcBaseLogo
     */
    public String getUgcBaseLogo() {
        return ugcBaseLogo;
    }

    /**
     * <p>Setter for the field <code>ugcBaseLogo</code>.</p>
     *
     * @param ugcBaseLogo
     *         The ugc_base_logo
     */
    public void setUgcBaseLogo(String ugcBaseLogo) {
        this.ugcBaseLogo = ugcBaseLogo;
    }

    /**
     * <p>Getter for the field <code>ugcBannerLogo</code>.</p>
     *
     * @return The ugcBannerLogo
     */
    public String getUgcBannerLogo() {
        return ugcBannerLogo;
    }

    /**
     * <p>Setter for the field <code>ugcBannerLogo</code>.</p>
     *
     * @param ugcBannerLogo
     *         The ugc_banner_logo
     */
    public void setUgcBannerLogo(String ugcBannerLogo) {
        this.ugcBannerLogo = ugcBannerLogo;
    }

    /**
     * <p>Getter for the field <code>ugcSponsorLogo</code>.</p>
     *
     * @return The ugcSponsorLogo
     */
    public String getUgcSponsorLogo() {
        return ugcSponsorLogo;
    }

    /**
     * <p>Setter for the field <code>ugcSponsorLogo</code>.</p>
     *
     * @param ugcSponsorLogo
     *         The ugc_sponsor_logo
     */
    public void setUgcSponsorLogo(String ugcSponsorLogo) {
        this.ugcSponsorLogo = ugcSponsorLogo;
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
     * <p>Getter for the field <code>wins</code>.</p>
     *
     * @return The wins
     */
    public int getWins() {
        return wins;
    }

    /**
     * <p>Setter for the field <code>wins</code>.</p>
     *
     * @param wins
     *         The wins
     */
    public void setWins(int wins) {
        this.wins = wins;
    }

    /**
     * <p>Getter for the field <code>losses</code>.</p>
     *
     * @return The losses
     */
    public int getLosses() {
        return losses;
    }

    /**
     * <p>Setter for the field <code>losses</code>.</p>
     *
     * @param losses
     *         The losses
     */
    public void setLosses(int losses) {
        this.losses = losses;
    }

    /**
     * <p>Getter for the field <code>rank</code>.</p>
     *
     * @return The rank
     */
    public int getRank() {
        return rank;
    }

    /**
     * <p>Setter for the field <code>rank</code>.</p>
     *
     * @param rank
     *         The rank
     */
    public void setRank(int rank) {
        this.rank = rank;
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
     * <p>Getter for the field <code>gamesPlayedTotal</code>.</p>
     *
     * @return The gamesPlayedTotal
     */
    public int getGamesPlayedTotal() {
        return gamesPlayedTotal;
    }

    /**
     * <p>Setter for the field <code>gamesPlayedTotal</code>.</p>
     *
     * @param gamesPlayedTotal
     *         The games_played_total
     */
    public void setGamesPlayedTotal(int gamesPlayedTotal) {
        this.gamesPlayedTotal = gamesPlayedTotal;
    }

    /**
     * <p>Getter for the field <code>gamesPlayedMatchmaking</code>.</p>
     *
     * @return The gamesPlayedMatchmaking
     */
    public int getGamesPlayedMatchmaking() {
        return gamesPlayedMatchmaking;
    }

    /**
     * <p>Setter for the field <code>gamesPlayedMatchmaking</code>.</p>
     *
     * @param gamesPlayedMatchmaking
     *         The games_played_matchmaking
     */
    public void setGamesPlayedMatchmaking(int gamesPlayedMatchmaking) {
        this.gamesPlayedMatchmaking = gamesPlayedMatchmaking;
    }

    /**
     * <p>Getter for the field <code>leaguesParticipated</code>.</p>
     *
     * @return The leaguesParticipated
     */
    public List<Integer> getLeaguesParticipated() {
        return leaguesParticipated;
    }

    /**
     * <p>Setter for the field <code>leaguesParticipated</code>.</p>
     *
     * @param leaguesParticipated
     *         The leagues_participated
     */
    public void setLeaguesParticipated(List<Integer> leaguesParticipated) {
        this.leaguesParticipated = leaguesParticipated;
    }

    /**
     * <p>Getter for the field <code>topMatchIds</code>.</p>
     *
     * @return The topMatchIds
     */
    public List<String> getTopMatchIds() {
        return topMatchIds;
    }

    /**
     * <p>Setter for the field <code>topMatchIds</code>.</p>
     *
     * @param topMatchIds
     *         The top_match_ids
     */
    public void setTopMatchIds(List<String> topMatchIds) {
        this.topMatchIds = topMatchIds;
    }

    /**
     * <p>Getter for the field <code>recentMatchIds</code>.</p>
     *
     * @return The recentMatchIds
     */
    public List<String> getRecentMatchIds() {
        return recentMatchIds;
    }

    /**
     * <p>Setter for the field <code>recentMatchIds</code>.</p>
     *
     * @param recentMatchIds
     *         The recent_match_ids
     */
    public void setRecentMatchIds(List<String> recentMatchIds) {
        this.recentMatchIds = recentMatchIds;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

}
