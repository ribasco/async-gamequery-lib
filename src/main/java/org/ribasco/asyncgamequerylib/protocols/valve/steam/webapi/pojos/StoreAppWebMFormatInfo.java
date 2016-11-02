package org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;

public class StoreAppWebMFormatInfo {
    @SerializedName("480")
    private String lowResUrl;
    @SerializedName("max")
    private String maxResUrl;

    public String getLowResUrl() {
        return lowResUrl;
    }

    public void setLowResUrl(String lowResUrl) {
        this.lowResUrl = lowResUrl;
    }

    public String getMaxResUrl() {
        return maxResUrl;
    }

    public void setMaxResUrl(String maxResUrl) {
        this.maxResUrl = maxResUrl;
    }
}
