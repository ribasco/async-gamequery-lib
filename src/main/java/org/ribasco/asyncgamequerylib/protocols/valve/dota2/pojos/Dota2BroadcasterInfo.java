package org.ribasco.asyncgamequerylib.protocols.valve.dota2.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Dota2BroadcasterInfo {
    @SerializedName("account_id")
    private long accountId;

    //TODO: Use a type adapter to convert this to a long type
    @SerializedName("server_steam_id")
    private String serverSteamId;

    private boolean live;

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getServerSteamId() {
        return serverSteamId;
    }

    public void setServerSteamId(String serverSteamId) {
        this.serverSteamId = serverSteamId;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
