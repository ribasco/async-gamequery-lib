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

import org.jetbrains.annotations.ApiStatus;
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

    private boolean secure;

    private String gameVersion;

    private long serverId;

    private int tvPort;

    private String tvName;

    private String serverTags;

    private long gameId;

    private InetSocketAddress address;

    private boolean privateServer;

    private int gamePort;

    /**
     * <p>Specifies if the server is a private server and password protected</p>
     *
     * @return {@code true} if the server is a private server and requires a password. otherwise {@code false} if the server is public.
     */
    public boolean isPrivateServer() {
        return privateServer;
    }

    /**
     * <p>Setter for the field <code>privateServer</code>.</p>
     *
     * @param privateServer
     *         a boolean
     */
    public void setPrivateServer(boolean privateServer) {
        this.privateServer = privateServer;
    }

    /**
     * <p>Protocol version used by the server</p>
     *
     * @return A byte representing the protocol version of the server.
     */
    public byte getNetworkVersion() {
        return networkVersion;
    }

    /**
     * <p>Setter for the field <code>networkVersion</code>.</p>
     *
     * @param networkVersion
     *         a byte
     */
    public void setNetworkVersion(byte networkVersion) {
        this.networkVersion = networkVersion;
    }

    /**
     * <p>The name of the map currently loaded by the server</p>
     *
     * @return The map name string
     */
    public String getMapName() {
        return mapName;
    }

    /**
     * <p>Setter for the field <code>mapName</code>.</p>
     *
     * @param mapName
     *         a {@link java.lang.String} object
     */
    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    /**
     * <p>The name of the folder containing the game files</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getGameDirectory() {
        return gameDirectory;
    }

    /**
     * <p>Setter for the field <code>gameDirectory</code>.</p>
     *
     * @param gameDirectory
     *         a {@link java.lang.String} object
     */
    public void setGameDirectory(String gameDirectory) {
        this.gameDirectory = gameDirectory;
    }

    /**
     * <p>Full name of the game</p>
     *
     * @return A String representing the full name of the game
     */
    public String getGameDescription() {
        return gameDescription;
    }

    /**
     * <p>Setter for the field <code>gameDescription</code>.</p>
     *
     * @param gameDescription
     *         a {@link java.lang.String} object
     */
    public void setGameDescription(String gameDescription) {
        this.gameDescription = gameDescription;
    }

    /**
     * <p>The <a href="https://developer.valvesoftware.com/wiki/Steam_Application_ID">App ID</a> of the game</p>
     *
     * @return An integer representing the app id of the game
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
     * <p>The number of players on the server</p>
     *
     * @return The number of players currently on the server
     */
    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    /**
     * <p>Setter for the field <code>numOfPlayers</code>.</p>
     *
     * @param numOfPlayers
     *         a int
     */
    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    /**
     * <p>The maximum number of players supported by the server</p>
     *
     * @return The maximum number of players supported by the server
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * <p>Setter for the field <code>maxPlayers</code>.</p>
     *
     * @param maxPlayers
     *         a int
     */
    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    /**
     * <p>The number of bots on the server</p>
     *
     * @return The number of bots on the server
     */
    public int getNumOfBots() {
        return numOfBots;
    }

    /**
     * <p>Setter for the field <code>numOfBots</code>.</p>
     *
     * @param numOfBots
     *         a int
     */
    public void setNumOfBots(int numOfBots) {
        this.numOfBots = numOfBots;
    }

    /**
     * <p>Specified whether the server is dedicated or not</p>
     *
     * @return {@code true} if the server is dedicated, otherwise {@code false} if it is non-dedicated OR a source tv proxy
     */
    public boolean isDedicated() {
        return dedicated;
    }

    /**
     * <p>Setter for the field <code>dedicated</code>.</p>
     *
     * @param dedicated
     *         a boolean
     */
    public void setDedicated(boolean dedicated) {
        this.dedicated = dedicated;
    }

    /**
     * <p>The operating system of the server (l = linux, w = windows, m = mac)</p>
     *
     * @return The code representing the operating system of the server
     */
    public String getOperatingSystem() {
        return operatingSystem;
    }

    /**
     * <p>Setter for the field <code>operatingSystem</code>.</p>
     *
     * @param operatingSystem
     *         a {@link java.lang.String} object
     */
    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    /**
     * <p>Specifies if the server is password</p>
     *
     * @return a boolean
     *
     * @deprecated Use {@link #isPrivateServer()}
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public boolean isPasswordProtected() {
        return isPrivateServer();
    }

    /**
     * <p>Setter for the field <code>passwordProtected</code>.</p>
     *
     * @param passwordProtected
     *         a boolean
     *
     * @deprecated Use {@link #setPrivateServer(boolean)}
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public void setPasswordProtected(boolean passwordProtected) {
        setPrivateServer(passwordProtected);
    }

    /**
     * <p>Specifies whether the server uses VAC</p>
     *
     * @return {@code true} if the server has VAC enabled.
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
     * <p>The version of the game installed on the server</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getGameVersion() {
        return gameVersion;
    }

    /**
     * <p>Setter for the field <code>gameVersion</code>.</p>
     *
     * @param gameVersion
     *         a {@link java.lang.String} object
     */
    public void setGameVersion(String gameVersion) {
        this.gameVersion = gameVersion;
    }

    /**
     * <p>The server's 64-bit steam id</p>
     *
     * @return The server's 64-bit steam id
     */
    public long getServerId() {
        return serverId;
    }

    /**
     * <p>Setter for the field <code>serverId</code>.</p>
     *
     * @param serverId
     *         a long
     */
    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    /**
     * <p>Source TV Port</p>
     *
     * @return The Source TV port number
     */
    public int getTvPort() {
        return tvPort;
    }

    /**
     * <p>The Source TV port</p>
     *
     * @param tvPort
     *         The Source TV port number of the server
     */
    public void setTvPort(int tvPort) {
        this.tvPort = tvPort;
    }

    /**
     * <p>Source TV name</p>
     *
     * @return The Source TV name
     */
    public String getTvName() {
        return tvName;
    }

    /**
     * <p>Setter for the field <code>tvName</code>.</p>
     *
     * @param tvName
     *         a {@link java.lang.String} object
     */
    public void setTvName(String tvName) {
        this.tvName = tvName;
    }

    /**
     * <p>Tags that describe the game according to the server</p>
     *
     * @return A string of comma-delimited tags
     */
    public String getServerTags() {
        return serverTags;
    }

    /**
     * <p>Setter for the field <code>serverTags</code>.</p>
     *
     * @param serverTags
     *         a {@link java.lang.String} object
     */
    public void setServerTags(String serverTags) {
        this.serverTags = serverTags;
    }

    /**
     * <p>The server's 64-bit game id</p>
     *
     * @return The server's 64-bit game id
     */
    public long getGameId() {
        return gameId;
    }

    /**
     * <p>Setter for the field <code>gameId</code>.</p>
     *
     * @param gameId
     *         a long
     */
    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    /**
     * <p>The name of the server</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name
     *         a {@link java.lang.String} object
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>The address of the connection to the server</p>
     *
     * @return An {@link InetSocketAddress} containing the host and query port of the server
     */
    public InetSocketAddress getAddress() {
        return address;
    }

    /**
     * <p>Setter for the field <code>address</code>.</p>
     *
     * @param address
     *         a {@link java.net.InetSocketAddress} object
     */
    public void setAddress(InetSocketAddress address) {
        this.address = address;
    }

    /**
     * <p>The IP address of the server. This is similar to calling {@code getAddress().getHostAddress()}</p>
     *
     * @return a {@link java.lang.String} representing the host address of the server
     */
    public String getHostAddress() {
        return address.getAddress().getHostAddress();
    }

    /**
     * <p>The query port of the server. This is similar to calling {@code getAddress().getPort()}.</p>
     *
     * <p><strong>Note:</strong> For most servers, the query and game ports are usually the same. If this is not the case and you need to get the actual game port number of the server, use {@link #getGamePort()}</p>
     *
     * @return The query port number of the server.
     *
     * @see #getGamePort()
     */
    public int getPort() {
        return address.getPort();
    }

    /**
     * <p>The game port of the server</p>
     *
     * @return The game port number of the server.
     */
    public int getGamePort() {
        return gamePort;
    }

    public void setGamePort(int gamePort) {
        this.gamePort = gamePort;
    }
}
