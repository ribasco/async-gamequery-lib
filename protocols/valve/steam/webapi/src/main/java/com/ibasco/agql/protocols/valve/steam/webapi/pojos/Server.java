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

package com.ibasco.agql.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class Server {

    @SerializedName("addr")
    @Expose
    private String addr;

    @SerializedName("gameport")
    @Expose
    private Integer gameport;

    @SerializedName("steamid")
    @Expose
    private String steamid;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("appid")
    @Expose
    private Integer appid;

    @SerializedName("gamedir")
    @Expose
    private String gamedir;

    @SerializedName("version")
    @Expose
    private String version;

    @SerializedName("product")
    @Expose
    private String product;

    @SerializedName("region")
    @Expose
    private Integer region;

    @SerializedName("players")
    @Expose
    private Integer players;

    @SerializedName("max_players")
    @Expose
    private Integer maxPlayers;

    @SerializedName("bots")
    @Expose
    private Integer bots;

    @SerializedName("map")
    @Expose
    private String map;

    @SerializedName("secure")
    @Expose
    private Boolean secure;

    @SerializedName("dedicated")
    @Expose
    private Boolean dedicated;

    @SerializedName("os")
    @Expose
    private String os;

    @SerializedName("gametype")
    @Expose
    private String gametype;

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Integer getGameport() {
        return gameport;
    }

    public void setGameport(Integer gameport) {
        this.gameport = gameport;
    }

    public String getSteamid() {
        return steamid;
    }

    public void setSteamid(String steamid) {
        this.steamid = steamid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAppid() {
        return appid;
    }

    public void setAppid(Integer appid) {
        this.appid = appid;
    }

    public String getGamedir() {
        return gamedir;
    }

    public void setGamedir(String gamedir) {
        this.gamedir = gamedir;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Integer getRegion() {
        return region;
    }

    public void setRegion(Integer region) {
        this.region = region;
    }

    public Integer getPlayers() {
        return players;
    }

    public void setPlayers(Integer players) {
        this.players = players;
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public Integer getBots() {
        return bots;
    }

    public void setBots(Integer bots) {
        this.bots = bots;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public Boolean getSecure() {
        return secure;
    }

    public void setSecure(Boolean secure) {
        this.secure = secure;
    }

    public Boolean getDedicated() {
        return dedicated;
    }

    public void setDedicated(Boolean dedicated) {
        this.dedicated = dedicated;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getGametype() {
        return gametype;
    }

    public void setGametype(String gametype) {
        this.gametype = gametype;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Server.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("addr");
        sb.append('=');
        sb.append(((this.addr == null) ? "<null>" : this.addr));
        sb.append(',');
        sb.append("gameport");
        sb.append('=');
        sb.append(((this.gameport == null) ? "<null>" : this.gameport));
        sb.append(',');
        sb.append("steamid");
        sb.append('=');
        sb.append(((this.steamid == null) ? "<null>" : this.steamid));
        sb.append(',');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null) ? "<null>" : this.name));
        sb.append(',');
        sb.append("appid");
        sb.append('=');
        sb.append(((this.appid == null) ? "<null>" : this.appid));
        sb.append(',');
        sb.append("gamedir");
        sb.append('=');
        sb.append(((this.gamedir == null) ? "<null>" : this.gamedir));
        sb.append(',');
        sb.append("version");
        sb.append('=');
        sb.append(((this.version == null) ? "<null>" : this.version));
        sb.append(',');
        sb.append("product");
        sb.append('=');
        sb.append(((this.product == null) ? "<null>" : this.product));
        sb.append(',');
        sb.append("region");
        sb.append('=');
        sb.append(((this.region == null) ? "<null>" : this.region));
        sb.append(',');
        sb.append("players");
        sb.append('=');
        sb.append(((this.players == null) ? "<null>" : this.players));
        sb.append(',');
        sb.append("maxPlayers");
        sb.append('=');
        sb.append(((this.maxPlayers == null) ? "<null>" : this.maxPlayers));
        sb.append(',');
        sb.append("bots");
        sb.append('=');
        sb.append(((this.bots == null) ? "<null>" : this.bots));
        sb.append(',');
        sb.append("map");
        sb.append('=');
        sb.append(((this.map == null) ? "<null>" : this.map));
        sb.append(',');
        sb.append("secure");
        sb.append('=');
        sb.append(((this.secure == null) ? "<null>" : this.secure));
        sb.append(',');
        sb.append("dedicated");
        sb.append('=');
        sb.append(((this.dedicated == null) ? "<null>" : this.dedicated));
        sb.append(',');
        sb.append("os");
        sb.append('=');
        sb.append(((this.os == null) ? "<null>" : this.os));
        sb.append(',');
        sb.append("gametype");
        sb.append('=');
        sb.append(((this.gametype == null) ? "<null>" : this.gametype));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}