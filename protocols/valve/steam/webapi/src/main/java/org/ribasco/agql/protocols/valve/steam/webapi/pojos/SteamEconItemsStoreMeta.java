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
