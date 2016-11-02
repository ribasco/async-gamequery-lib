package org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

public class SteamEconItemsSMSorting {
    private List<SteamEconItemsSMSorter> sorters;
    @SerializedName("sorting_prefabs")
    private List<SteamEconItemsSMSortingPrefab> sortingPrefabs;

    public List<SteamEconItemsSMSorter> getSorters() {
        return sorters;
    }

    public void setSorters(List<SteamEconItemsSMSorter> sorters) {
        this.sorters = sorters;
    }

    public List<SteamEconItemsSMSortingPrefab> getSortingPrefabs() {
        return sortingPrefabs;
    }

    public void setSortingPrefabs(List<SteamEconItemsSMSortingPrefab> sortingPrefabs) {
        this.sortingPrefabs = sortingPrefabs;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
