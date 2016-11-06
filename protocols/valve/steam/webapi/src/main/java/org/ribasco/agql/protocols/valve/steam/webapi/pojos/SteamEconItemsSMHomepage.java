package org.ribasco.agql.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

public class SteamEconItemsSMHomepage {
    @SerializedName("home_category_id")
    private long categoryId;
    @SerializedName("popular_items")
    private List<SteamEconItemsSMPopularItems> popularItems;

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public List<SteamEconItemsSMPopularItems> getPopularItems() {
        return popularItems;
    }

    public void setPopularItems(List<SteamEconItemsSMPopularItems> popularItems) {
        this.popularItems = popularItems;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
