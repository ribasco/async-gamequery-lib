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
