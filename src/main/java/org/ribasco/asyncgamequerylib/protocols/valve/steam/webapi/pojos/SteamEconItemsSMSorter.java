package org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SteamEconItemsSMSorter {
    private long id;
    private String name;
    @SerializedName("data_type")
    private String dataType;
    @SerializedName("sort_field")
    private String sortField;
    @SerializedName("sort_reversed")
    private boolean sortReversed;
    @SerializedName("localized_text")
    private String localizedText;

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

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public boolean isSortReversed() {
        return sortReversed;
    }

    public void setSortReversed(boolean sortReversed) {
        this.sortReversed = sortReversed;
    }

    public String getLocalizedText() {
        return localizedText;
    }

    public void setLocalizedText(String localizedText) {
        this.localizedText = localizedText;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
