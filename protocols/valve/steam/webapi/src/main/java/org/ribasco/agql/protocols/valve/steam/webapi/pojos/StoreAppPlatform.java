package org.ribasco.agql.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class StoreAppPlatform {
    @SerializedName("windows")
    private boolean windowsSupported;
    @SerializedName("mac")
    private boolean macSupported;
    @SerializedName("linux")
    private boolean linuxSupported;

    public boolean isWindowsSupported() {
        return windowsSupported;
    }

    public void setWindowsSupported(boolean windowsSupported) {
        this.windowsSupported = windowsSupported;
    }

    public boolean isMacSupported() {
        return macSupported;
    }

    public void setMacSupported(boolean macSupported) {
        this.macSupported = macSupported;
    }

    public boolean isLinuxSupported() {
        return linuxSupported;
    }

    public void setLinuxSupported(boolean linuxSupported) {
        this.linuxSupported = linuxSupported;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("windows", isWindowsSupported())
                .append("mac", isMacSupported())
                .append("linux", isLinuxSupported())
                .toString();
    }
}
