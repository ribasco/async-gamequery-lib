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

package com.ibasco.agql.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class StoreFeaturedCategories {
    @SerializedName("0")
    private StoreFeaturedCategory<StoreAppSpotlightInfo> firstSpotlightItem;
    @SerializedName("1")
    private StoreFeaturedCategory<StoreAppSpotlightInfo> secondSpotlightItem;
    @SerializedName("2")
    private StoreFeaturedCategory<StoreDailyDealsInfo> dailyDeals;
    private StoreFeaturedCategory<StoreFeaturedAppInfo> specials;
    @SerializedName("coming_soon")
    private StoreFeaturedCategory<StoreFeaturedAppInfo> comingSoon;
    @SerializedName("top_sellers")
    private StoreFeaturedCategory<StoreFeaturedAppInfo> topSellers;
    @SerializedName("new_releases")
    private StoreFeaturedCategory<StoreFeaturedAppInfo> newReleases;
    private StoreFeaturedCategory<StoreFeaturedAppInfo> genres;
    @SerializedName("trailerslideshow")
    private StoreFeaturedCategory<StoreFeaturedAppInfo> trailerSlideShows;
    private int status;

    public StoreFeaturedCategory<StoreAppSpotlightInfo> getFirstSpotlightItem() {
        return firstSpotlightItem;
    }

    public void setFirstSpotlightItem(StoreFeaturedCategory<StoreAppSpotlightInfo> firstSpotlightItem) {
        this.firstSpotlightItem = firstSpotlightItem;
    }

    public StoreFeaturedCategory<StoreAppSpotlightInfo> getSecondSpotlightItem() {
        return secondSpotlightItem;
    }

    public void setSecondSpotlightItem(StoreFeaturedCategory<StoreAppSpotlightInfo> secondSpotlightItem) {
        this.secondSpotlightItem = secondSpotlightItem;
    }

    public StoreFeaturedCategory<StoreDailyDealsInfo> getDailyDeals() {
        return dailyDeals;
    }

    public void setDailyDeals(StoreFeaturedCategory<StoreDailyDealsInfo> dailyDeals) {
        this.dailyDeals = dailyDeals;
    }

    public StoreFeaturedCategory<StoreFeaturedAppInfo> getSpecials() {
        return specials;
    }

    public void setSpecials(StoreFeaturedCategory<StoreFeaturedAppInfo> specials) {
        this.specials = specials;
    }

    public StoreFeaturedCategory<StoreFeaturedAppInfo> getComingSoon() {
        return comingSoon;
    }

    public void setComingSoon(StoreFeaturedCategory<StoreFeaturedAppInfo> comingSoon) {
        this.comingSoon = comingSoon;
    }

    public StoreFeaturedCategory<StoreFeaturedAppInfo> getTopSellers() {
        return topSellers;
    }

    public void setTopSellers(StoreFeaturedCategory<StoreFeaturedAppInfo> topSellers) {
        this.topSellers = topSellers;
    }

    public StoreFeaturedCategory<StoreFeaturedAppInfo> getNewReleases() {
        return newReleases;
    }

    public void setNewReleases(StoreFeaturedCategory<StoreFeaturedAppInfo> newReleases) {
        this.newReleases = newReleases;
    }

    public StoreFeaturedCategory<StoreFeaturedAppInfo> getGenres() {
        return genres;
    }

    public void setGenres(StoreFeaturedCategory<StoreFeaturedAppInfo> genres) {
        this.genres = genres;
    }

    public StoreFeaturedCategory<StoreFeaturedAppInfo> getTrailerSlideShows() {
        return trailerSlideShows;
    }

    public void setTrailerSlideShows(StoreFeaturedCategory<StoreFeaturedAppInfo> trailerSlideShows) {
        this.trailerSlideShows = trailerSlideShows;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("firstSpotlightItem", getFirstSpotlightItem())
                .append("secondSpotlightItem", getSecondSpotlightItem())
                .append("dailyDeals", getDailyDeals())
                .append("specials", getSpecials())
                .append("comingSoon", getComingSoon())
                .append("topSellers", getTopSellers())
                .append("newReleases", getNewReleases())
                .append("trailerSlideShows", getTrailerSlideShows())
                .toString();
    }
}
