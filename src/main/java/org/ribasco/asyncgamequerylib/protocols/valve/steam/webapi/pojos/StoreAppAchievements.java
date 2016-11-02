package org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoreAppAchievements {
    private int total;
    @SerializedName("highlighted")
    private List<StoreAppAchievementHighlight> achievementHighlights;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<StoreAppAchievementHighlight> getAchievementHighlights() {
        return achievementHighlights;
    }

    public void setAchievementHighlights(List<StoreAppAchievementHighlight> achievementHighlights) {
        this.achievementHighlights = achievementHighlights;
    }
}
