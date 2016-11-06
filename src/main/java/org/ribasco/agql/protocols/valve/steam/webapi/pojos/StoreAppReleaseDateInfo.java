package org.ribasco.agql.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

//TODO: Add a custom type adapter for this class
public class StoreAppReleaseDateInfo {
    @SerializedName("coming_soon")
    private boolean comingSoon;
    @SerializedName("date")
    private String releaseDate;

    public boolean isComingSoon() {
        return comingSoon;
    }

    public void setComingSoon(boolean comingSoon) {
        this.comingSoon = comingSoon;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("comingSoon", isComingSoon())
                .append("releaseDate", getReleaseDate())
                .toString();
    }
}
