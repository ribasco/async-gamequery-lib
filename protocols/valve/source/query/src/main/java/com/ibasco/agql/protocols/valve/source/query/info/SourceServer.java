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

package com.ibasco.agql.protocols.valve.source.query.info;

import java.net.InetSocketAddress;

/**
 * <p>SourceServer class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SourceServer {

    private String name;

    private byte networkVersion;

    private String mapName;

    private String gameDirectory;

    private String gameDescription;

    private int appId;

    private int numOfPlayers;

    private int maxPlayers;

    private int numOfBots;

    private boolean dedicated;

    private String operatingSystem;

    private boolean passwordProtected;

    private boolean secure;

    private String gameVersion;

    private long serverId;

    private int tvPort;

    private String tvName;

    private String serverTags;

    private long gameId;

    private InetSocketAddress address;

    private boolean privateServer;

    /**
     * <p>isPrivateServer.</p>
     *
     * @return a boolean
     */
    public boolean isPrivateServer() {
        return privateServer;
    }

    /**
     * <p>Setter for the field <code>privateServer</code>.</p>
     *
     * @param privateServer a boolean
     */
    public void setPrivateServer(boolean privateServer) {
        this.privateServer = privateServer;
    }

    /**
     * <p>Getter for the field <code>networkVersion</code>.</p>
     *
     * @return a byte
     */
    public byte getNetworkVersion() {
        return networkVersion;
    }

    /**
     * <p>Setter for the field <code>networkVersion</code>.</p>
     *
     * @param networkVersion a byte
     */
    public void setNetworkVersion(byte networkVersion) {
        this.networkVersion = networkVersion;
    }

    /**
     * <p>Getter for the field <code>mapName</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getMapName() {
        return mapName;
    }

    /**
     * <p>Setter for the field <code>mapName</code>.</p>
     *
     * @param mapName a {@link java.lang.String} object
     */
    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    /**
     * <p>Getter for the field <code>gameDirectory</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getGameDirectory() {
        return gameDirectory;
    }

    /**
     * <p>Setter for the field <code>gameDirectory</code>.</p>
     *
     * @param gameDirectory a {@link java.lang.String} object
     */
    public void setGameDirectory(String gameDirectory) {
        this.gameDirectory = gameDirectory;
    }

    /**
     * <p>Getter for the field <code>gameDescription</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getGameDescription() {
        return gameDescription;
    }

    /**
     * <p>Setter for the field <code>gameDescription</code>.</p>
     *
     * @param gameDescription a {@link java.lang.String} object
     */
    public void setGameDescription(String gameDescription) {
        this.gameDescription = gameDescription;
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
     * @param appId a int
     */
    public void setAppId(int appId) {
        this.appId = appId;
    }

    /**
     * <p>Getter for the field <code>numOfPlayers</code>.</p>
     *
     * @return a int
     */
    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    /**
     * <p>Setter for the field <code>numOfPlayers</code>.</p>
     *
     * @param numOfPlayers a int
     */
    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    /**
     * <p>Getter for the field <code>maxPlayers</code>.</p>
     *
     * @return a int
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * <p>Setter for the field <code>maxPlayers</code>.</p>
     *
     * @param maxPlayers a int
     */
    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    /**
     * <p>Getter for the field <code>numOfBots</code>.</p>
     *
     * @return a int
     */
    public int getNumOfBots() {
        return numOfBots;
    }

    /**
     * <p>Setter for the field <code>numOfBots</code>.</p>
     *
     * @param numOfBots a int
     */
    public void setNumOfBots(int numOfBots) {
        this.numOfBots = numOfBots;
    }

    /**
     * <p>isDedicated.</p>
     *
     * @return a boolean
     */
    public boolean isDedicated() {
        return dedicated;
    }

    /**
     * <p>Setter for the field <code>dedicated</code>.</p>
     *
     * @param dedicated a boolean
     */
    public void setDedicated(boolean dedicated) {
        this.dedicated = dedicated;
    }

    /**
     * <p>Getter for the field <code>operatingSystem</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getOperatingSystem() {
        return operatingSystem;
    }

    /**
     * <p>Setter for the field <code>operatingSystem</code>.</p>
     *
     * @param operatingSystem a {@link java.lang.String} object
     */
    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    /**
     * <p>isPasswordProtected.</p>
     *
     * @return a boolean
     */
    public boolean isPasswordProtected() {
        return passwordProtected;
    }

    /**
     * <p>Setter for the field <code>passwordProtected</code>.</p>
     *
     * @param passwordProtected a boolean
     */
    public void setPasswordProtected(boolean passwordProtected) {
        this.passwordProtected = passwordProtected;
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
     * @param secure a boolean
     */
    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    /**
     * <p>Getter for the field <code>gameVersion</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getGameVersion() {
        return gameVersion;
    }

    /**
     * <p>Setter for the field <code>gameVersion</code>.</p>
     *
     * @param gameVersion a {@link java.lang.String} object
     */
    public void setGameVersion(String gameVersion) {
        this.gameVersion = gameVersion;
    }

    /**
     * <p>Getter for the field <code>serverId</code>.</p>
     *
     * @return a long
     */
    public long getServerId() {
        return serverId;
    }

    /**
     * <p>Setter for the field <code>serverId</code>.</p>
     *
     * @param serverId a long
     */
    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    /**
     * <p>Getter for the field <code>tvPort</code>.</p>
     *
     * @return a int
     */
    public int getTvPort() {
        return tvPort;
    }

    /**
     * <p>Setter for the field <code>tvPort</code>.</p>
     *
     * @param tvPort a int
     */
    public void setTvPort(int tvPort) {
        this.tvPort = tvPort;
    }

    /**
     * <p>Getter for the field <code>tvName</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getTvName() {
        return tvName;
    }

    /**
     * <p>Setter for the field <code>tvName</code>.</p>
     *
     * @param tvName a {@link java.lang.String} object
     */
    public void setTvName(String tvName) {
        this.tvName = tvName;
    }

    /**
     * <p>Getter for the field <code>serverTags</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getServerTags() {
        return serverTags;
    }

    /**
     * <p>Setter for the field <code>serverTags</code>.</p>
     *
     * @param serverTags a {@link java.lang.String} object
     */
    public void setServerTags(String serverTags) {
        this.serverTags = serverTags;
    }

    /**
     * <p>Getter for the field <code>gameId</code>.</p>
     *
     * @return a long
     */
    public long getGameId() {
        return gameId;
    }

    /**
     * <p>Setter for the field <code>gameId</code>.</p>
     *
     * @param gameId a long
     */
    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name a {@link java.lang.String} object
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>Getter for the field <code>address</code>.</p>
     *
     * @return a {@link java.net.InetSocketAddress} object
     */
    public InetSocketAddress getAddress() {
        return address;
    }

    /**
     * <p>getHostAddress.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getHostAddress() {
        return address.getAddress().getHostAddress();
    }

    /**
     * <p>getPort.</p>
     *
     * @return a int
     */
    public int getPort() {
        return address.getPort();
    }

    /**
     * <p>Setter for the field <code>address</code>.</p>
     *
     * @param address a {@link java.net.InetSocketAddress} object
     */
    public void setAddress(InetSocketAddress address) {
        this.address = address;
    }
}
