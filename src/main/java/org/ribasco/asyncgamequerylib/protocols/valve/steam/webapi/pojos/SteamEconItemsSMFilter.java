package org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

public class SteamEconItemsSMFilter {
    private long id;
    private String name;
    @SerializedName("url_history_param_name")
    private String urlHistoryParamName;
    private SteamEconItemsSMFilterElement allElement;
    /**
     * A list of elements within the filter.
     */
    private List<SteamEconItemsSMFilterElement> elements;
    private int count;

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

    public SteamEconItemsSMFilterElement getAllElement() {
        return allElement;
    }

    public void setAllElement(SteamEconItemsSMFilterElement allElement) {
        this.allElement = allElement;
    }

    public List<SteamEconItemsSMFilterElement> getElements() {
        return elements;
    }

    public void setElements(List<SteamEconItemsSMFilterElement> elements) {
        this.elements = elements;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
