package org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

public class SteamEconItemsSMSortingPrefab {
    private long id;
    private String name;
    @SerializedName("url_history_param_name")
    private String urlHistoryParamName;
    @SerializedName("sorter_ids")
    private List<SteamEconItemsSMSorterId> sorterIds;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlHistoryParamName() {
        return urlHistoryParamName;
    }

    public void setUrlHistoryParamName(String urlHistoryParamName) {
        this.urlHistoryParamName = urlHistoryParamName;
    }

    public List<SteamEconItemsSMSorterId> getSorterIds() {
        return sorterIds;
    }

    public void setSorterIds(List<SteamEconItemsSMSorterId> sorterIds) {
        this.sorterIds = sorterIds;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
