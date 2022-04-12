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
 * <p>CheatData class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class CheatData {
    private long steamId;
    private int appId;
    private String filePath;
    private String webCheatUrl;
    private long timeNow;
    private long timeStarted;
    private long timeStopped;
    private String cheatName;
    private int gameProcessId;
    private int cheatProcessId;
    private long cheatParam1;
    private long cheatParam2;

    /**
     * <p>Getter for the field <code>steamId</code>.</p>
     *
     * @return Steamid of the user running and reporting the cheat.
     */
    public long getSteamId() {
        return steamId;
    }

    /**
     * <p>Setter for the field <code>steamId</code>.</p>
     *
     * @param steamId a long
     */
    public void setSteamId(long steamId) {
        this.steamId = steamId;
    }

    /**
     * <p>Getter for the field <code>appId</code>.</p>
     *
     * @return The Steam APP Id
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
     * <p>Getter for the field <code>filePath</code>.</p>
     *
     * @return Path and file name of the cheat executable.
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * <p>Setter for the field <code>filePath</code>.</p>
     *
     * @param filePath a {@link java.lang.String} object
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * <p>Getter for the field <code>webCheatUrl</code>.</p>
     *
     * @return The web url where the cheat was found and downloaded.
     */
    public String getWebCheatUrl() {
        return webCheatUrl;
    }

    /**
     * <p>Setter for the field <code>webCheatUrl</code>.</p>
     *
     * @param webCheatUrl a {@link java.lang.String} object
     */
    public void setWebCheatUrl(String webCheatUrl) {
        this.webCheatUrl = webCheatUrl;
    }

    /**
     * <p>Getter for the field <code>timeNow</code>.</p>
     *
     * @return Local system time now.
     */
    public long getTimeNow() {
        return timeNow;
    }

    /**
     * <p>Setter for the field <code>timeNow</code>.</p>
     *
     * @param timeNow a long
     */
    public void setTimeNow(long timeNow) {
        this.timeNow = timeNow;
    }

    /**
     * <p>Getter for the field <code>timeStarted</code>.</p>
     *
     * @return Local system time when cheat process started. ( 0 if not yet run )
     */
    public long getTimeStarted() {
        return timeStarted;
    }

    /**
     * <p>Setter for the field <code>timeStarted</code>.</p>
     *
     * @param timeStarted a long
     */
    public void setTimeStarted(long timeStarted) {
        this.timeStarted = timeStarted;
    }

    /**
     * <p>Getter for the field <code>timeStopped</code>.</p>
     *
     * @return Local system time when cheat process stopped. ( 0 if still running )
     */
    public long getTimeStopped() {
        return timeStopped;
    }

    /**
     * <p>Setter for the field <code>timeStopped</code>.</p>
     *
     * @param timeStopped a long
     */
    public void setTimeStopped(long timeStopped) {
        this.timeStopped = timeStopped;
    }

    /**
     * <p>Getter for the field <code>cheatName</code>.</p>
     *
     * @return The descriptive name for the cheat.
     */
    public String getCheatName() {
        return cheatName;
    }

    /**
     * <p>Setter for the field <code>cheatName</code>.</p>
     *
     * @param cheatName a {@link java.lang.String} object
     */
    public void setCheatName(String cheatName) {
        this.cheatName = cheatName;
    }

    /**
     * <p>Getter for the field <code>gameProcessId</code>.</p>
     *
     * @return The process ID of the running game.
     */
    public int getGameProcessId() {
        return gameProcessId;
    }

    /**
     * <p>Setter for the field <code>gameProcessId</code>.</p>
     *
     * @param gameProcessId a int
     */
    public void setGameProcessId(int gameProcessId) {
        this.gameProcessId = gameProcessId;
    }

    /**
     * <p>Getter for the field <code>cheatProcessId</code>.</p>
     *
     * @return The process ID of the cheat process that ran
     */
    public int getCheatProcessId() {
        return cheatProcessId;
    }

    /**
     * <p>Setter for the field <code>cheatProcessId</code>.</p>
     *
     * @param cheatProcessId a int
     */
    public void setCheatProcessId(int cheatProcessId) {
        this.cheatProcessId = cheatProcessId;
    }

    /**
     * <p>Getter for the field <code>cheatParam1</code>.</p>
     *
     * @return Cheat param 1
     */
    public long getCheatParam1() {
        return cheatParam1;
    }

    /**
     * <p>Setter for the field <code>cheatParam1</code>.</p>
     *
     * @param cheatParam1 a long
     */
    public void setCheatParam1(long cheatParam1) {
        this.cheatParam1 = cheatParam1;
    }

    /**
     * <p>Getter for the field <code>cheatParam2</code>.</p>
     *
     * @return Cheat param 2
     */
    public long getCheatParam2() {
        return cheatParam2;
    }

    /**
     * <p>Setter for the field <code>cheatParam2</code>.</p>
     *
     * @param cheatParam2 a long
     */
    public void setCheatParam2(long cheatParam2) {
        this.cheatParam2 = cheatParam2;
    }
}
