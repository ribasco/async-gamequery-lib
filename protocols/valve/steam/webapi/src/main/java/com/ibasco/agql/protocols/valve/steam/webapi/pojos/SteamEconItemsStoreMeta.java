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
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>SteamEconItemsStoreMeta class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SteamEconItemsStoreMeta {

    @SerializedName("carousel_data")
    private SteamEconItemsSMCarouselData carouselData;

    private List<SteamEconItemsSMTab> tabs = new ArrayList<>();

    private List<SteamEconItemsSMFilter> filters = new ArrayList<>();

    private SteamEconItemsSMSorting sorting;

    @SerializedName("dropdown_data")
    private SteamEconItemsSMDropdownData dropdownData;

    @SerializedName("player_class_data")
    private List<SteamEconItemsSMPlayerClass> playerClassData = new ArrayList<>();

    @SerializedName("home_page_data")
    private SteamEconItemsSMHomepage homePageData;

    /**
     * <p>Getter for the field <code>carouselData</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamEconItemsSMCarouselData} object
     */
    public SteamEconItemsSMCarouselData getCarouselData() {
        return carouselData;
    }

    /**
     * <p>Setter for the field <code>carouselData</code>.</p>
     *
     * @param carouselData
     *         a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamEconItemsSMCarouselData} object
     */
    public void setCarouselData(SteamEconItemsSMCarouselData carouselData) {
        this.carouselData = carouselData;
    }

    /**
     * <p>Getter for the field <code>tabs</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<SteamEconItemsSMTab> getTabs() {
        return tabs;
    }

    /**
     * <p>Setter for the field <code>tabs</code>.</p>
     *
     * @param tabs
     *         a {@link java.util.List} object
     */
    public void setTabs(List<SteamEconItemsSMTab> tabs) {
        this.tabs = tabs;
    }

    /**
     * <p>Getter for the field <code>filters</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<SteamEconItemsSMFilter> getFilters() {
        return filters;
    }

    /**
     * <p>Setter for the field <code>filters</code>.</p>
     *
     * @param filters
     *         a {@link java.util.List} object
     */
    public void setFilters(List<SteamEconItemsSMFilter> filters) {
        this.filters = filters;
    }

    /**
     * <p>Getter for the field <code>sorting</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamEconItemsSMSorting} object
     */
    public SteamEconItemsSMSorting getSorting() {
        return sorting;
    }

    /**
     * <p>Setter for the field <code>sorting</code>.</p>
     *
     * @param sorting
     *         a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamEconItemsSMSorting} object
     */
    public void setSorting(SteamEconItemsSMSorting sorting) {
        this.sorting = sorting;
    }

    /**
     * <p>Getter for the field <code>dropdownData</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamEconItemsSMDropdownData} object
     */
    public SteamEconItemsSMDropdownData getDropdownData() {
        return dropdownData;
    }

    /**
     * <p>Setter for the field <code>dropdownData</code>.</p>
     *
     * @param dropdownData
     *         a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamEconItemsSMDropdownData} object
     */
    public void setDropdownData(SteamEconItemsSMDropdownData dropdownData) {
        this.dropdownData = dropdownData;
    }

    /**
     * <p>Getter for the field <code>playerClassData</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<SteamEconItemsSMPlayerClass> getPlayerClassData() {
        return playerClassData;
    }

    /**
     * <p>Setter for the field <code>playerClassData</code>.</p>
     *
     * @param playerClassData
     *         a {@link java.util.List} object
     */
    public void setPlayerClassData(List<SteamEconItemsSMPlayerClass> playerClassData) {
        this.playerClassData = playerClassData;
    }

    /**
     * <p>Getter for the field <code>homePageData</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamEconItemsSMHomepage} object
     */
    public SteamEconItemsSMHomepage getHomePageData() {
        return homePageData;
    }

    /**
     * <p>Setter for the field <code>homePageData</code>.</p>
     *
     * @param homePageData
     *         a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamEconItemsSMHomepage} object
     */
    public void setHomePageData(SteamEconItemsSMHomepage homePageData) {
        this.homePageData = homePageData;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
