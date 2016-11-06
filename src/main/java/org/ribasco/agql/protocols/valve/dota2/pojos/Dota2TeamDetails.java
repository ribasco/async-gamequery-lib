
package org.ribasco.agql.protocols.valve.dota2.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

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
     * @return The members
     */
    public List<Dota2TeamMemberDetails> getMembers() {
        return members;
    }

    /**
     * @param members The members
     */
    public void setMembers(List<Dota2TeamMemberDetails> members) {
        this.members = members;
    }

    /**
     * @return The teamId
     */
    public int getTeamId() {
        return teamId;
    }

    /**
     * @param teamId The team_id
     */
    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
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
     * @param tag The tag
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * @return The timeCreated
     */
    public int getTimeCreated() {
        return timeCreated;
    }

    /**
     * @param timeCreated The time_created
     */
    public void setTimeCreated(int timeCreated) {
        this.timeCreated = timeCreated;
    }

    /**
     * @return The pro
     */
    public boolean isPro() {
        return pro;
    }

    /**
     * @param pro The pro
     */
    public void setPro(boolean pro) {
        this.pro = pro;
    }

    /**
     * @return The locked
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * @param locked The locked
     */
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    /**
     * @return The ugcLogo
     */
    public String getUgcLogo() {
        return ugcLogo;
    }

    /**
     * @param ugcLogo The ugc_logo
     */
    public void setUgcLogo(String ugcLogo) {
        this.ugcLogo = ugcLogo;
    }

    /**
     * @return The ugcBaseLogo
     */
    public String getUgcBaseLogo() {
        return ugcBaseLogo;
    }

    /**
     * @param ugcBaseLogo The ugc_base_logo
     */
    public void setUgcBaseLogo(String ugcBaseLogo) {
        this.ugcBaseLogo = ugcBaseLogo;
    }

    /**
     * @return The ugcBannerLogo
     */
    public String getUgcBannerLogo() {
        return ugcBannerLogo;
    }

    /**
     * @param ugcBannerLogo The ugc_banner_logo
     */
    public void setUgcBannerLogo(String ugcBannerLogo) {
        this.ugcBannerLogo = ugcBannerLogo;
    }

    /**
     * @return The ugcSponsorLogo
     */
    public String getUgcSponsorLogo() {
        return ugcSponsorLogo;
    }

    /**
     * @param ugcSponsorLogo The ugc_sponsor_logo
     */
    public void setUgcSponsorLogo(String ugcSponsorLogo) {
        this.ugcSponsorLogo = ugcSponsorLogo;
    }

    /**
     * @return The countryCode
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * @param countryCode The country_code
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
     * @param url The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return The wins
     */
    public int getWins() {
        return wins;
    }

    /**
     * @param wins The wins
     */
    public void setWins(int wins) {
        this.wins = wins;
    }

    /**
     * @return The losses
     */
    public int getLosses() {
        return losses;
    }

    /**
     * @param losses The losses
     */
    public void setLosses(int losses) {
        this.losses = losses;
    }

    /**
     * @return The rank
     */
    public int getRank() {
        return rank;
    }

    /**
     * @param rank The rank
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * @return The calibrationGamesRemaining
     */
    public int getCalibrationGamesRemaining() {
        return calibrationGamesRemaining;
    }

    /**
     * @param calibrationGamesRemaining The calibration_games_remaining
     */
    public void setCalibrationGamesRemaining(int calibrationGamesRemaining) {
        this.calibrationGamesRemaining = calibrationGamesRemaining;
    }

    /**
     * @return The gamesPlayedTotal
     */
    public int getGamesPlayedTotal() {
        return gamesPlayedTotal;
    }

    /**
     * @param gamesPlayedTotal The games_played_total
     */
    public void setGamesPlayedTotal(int gamesPlayedTotal) {
        this.gamesPlayedTotal = gamesPlayedTotal;
    }

    /**
     * @return The gamesPlayedMatchmaking
     */
    public int getGamesPlayedMatchmaking() {
        return gamesPlayedMatchmaking;
    }

    /**
     * @param gamesPlayedMatchmaking The games_played_matchmaking
     */
    public void setGamesPlayedMatchmaking(int gamesPlayedMatchmaking) {
        this.gamesPlayedMatchmaking = gamesPlayedMatchmaking;
    }

    /**
     * @return The leaguesParticipated
     */
    public List<Integer> getLeaguesParticipated() {
        return leaguesParticipated;
    }

    /**
     * @param leaguesParticipated The leagues_participated
     */
    public void setLeaguesParticipated(List<Integer> leaguesParticipated) {
        this.leaguesParticipated = leaguesParticipated;
    }

    /**
     * @return The topMatchIds
     */
    public List<String> getTopMatchIds() {
        return topMatchIds;
    }

    /**
     * @param topMatchIds The top_match_ids
     */
    public void setTopMatchIds(List<String> topMatchIds) {
        this.topMatchIds = topMatchIds;
    }

    /**
     * @return The recentMatchIds
     */
    public List<String> getRecentMatchIds() {
        return recentMatchIds;
    }

    /**
     * @param recentMatchIds The recent_match_ids
     */
    public void setRecentMatchIds(List<String> recentMatchIds) {
        this.recentMatchIds = recentMatchIds;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

}
