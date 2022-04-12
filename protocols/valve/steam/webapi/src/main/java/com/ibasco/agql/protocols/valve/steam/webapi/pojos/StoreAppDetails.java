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

package com.ibasco.agql.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>StoreAppDetails class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class StoreAppDetails {
    private String type;
    private String name;
    @SerializedName("steam_appid")
    private int appId;
    @SerializedName("required_age")
    private int requiredAge;
    @SerializedName("is_free")
    private boolean free;
    @SerializedName("controller_support")
    private String controllerSupport;
    @SerializedName("detailed_description")
    private String detailedDescription;
    @SerializedName("about_the_game")
    private String aboutTheGame;
    @SerializedName("short_description")
    private String shortDescription;
    @SerializedName("supported_languages")
    private String supportedLanguages;
    @SerializedName("header_image")
    private String headerImageUrl;
    @SerializedName("website")
    private String websiteUrl;
    @SerializedName("pc_requirements")
    private StoreAppPcRequirements pcRequirements;
    @SerializedName("mac_requirements")
    private StoreAppPcRequirements macRequirements;
    @SerializedName("linux_requirements")
    private StoreAppPcRequirements linuxRequirements;
    private List<String> developers = new ArrayList<>();
    private List<String> publishers = new ArrayList<>();
    @SerializedName("price_overview")
    private StoreAppPriceInfo priceOverview;
    @SerializedName("packages")
    private List<Integer> packageIds = new ArrayList<>();
    @SerializedName("package_groups")
    private List<StoreAppPackageGroup> packageGroups = new ArrayList<>();
    @SerializedName("platforms")
    private StoreAppPlatform platform;
    private StoreAppMetacritic metacritic;
    private List<StoreAppCategory> categories = new ArrayList<>();
    private List<StoreAppGenre> genres = new ArrayList<>();
    private List<StoreAppScreenshots> screenshots = new ArrayList<>();
    private List<StoreAppMovie> movies = new ArrayList<>();
    private StoreAppRecommendations recommendations;
    private StoreAppAchievements achievements;
    @SerializedName("release_date")
    private StoreAppReleaseDateInfo releaseDate;
    @SerializedName("support_info")
    private StoreAppSupportInfo supportInfo;
    @SerializedName("background")
    private String backgroundUrl;

    /**
     * <p>Getter for the field <code>type</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getType() {
        return type;
    }

    /**
     * <p>Setter for the field <code>type</code>.</p>
     *
     * @param type a {@link java.lang.String} object
     */
    public void setType(String type) {
        this.type = type;
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
     * <p>Getter for the field <code>appId</code>.</p>
     *
     * @return a int
     */
    public int getAppId() {
        return appId;
    }

    /**
     * <p>Setter for the field <code>appId</code>.</p>
     *
     * @param appId a int
     */
    public void setAppId(int appId) {
        this.appId = appId;
    }

    /**
     * <p>Getter for the field <code>requiredAge</code>.</p>
     *
     * @return a int
     */
    public int getRequiredAge() {
        return requiredAge;
    }

    /**
     * <p>Setter for the field <code>requiredAge</code>.</p>
     *
     * @param requiredAge a int
     */
    public void setRequiredAge(int requiredAge) {
        this.requiredAge = requiredAge;
    }

    /**
     * <p>isFree.</p>
     *
     * @return a boolean
     */
    public boolean isFree() {
        return free;
    }

    /**
     * <p>Setter for the field <code>free</code>.</p>
     *
     * @param free a boolean
     */
    public void setFree(boolean free) {
        this.free = free;
    }

    /**
     * <p>Getter for the field <code>controllerSupport</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getControllerSupport() {
        return controllerSupport;
    }

    /**
     * <p>Setter for the field <code>controllerSupport</code>.</p>
     *
     * @param controllerSupport a {@link java.lang.String} object
     */
    public void setControllerSupport(String controllerSupport) {
        this.controllerSupport = controllerSupport;
    }

    /**
     * <p>Getter for the field <code>detailedDescription</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getDetailedDescription() {
        return detailedDescription;
    }

    /**
     * <p>Setter for the field <code>detailedDescription</code>.</p>
     *
     * @param detailedDescription a {@link java.lang.String} object
     */
    public void setDetailedDescription(String detailedDescription) {
        this.detailedDescription = detailedDescription;
    }

    /**
     * <p>Getter for the field <code>aboutTheGame</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getAboutTheGame() {
        return aboutTheGame;
    }

    /**
     * <p>Setter for the field <code>aboutTheGame</code>.</p>
     *
     * @param aboutTheGame a {@link java.lang.String} object
     */
    public void setAboutTheGame(String aboutTheGame) {
        this.aboutTheGame = aboutTheGame;
    }

    /**
     * <p>Getter for the field <code>shortDescription</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     * <p>Setter for the field <code>shortDescription</code>.</p>
     *
     * @param shortDescription a {@link java.lang.String} object
     */
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    /**
     * <p>Getter for the field <code>supportedLanguages</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getSupportedLanguages() {
        return supportedLanguages;
    }

    /**
     * <p>Setter for the field <code>supportedLanguages</code>.</p>
     *
     * @param supportedLanguages a {@link java.lang.String} object
     */
    public void setSupportedLanguages(String supportedLanguages) {
        this.supportedLanguages = supportedLanguages;
    }

    /**
     * <p>Getter for the field <code>headerImageUrl</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getHeaderImageUrl() {
        return headerImageUrl;
    }

    /**
     * <p>Setter for the field <code>headerImageUrl</code>.</p>
     *
     * @param headerImageUrl a {@link java.lang.String} object
     */
    public void setHeaderImageUrl(String headerImageUrl) {
        this.headerImageUrl = headerImageUrl;
    }

    /**
     * <p>Getter for the field <code>websiteUrl</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getWebsiteUrl() {
        return websiteUrl;
    }

    /**
     * <p>Setter for the field <code>websiteUrl</code>.</p>
     *
     * @param websiteUrl a {@link java.lang.String} object
     */
    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    /**
     * <p>Getter for the field <code>pcRequirements</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreAppPcRequirements} object
     */
    public StoreAppPcRequirements getPcRequirements() {
        return pcRequirements;
    }

    /**
     * <p>Setter for the field <code>pcRequirements</code>.</p>
     *
     * @param pcRequirements a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreAppPcRequirements} object
     */
    public void setPcRequirements(StoreAppPcRequirements pcRequirements) {
        this.pcRequirements = pcRequirements;
    }

    /**
     * <p>Getter for the field <code>macRequirements</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreAppPcRequirements} object
     */
    public StoreAppPcRequirements getMacRequirements() {
        return macRequirements;
    }

    /**
     * <p>Setter for the field <code>macRequirements</code>.</p>
     *
     * @param macRequirements a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreAppPcRequirements} object
     */
    public void setMacRequirements(StoreAppPcRequirements macRequirements) {
        this.macRequirements = macRequirements;
    }

    /**
     * <p>Getter for the field <code>linuxRequirements</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreAppPcRequirements} object
     */
    public StoreAppPcRequirements getLinuxRequirements() {
        return linuxRequirements;
    }

    /**
     * <p>Setter for the field <code>linuxRequirements</code>.</p>
     *
     * @param linuxRequirements a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreAppPcRequirements} object
     */
    public void setLinuxRequirements(StoreAppPcRequirements linuxRequirements) {
        this.linuxRequirements = linuxRequirements;
    }

    /**
     * <p>Getter for the field <code>developers</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<String> getDevelopers() {
        return developers;
    }

    /**
     * <p>Setter for the field <code>developers</code>.</p>
     *
     * @param developers a {@link java.util.List} object
     */
    public void setDevelopers(List<String> developers) {
        this.developers = developers;
    }

    /**
     * <p>Getter for the field <code>publishers</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<String> getPublishers() {
        return publishers;
    }

    /**
     * <p>Setter for the field <code>publishers</code>.</p>
     *
     * @param publishers a {@link java.util.List} object
     */
    public void setPublishers(List<String> publishers) {
        this.publishers = publishers;
    }

    /**
     * <p>Getter for the field <code>priceOverview</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreAppPriceInfo} object
     */
    public StoreAppPriceInfo getPriceOverview() {
        return priceOverview;
    }

    /**
     * <p>Setter for the field <code>priceOverview</code>.</p>
     *
     * @param priceOverview a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreAppPriceInfo} object
     */
    public void setPriceOverview(StoreAppPriceInfo priceOverview) {
        this.priceOverview = priceOverview;
    }

    /**
     * <p>Getter for the field <code>packageIds</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<Integer> getPackageIds() {
        return packageIds;
    }

    /**
     * <p>Setter for the field <code>packageIds</code>.</p>
     *
     * @param packageIds a {@link java.util.List} object
     */
    public void setPackageIds(List<Integer> packageIds) {
        this.packageIds = packageIds;
    }

    /**
     * <p>Getter for the field <code>packageGroups</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<StoreAppPackageGroup> getPackageGroups() {
        return packageGroups;
    }

    /**
     * <p>Setter for the field <code>packageGroups</code>.</p>
     *
     * @param packageGroups a {@link java.util.List} object
     */
    public void setPackageGroups(List<StoreAppPackageGroup> packageGroups) {
        this.packageGroups = packageGroups;
    }

    /**
     * <p>Getter for the field <code>platform</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreAppPlatform} object
     */
    public StoreAppPlatform getPlatform() {
        return platform;
    }

    /**
     * <p>Setter for the field <code>platform</code>.</p>
     *
     * @param platform a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreAppPlatform} object
     */
    public void setPlatform(StoreAppPlatform platform) {
        this.platform = platform;
    }

    /**
     * <p>Getter for the field <code>metacritic</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreAppMetacritic} object
     */
    public StoreAppMetacritic getMetacritic() {
        return metacritic;
    }

    /**
     * <p>Setter for the field <code>metacritic</code>.</p>
     *
     * @param metacritic a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreAppMetacritic} object
     */
    public void setMetacritic(StoreAppMetacritic metacritic) {
        this.metacritic = metacritic;
    }

    /**
     * <p>Getter for the field <code>categories</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<StoreAppCategory> getCategories() {
        return categories;
    }

    /**
     * <p>Setter for the field <code>categories</code>.</p>
     *
     * @param categories a {@link java.util.List} object
     */
    public void setCategories(List<StoreAppCategory> categories) {
        this.categories = categories;
    }

    /**
     * <p>Getter for the field <code>genres</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<StoreAppGenre> getGenres() {
        return genres;
    }

    /**
     * <p>Setter for the field <code>genres</code>.</p>
     *
     * @param genres a {@link java.util.List} object
     */
    public void setGenres(List<StoreAppGenre> genres) {
        this.genres = genres;
    }

    /**
     * <p>Getter for the field <code>screenshots</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<StoreAppScreenshots> getScreenshots() {
        return screenshots;
    }

    /**
     * <p>Setter for the field <code>screenshots</code>.</p>
     *
     * @param screenshots a {@link java.util.List} object
     */
    public void setScreenshots(List<StoreAppScreenshots> screenshots) {
        this.screenshots = screenshots;
    }

    /**
     * <p>Getter for the field <code>movies</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<StoreAppMovie> getMovies() {
        return movies;
    }

    /**
     * <p>Setter for the field <code>movies</code>.</p>
     *
     * @param movies a {@link java.util.List} object
     */
    public void setMovies(List<StoreAppMovie> movies) {
        this.movies = movies;
    }

    /**
     * <p>Getter for the field <code>recommendations</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreAppRecommendations} object
     */
    public StoreAppRecommendations getRecommendations() {
        return recommendations;
    }

    /**
     * <p>Setter for the field <code>recommendations</code>.</p>
     *
     * @param recommendations a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreAppRecommendations} object
     */
    public void setRecommendations(StoreAppRecommendations recommendations) {
        this.recommendations = recommendations;
    }

    /**
     * <p>Getter for the field <code>achievements</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreAppAchievements} object
     */
    public StoreAppAchievements getAchievements() {
        return achievements;
    }

    /**
     * <p>Setter for the field <code>achievements</code>.</p>
     *
     * @param achievements a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreAppAchievements} object
     */
    public void setAchievements(StoreAppAchievements achievements) {
        this.achievements = achievements;
    }

    /**
     * <p>Getter for the field <code>releaseDate</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreAppReleaseDateInfo} object
     */
    public StoreAppReleaseDateInfo getReleaseDate() {
        return releaseDate;
    }

    /**
     * <p>Setter for the field <code>releaseDate</code>.</p>
     *
     * @param releaseDate a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreAppReleaseDateInfo} object
     */
    public void setReleaseDate(StoreAppReleaseDateInfo releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     * <p>Getter for the field <code>supportInfo</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreAppSupportInfo} object
     */
    public StoreAppSupportInfo getSupportInfo() {
        return supportInfo;
    }

    /**
     * <p>Setter for the field <code>supportInfo</code>.</p>
     *
     * @param supportInfo a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreAppSupportInfo} object
     */
    public void setSupportInfo(StoreAppSupportInfo supportInfo) {
        this.supportInfo = supportInfo;
    }

    /**
     * <p>Getter for the field <code>backgroundUrl</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    /**
     * <p>Setter for the field <code>backgroundUrl</code>.</p>
     *
     * @param backgroundUrl a {@link java.lang.String} object
     */
    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append("type", getType())
                .append("name", getName())
                .append("appid", getAppId())
                .append("requiredAge", getRequiredAge())
                .append("isFree", isFree())
                .append("controllerSupport", getControllerSupport())
                .toString();
    }
}
