package org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class StoreGameControllerInfo {
    @SerializedName("full_gamepad")
    private boolean fullGamepad;

    public boolean isFullGamepad() {
        return fullGamepad;
    }

    public void setFullGamepad(boolean fullGamepad) {
        this.fullGamepad = fullGamepad;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("fullGamepad", isFullGamepad()).toString();
    }
}
