package org.ribasco.agql.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

public class SteamEconItemsSMCarouselData {
    @SerializedName("max_display_banners")
    private int maxDisplayBanners;
    private List<SteamEconItemsCarouselBanner> banners;

    public int getMaxDisplayBanners() {
        return maxDisplayBanners;
    }

    public void setMaxDisplayBanners(int maxDisplayBanners) {
        this.maxDisplayBanners = maxDisplayBanners;
    }

    public List<SteamEconItemsCarouselBanner> getBanners() {
        return banners;
    }

    public void setBanners(List<SteamEconItemsCarouselBanner> banners) {
        this.banners = banners;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
