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
import java.util.List;

public class GameServerAccount {

    @SerializedName("servers")
    @Expose
    private List<GameServerAccountDetail> servers = null;

    @SerializedName("is_banned")
    @Expose
    private Boolean isBanned;

    @SerializedName("expires")
    @Expose
    private Integer expires;

    @SerializedName("actor")
    @Expose
    private String actor;

    @SerializedName("last_action_time")
    @Expose
    private Integer lastActionTime;

    public List<GameServerAccountDetail> getServers() {
        return servers;
    }

    public void setServers(List<GameServerAccountDetail> servers) {
        this.servers = servers;
    }

    public Boolean getIsBanned() {
        return isBanned;
    }

    public void setIsBanned(Boolean isBanned) {
        this.isBanned = isBanned;
    }

    public Integer getExpires() {
        return expires;
    }

    public void setExpires(Integer expires) {
        this.expires = expires;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public Integer getLastActionTime() {
        return lastActionTime;
    }

    public void setLastActionTime(Integer lastActionTime) {
        this.lastActionTime = lastActionTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(GameServerAccount.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("servers");
        sb.append('=');
        sb.append(((this.servers == null) ? "<null>" : this.servers));
        sb.append(',');
        sb.append("isBanned");
        sb.append('=');
        sb.append(((this.isBanned == null) ? "<null>" : this.isBanned));
        sb.append(',');
        sb.append("expires");
        sb.append('=');
        sb.append(((this.expires == null) ? "<null>" : this.expires));
        sb.append(',');
        sb.append("actor");
        sb.append('=');
        sb.append(((this.actor == null) ? "<null>" : this.actor));
        sb.append(',');
        sb.append("lastActionTime");
        sb.append('=');
        sb.append(((this.lastActionTime == null) ? "<null>" : this.lastActionTime));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}