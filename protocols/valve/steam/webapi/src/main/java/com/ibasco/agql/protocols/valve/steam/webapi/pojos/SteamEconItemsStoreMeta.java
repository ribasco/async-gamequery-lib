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
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

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

    public SteamEconItemsSMCarouselData getCarouselData() {
        return carouselData;
    }

    public void setCarouselData(SteamEconItemsSMCarouselData carouselData) {
        this.carouselData = carouselData;
    }

    public List<SteamEconItemsSMTab> getTabs() {
        return tabs;
    }

    public void setTabs(List<SteamEconItemsSMTab> tabs) {
        this.tabs = tabs;
    }

    public List<SteamEconItemsSMFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<SteamEconItemsSMFilter> filters) {
        this.filters = filters;
    }

    public SteamEconItemsSMSorting getSorting() {
        return sorting;
    }

    public void setSorting(SteamEconItemsSMSorting sorting) {
        this.sorting = sorting;
    }

    public SteamEconItemsSMDropdownData getDropdownData() {
        return dropdownData;
    }

    public void setDropdownData(SteamEconItemsSMDropdownData dropdownData) {
        this.dropdownData = dropdownData;
    }

    public List<SteamEconItemsSMPlayerClass> getPlayerClassData() {
        return playerClassData;
    }

    public void setPlayerClassData(List<SteamEconItemsSMPlayerClass> playerClassData) {
        this.playerClassData = playerClassData;
    }

    public SteamEconItemsSMHomepage getHomePageData() {
        return homePageData;
    }

    public void setHomePageData(SteamEconItemsSMHomepage homePageData) {
        this.homePageData = homePageData;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
