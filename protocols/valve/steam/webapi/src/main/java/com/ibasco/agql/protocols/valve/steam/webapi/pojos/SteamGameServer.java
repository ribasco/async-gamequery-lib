/*
 * Copyright 2018-2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.protocols.valve.steam.webapi.pojos;

/**
 * Created by raffy on 10/26/2016.
 */
public class SteamGameServer {
    private String addr;
    private int gmxindex;
    private int appId;
    private String gameDir;
    private int region;
    private boolean secure;
    private boolean lan;
    private int gameport;
    private int specport;

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public int getGmxindex() {
        return gmxindex;
    }

    public void setGmxindex(int gmxindex) {
        this.gmxindex = gmxindex;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getGameDir() {
        return gameDir;
    }

    public void setGameDir(String gameDir) {
        this.gameDir = gameDir;
    }

    public int getRegion() {
        return region;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public boolean isLan() {
        return lan;
    }

    public void setLan(boolean lan) {
        this.lan = lan;
    }

    public int getGameport() {
        return gameport;
    }

    public void setGameport(int gameport) {
        this.gameport = gameport;
    }

    public int getSpecport() {
        return specport;
    }

    public void setSpecport(int specport) {
        this.specport = specport;
    }
}
