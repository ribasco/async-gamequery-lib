/*
 * Copyright (c) 2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.protocols.valve.steam.webapi.interfaces.gameservers.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GameServerAccountDetail {

    @SerializedName("steamid")
    @Expose
    private String steamid;

    @SerializedName("appid")
    @Expose
    private Integer appid;

    @SerializedName("login_token")
    @Expose
    private String loginToken;

    @SerializedName("memo")
    @Expose
    private String memo;

    @SerializedName("is_deleted")
    @Expose
    private Boolean isDeleted;

    @SerializedName("is_expired")
    @Expose
    private Boolean isExpired;

    @SerializedName("rt_last_logon")
    @Expose
    private Integer rtLastLogon;

    public String getSteamid() {
        return steamid;
    }

    public void setSteamid(String steamid) {
        this.steamid = steamid;
    }

    public Integer getAppid() {
        return appid;
    }

    public void setAppid(Integer appid) {
        this.appid = appid;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Boolean getIsExpired() {
        return isExpired;
    }

    public void setIsExpired(Boolean isExpired) {
        this.isExpired = isExpired;
    }

    public Integer getRtLastLogon() {
        return rtLastLogon;
    }

    public void setRtLastLogon(Integer rtLastLogon) {
        this.rtLastLogon = rtLastLogon;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(GameServerAccountDetail.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("steamid");
        sb.append('=');
        sb.append(((this.steamid == null) ? "<null>" : this.steamid));
        sb.append(',');
        sb.append("appid");
        sb.append('=');
        sb.append(((this.appid == null) ? "<null>" : this.appid));
        sb.append(',');
        sb.append("loginToken");
        sb.append('=');
        sb.append(((this.loginToken == null) ? "<null>" : this.loginToken));
        sb.append(',');
        sb.append("memo");
        sb.append('=');
        sb.append(((this.memo == null) ? "<null>" : this.memo));
        sb.append(',');
        sb.append("isDeleted");
        sb.append('=');
        sb.append(((this.isDeleted == null) ? "<null>" : this.isDeleted));
        sb.append(',');
        sb.append("isExpired");
        sb.append('=');
        sb.append(((this.isExpired == null) ? "<null>" : this.isExpired));
        sb.append(',');
        sb.append("rtLastLogon");
        sb.append('=');
        sb.append(((this.rtLastLogon == null) ? "<null>" : this.rtLastLogon));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}