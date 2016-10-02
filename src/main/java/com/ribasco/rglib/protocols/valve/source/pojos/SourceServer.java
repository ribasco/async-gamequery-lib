/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Ibasco
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **************************************************************************************************/

package com.ribasco.rglib.protocols.valve.source.pojos;

import com.ribasco.rglib.core.pojos.GameServer;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SourceServer extends GameServer {
    private byte networkVersion;
    private String mapName;
    private String gameDirectory;
    private String gameDescription;
    private short appId;
    private byte numOfPlayers;
    private byte maxPlayers;
    private byte numOfBots;
    private boolean dedicated;
    private char operatingSystem;
    private boolean passwordProtected;
    private boolean secure;
    private String gameVersion;
    private long serverId;
    private short tvPort;
    private String tvName;
    private String serverTags;
    private long gameId;

    public byte getNetworkVersion() {
        return networkVersion;
    }

    public void setNetworkVersion(byte networkVersion) {
        this.networkVersion = networkVersion;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getGameDirectory() {
        return gameDirectory;
    }

    public void setGameDirectory(String gameDirectory) {
        this.gameDirectory = gameDirectory;
    }

    public String getGameDescription() {
        return gameDescription;
    }

    public void setGameDescription(String gameDescription) {
        this.gameDescription = gameDescription;
    }

    public short getAppId() {
        return appId;
    }

    public void setAppId(short appId) {
        this.appId = appId;
    }

    public byte getNumOfPlayers() {
        return numOfPlayers;
    }

    public void setNumOfPlayers(byte numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    public byte getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(byte maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public byte getNumOfBots() {
        return numOfBots;
    }

    public void setNumOfBots(byte numOfBots) {
        this.numOfBots = numOfBots;
    }

    public boolean isDedicated() {
        return dedicated;
    }

    public void setDedicated(boolean dedicated) {
        this.dedicated = dedicated;
    }

    public char getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(char operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public boolean isPasswordProtected() {
        return passwordProtected;
    }

    public void setPasswordProtected(boolean passwordProtected) {
        this.passwordProtected = passwordProtected;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public String getGameVersion() {
        return gameVersion;
    }

    public void setGameVersion(String gameVersion) {
        this.gameVersion = gameVersion;
    }

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public short getTvPort() {
        return tvPort;
    }

    public void setTvPort(short tvPort) {
        this.tvPort = tvPort;
    }

    public String getTvName() {
        return tvName;
    }

    public void setTvName(String tvName) {
        this.tvName = tvName;
    }

    public String getServerTags() {
        return serverTags;
    }

    public void setServerTags(String serverTags) {
        this.serverTags = serverTags;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE).append("name", this.getName())
                .append("map", getMapName())
                .append("description", gameDescription)
                .append("numOfPlayers", getNumOfPlayers())
                .append("maxPlayers", getMaxPlayers())
                .append("numOfBots", getNumOfBots())
                .append("operatingSystem", getOperatingSystem())
                .append("appId", getAppId())
                .append("isDedicated", isDedicated())
                .append("isSecure", isSecure())
                .append("serverId", getServerId())
                .append("tags", getServerTags())
                .toString();
    }
}
