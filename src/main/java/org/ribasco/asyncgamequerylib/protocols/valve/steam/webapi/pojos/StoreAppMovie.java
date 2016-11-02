package org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;

public class StoreAppMovie {
    private int id;
    private String name;
    @SerializedName("thumbnail")
    private String thumbnailUrl;
    @SerializedName("webm")
    private StoreAppWebMFormatInfo webMFormatInfo;
    private boolean highlight;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public StoreAppWebMFormatInfo getWebMFormatInfo() {
        return webMFormatInfo;
    }

    public void setWebMFormatInfo(StoreAppWebMFormatInfo webMFormatInfo) {
        this.webMFormatInfo = webMFormatInfo;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }
}
