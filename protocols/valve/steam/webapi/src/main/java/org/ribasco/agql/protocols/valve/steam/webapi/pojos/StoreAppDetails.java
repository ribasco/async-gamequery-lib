/*
 * MIT License
 *
 * Copyright (c) 2016 Asynchronous Game Query Library
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.ribasco.agql.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

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
    private List<String> developers;
    private List<String> publishers;
    @SerializedName("price_overview")
    private StoreAppPriceInfo priceOverview;
    @SerializedName("packages")
    private List<Integer> packageIds;
    @SerializedName("package_groups")
    private List<StoreAppPackageGroup> packageGroups;
    @SerializedName("platforms")
    private StoreAppPlatform platform;
    private StoreAppMetacritic metacritic;
    private List<StoreAppCategory> categories;
    private List<StoreAppGenre> genres;
    private List<StoreAppScreenshots> screenshots;
    private List<StoreAppMovie> movies;
    private StoreAppRecommendations recommendations;
    private StoreAppAchievements achievements;
    @SerializedName("release_date")
    private StoreAppReleaseDateInfo releaseDate;
    @SerializedName("support_info")
    private StoreAppSupportInfo supportInfo;
    @SerializedName("background")
    private String backgroundUrl;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public int getRequiredAge() {
        return requiredAge;
    }

    public void setRequiredAge(int requiredAge) {
        this.requiredAge = requiredAge;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public String getControllerSupport() {
        return controllerSupport;
    }

    public void setControllerSupport(String controllerSupport) {
        this.controllerSupport = controllerSupport;
    }

    public String getDetailedDescription() {
        return detailedDescription;
    }

    public void setDetailedDescription(String detailedDescription) {
        this.detailedDescription = detailedDescription;
    }

    public String getAboutTheGame() {
        return aboutTheGame;
    }

    public void setAboutTheGame(String aboutTheGame) {
        this.aboutTheGame = aboutTheGame;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getSupportedLanguages() {
        return supportedLanguages;
    }

    public void setSupportedLanguages(String supportedLanguages) {
        this.supportedLanguages = supportedLanguages;
    }

    public String getHeaderImageUrl() {
        return headerImageUrl;
    }

    public void setHeaderImageUrl(String headerImageUrl) {
        this.headerImageUrl = headerImageUrl;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public StoreAppPcRequirements getPcRequirements() {
        return pcRequirements;
    }

    public void setPcRequirements(StoreAppPcRequirements pcRequirements) {
        this.pcRequirements = pcRequirements;
    }

    public StoreAppPcRequirements getMacRequirements() {
        return macRequirements;
    }

    public void setMacRequirements(StoreAppPcRequirements macRequirements) {
        this.macRequirements = macRequirements;
    }

    public StoreAppPcRequirements getLinuxRequirements() {
        return linuxRequirements;
    }

    public void setLinuxRequirements(StoreAppPcRequirements linuxRequirements) {
        this.linuxRequirements = linuxRequirements;
    }

    public List<String> getDevelopers() {
        return developers;
    }

    public void setDevelopers(List<String> developers) {
        this.developers = developers;
    }

    public List<String> getPublishers() {
        return publishers;
    }

    public void setPublishers(List<String> publishers) {
        this.publishers = publishers;
    }

    public StoreAppPriceInfo getPriceOverview() {
        return priceOverview;
    }

    public void setPriceOverview(StoreAppPriceInfo priceOverview) {
        this.priceOverview = priceOverview;
    }

    public List<Integer> getPackageIds() {
        return packageIds;
    }

    public void setPackageIds(List<Integer> packageIds) {
        this.packageIds = packageIds;
    }

    public List<StoreAppPackageGroup> getPackageGroups() {
        return packageGroups;
    }

    public void setPackageGroups(List<StoreAppPackageGroup> packageGroups) {
        this.packageGroups = packageGroups;
    }

    public StoreAppPlatform getPlatform() {
        return platform;
    }

    public void setPlatform(StoreAppPlatform platform) {
        this.platform = platform;
    }

    public StoreAppMetacritic getMetacritic() {
        return metacritic;
    }

    public void setMetacritic(StoreAppMetacritic metacritic) {
        this.metacritic = metacritic;
    }

    public List<StoreAppCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<StoreAppCategory> categories) {
        this.categories = categories;
    }

    public List<StoreAppGenre> getGenres() {
        return genres;
    }

    public void setGenres(List<StoreAppGenre> genres) {
        this.genres = genres;
    }

    public List<StoreAppScreenshots> getScreenshots() {
        return screenshots;
    }

    public void setScreenshots(List<StoreAppScreenshots> screenshots) {
        this.screenshots = screenshots;
    }

    public List<StoreAppMovie> getMovies() {
        return movies;
    }

    public void setMovies(List<StoreAppMovie> movies) {
        this.movies = movies;
    }

    public StoreAppRecommendations getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(StoreAppRecommendations recommendations) {
        this.recommendations = recommendations;
    }

    public StoreAppAchievements getAchievements() {
        return achievements;
    }

    public void setAchievements(StoreAppAchievements achievements) {
        this.achievements = achievements;
    }

    public StoreAppReleaseDateInfo getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(StoreAppReleaseDateInfo releaseDate) {
        this.releaseDate = releaseDate;
    }

    public StoreAppSupportInfo getSupportInfo() {
        return supportInfo;
    }

    public void setSupportInfo(StoreAppSupportInfo supportInfo) {
        this.supportInfo = supportInfo;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("type", getType())
                .append("name", getName())
                .append("appid", getAppId())
                .append("requiredAge", getRequiredAge())
                .append("isFree", isFree())
                .append("controllerSupport", getControllerSupport())
                .toString();
    }
}
