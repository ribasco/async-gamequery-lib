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

/**
 * Created by raffy on 10/26/2016.
 *
 * @author Rafael Luis Ibasco
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

    /**
     * <p>Getter for the field <code>addr</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getAddr() {
        return addr;
    }

    /**
     * <p>Setter for the field <code>addr</code>.</p>
     *
     * @param addr
     *         a {@link java.lang.String} object
     */
    public void setAddr(String addr) {
        this.addr = addr;
    }

    /**
     * <p>Getter for the field <code>gmxindex</code>.</p>
     *
     * @return a int
     */
    public int getGmxindex() {
        return gmxindex;
    }

    /**
     * <p>Setter for the field <code>gmxindex</code>.</p>
     *
     * @param gmxindex
     *         a int
     */
    public void setGmxindex(int gmxindex) {
        this.gmxindex = gmxindex;
    }

    /**
     * <p>Getter for the field <code>appId</code>.</p>
     *
     * @return a int
     */
    public int getAppId() {
        return appId;
    }

    /**
     * <p>Setter for the field <code>appId</code>.</p>
     *
     * @param appId
     *         a int
     */
    public void setAppId(int appId) {
        this.appId = appId;
    }

    /**
     * <p>Getter for the field <code>gameDir</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getGameDir() {
        return gameDir;
    }

    /**
     * <p>Setter for the field <code>gameDir</code>.</p>
     *
     * @param gameDir
     *         a {@link java.lang.String} object
     */
    public void setGameDir(String gameDir) {
        this.gameDir = gameDir;
    }

    /**
     * <p>Getter for the field <code>region</code>.</p>
     *
     * @return a int
     */
    public int getRegion() {
        return region;
    }

    /**
     * <p>Setter for the field <code>region</code>.</p>
     *
     * @param region
     *         a int
     */
    public void setRegion(int region) {
        this.region = region;
    }

    /**
     * <p>isSecure.</p>
     *
     * @return a boolean
     */
    public boolean isSecure() {
        return secure;
    }

    /**
     * <p>Setter for the field <code>secure</code>.</p>
     *
     * @param secure
     *         a boolean
     */
    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    /**
     * <p>isLan.</p>
     *
     * @return a boolean
     */
    public boolean isLan() {
        return lan;
    }

    /**
     * <p>Setter for the field <code>lan</code>.</p>
     *
     * @param lan
     *         a boolean
     */
    public void setLan(boolean lan) {
        this.lan = lan;
    }

    /**
     * <p>Getter for the field <code>gameport</code>.</p>
     *
     * @return a int
     */
    public int getGameport() {
        return gameport;
    }

    /**
     * <p>Setter for the field <code>gameport</code>.</p>
     *
     * @param gameport
     *         a int
     */
    public void setGameport(int gameport) {
        this.gameport = gameport;
    }

    /**
     * <p>Getter for the field <code>specport</code>.</p>
     *
     * @return a int
     */
    public int getSpecport() {
        return specport;
    }

    /**
     * <p>Setter for the field <code>specport</code>.</p>
     *
     * @param specport
     *         a int
     */
    public void setSpecport(int specport) {
        this.specport = specport;
    }
}
