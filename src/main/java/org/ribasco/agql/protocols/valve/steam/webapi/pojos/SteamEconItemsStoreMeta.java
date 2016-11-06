package org.ribasco.agql.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

public class SteamEconItemsStoreMeta {
    @SerializedName("carousel_data")
    private SteamEconItemsSMCarouselData carouselData;
    private List<SteamEconItemsSMTab> tabs;
    private List<SteamEconItemsSMFilter> filters;
    private SteamEconItemsSMSorting sorting;
    @SerializedName("dropdown_data")
    private SteamEconItemsSMDropdownData dropdownData;
    @SerializedName("player_class_data")
    private List<SteamEconItemsSMPlayerClass> playerClassData;
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
