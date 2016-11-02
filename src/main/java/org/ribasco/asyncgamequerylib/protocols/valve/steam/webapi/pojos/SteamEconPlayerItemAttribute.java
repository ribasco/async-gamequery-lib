package org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SteamEconPlayerItemAttribute<T> {
    @SerializedName("defindex")
    private int defIndex;
    private T value;
    @SerializedName("float_value")
    private float floatValue;
    @SerializedName("account_info")
    private SteamEconPlayerAccountInfo accountInfo;

    public int getDefIndex() {
        return defIndex;
    }

    public void setDefIndex(int defIndex) {
        this.defIndex = defIndex;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public float getFloatValue() {
        return floatValue;
    }

    public void setFloatValue(float floatValue) {
        this.floatValue = floatValue;
    }

    public SteamEconPlayerAccountInfo getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(SteamEconPlayerAccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("defindex", getDefIndex())
                .append("value", getValue())
                .append("floatValue", getFloatValue())
                .append("accountInfo", getAccountInfo())
                .toString();
    }
}
