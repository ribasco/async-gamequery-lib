package org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;

public class StoreAppScreenshots {
    private int id;
    @SerializedName("path_thumbnail")
    private String thumbnailPath;
    @SerializedName("path_full")
    private String fullPath;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }
}
