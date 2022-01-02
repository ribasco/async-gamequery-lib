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
     * @return Steamid of the user running and reporting the cheat.
     */
    public long getSteamId() {
        return steamId;
    }

    public void setSteamId(long steamId) {
        this.steamId = steamId;
    }

    /**
     * @return The Steam APP Id
     */
    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    /**
     * @return Path and file name of the cheat executable.
     */
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * @return The web url where the cheat was found and downloaded.
     */
    public String getWebCheatUrl() {
        return webCheatUrl;
    }

    public void setWebCheatUrl(String webCheatUrl) {
        this.webCheatUrl = webCheatUrl;
    }

    /**
     * @return Local system time now.
     */
    public long getTimeNow() {
        return timeNow;
    }

    public void setTimeNow(long timeNow) {
        this.timeNow = timeNow;
    }

    /**
     * @return Local system time when cheat process started. ( 0 if not yet run )
     */
    public long getTimeStarted() {
        return timeStarted;
    }

    public void setTimeStarted(long timeStarted) {
        this.timeStarted = timeStarted;
    }

    /**
     * @return Local system time when cheat process stopped. ( 0 if still running )
     */
    public long getTimeStopped() {
        return timeStopped;
    }

    public void setTimeStopped(long timeStopped) {
        this.timeStopped = timeStopped;
    }

    /**
     * @return The descriptive name for the cheat.
     */
    public String getCheatName() {
        return cheatName;
    }

    public void setCheatName(String cheatName) {
        this.cheatName = cheatName;
    }

    /**
     * @return The process ID of the running game.
     */
    public int getGameProcessId() {
        return gameProcessId;
    }

    public void setGameProcessId(int gameProcessId) {
        this.gameProcessId = gameProcessId;
    }

    /**
     * @return The process ID of the cheat process that ran
     */
    public int getCheatProcessId() {
        return cheatProcessId;
    }

    public void setCheatProcessId(int cheatProcessId) {
        this.cheatProcessId = cheatProcessId;
    }

    /**
     * @return Cheat param 1
     */
    public long getCheatParam1() {
        return cheatParam1;
    }

    public void setCheatParam1(long cheatParam1) {
        this.cheatParam1 = cheatParam1;
    }

    /**
     * @return Cheat param 2
     */
    public long getCheatParam2() {
        return cheatParam2;
    }

    public void setCheatParam2(long cheatParam2) {
        this.cheatParam2 = cheatParam2;
    }
}
