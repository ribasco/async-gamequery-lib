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

/**
 * <p>StoreFeaturedCategories class.</p>
 *
 * @author Rafael Luis Ibasco
 */
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

    /**
     * <p>Getter for the field <code>genres</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreFeaturedCategory} object
     */
    public StoreFeaturedCategory<StoreFeaturedAppInfo> getGenres() {
        return genres;
    }

    /**
     * <p>Setter for the field <code>genres</code>.</p>
     *
     * @param genres
     *         a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreFeaturedCategory} object
     */
    public void setGenres(StoreFeaturedCategory<StoreFeaturedAppInfo> genres) {
        this.genres = genres;
    }

    /**
     * <p>Getter for the field <code>status</code>.</p>
     *
     * @return a int
     */
    public int getStatus() {
        return status;
    }

    /**
     * <p>Setter for the field <code>status</code>.</p>
     *
     * @param status
     *         a int
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /** {@inheritDoc} */
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

    /**
     * <p>Getter for the field <code>firstSpotlightItem</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreFeaturedCategory} object
     */
    public StoreFeaturedCategory<StoreAppSpotlightInfo> getFirstSpotlightItem() {
        return firstSpotlightItem;
    }

    /**
     * <p>Setter for the field <code>firstSpotlightItem</code>.</p>
     *
     * @param firstSpotlightItem
     *         a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreFeaturedCategory} object
     */
    public void setFirstSpotlightItem(StoreFeaturedCategory<StoreAppSpotlightInfo> firstSpotlightItem) {
        this.firstSpotlightItem = firstSpotlightItem;
    }

    /**
     * <p>Getter for the field <code>secondSpotlightItem</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreFeaturedCategory} object
     */
    public StoreFeaturedCategory<StoreAppSpotlightInfo> getSecondSpotlightItem() {
        return secondSpotlightItem;
    }

    /**
     * <p>Setter for the field <code>secondSpotlightItem</code>.</p>
     *
     * @param secondSpotlightItem
     *         a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreFeaturedCategory} object
     */
    public void setSecondSpotlightItem(StoreFeaturedCategory<StoreAppSpotlightInfo> secondSpotlightItem) {
        this.secondSpotlightItem = secondSpotlightItem;
    }

    /**
     * <p>Getter for the field <code>dailyDeals</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreFeaturedCategory} object
     */
    public StoreFeaturedCategory<StoreDailyDealsInfo> getDailyDeals() {
        return dailyDeals;
    }

    /**
     * <p>Setter for the field <code>dailyDeals</code>.</p>
     *
     * @param dailyDeals
     *         a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreFeaturedCategory} object
     */
    public void setDailyDeals(StoreFeaturedCategory<StoreDailyDealsInfo> dailyDeals) {
        this.dailyDeals = dailyDeals;
    }

    /**
     * <p>Getter for the field <code>specials</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreFeaturedCategory} object
     */
    public StoreFeaturedCategory<StoreFeaturedAppInfo> getSpecials() {
        return specials;
    }

    /**
     * <p>Setter for the field <code>specials</code>.</p>
     *
     * @param specials
     *         a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreFeaturedCategory} object
     */
    public void setSpecials(StoreFeaturedCategory<StoreFeaturedAppInfo> specials) {
        this.specials = specials;
    }

    /**
     * <p>Getter for the field <code>comingSoon</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreFeaturedCategory} object
     */
    public StoreFeaturedCategory<StoreFeaturedAppInfo> getComingSoon() {
        return comingSoon;
    }

    /**
     * <p>Setter for the field <code>comingSoon</code>.</p>
     *
     * @param comingSoon
     *         a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreFeaturedCategory} object
     */
    public void setComingSoon(StoreFeaturedCategory<StoreFeaturedAppInfo> comingSoon) {
        this.comingSoon = comingSoon;
    }

    /**
     * <p>Getter for the field <code>topSellers</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreFeaturedCategory} object
     */
    public StoreFeaturedCategory<StoreFeaturedAppInfo> getTopSellers() {
        return topSellers;
    }

    /**
     * <p>Setter for the field <code>topSellers</code>.</p>
     *
     * @param topSellers
     *         a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreFeaturedCategory} object
     */
    public void setTopSellers(StoreFeaturedCategory<StoreFeaturedAppInfo> topSellers) {
        this.topSellers = topSellers;
    }

    /**
     * <p>Getter for the field <code>newReleases</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreFeaturedCategory} object
     */
    public StoreFeaturedCategory<StoreFeaturedAppInfo> getNewReleases() {
        return newReleases;
    }

    /**
     * <p>Setter for the field <code>newReleases</code>.</p>
     *
     * @param newReleases
     *         a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreFeaturedCategory} object
     */
    public void setNewReleases(StoreFeaturedCategory<StoreFeaturedAppInfo> newReleases) {
        this.newReleases = newReleases;
    }

    /**
     * <p>Getter for the field <code>trailerSlideShows</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreFeaturedCategory} object
     */
    public StoreFeaturedCategory<StoreFeaturedAppInfo> getTrailerSlideShows() {
        return trailerSlideShows;
    }

    /**
     * <p>Setter for the field <code>trailerSlideShows</code>.</p>
     *
     * @param trailerSlideShows
     *         a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreFeaturedCategory} object
     */
    public void setTrailerSlideShows(StoreFeaturedCategory<StoreFeaturedAppInfo> trailerSlideShows) {
        this.trailerSlideShows = trailerSlideShows;
    }
}
