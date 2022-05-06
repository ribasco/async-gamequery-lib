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

import com.google.gson.annotations.SerializedName;

/**
 * <p>SteamPlayerBadge class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SteamPlayerBadge {

    @SerializedName("badgeid")
    private int badgeId;

    private int level;

    @SerializedName("completion_time")
    private int completionTime;

    private int xp;

    private int scarcity;

    /**
     * <p>Getter for the field <code>badgeId</code>.</p>
     *
     * @return a int
     */
    public int getBadgeId() {
        return badgeId;
    }

    /**
     * <p>Setter for the field <code>badgeId</code>.</p>
     *
     * @param badgeId
     *         a int
     */
    public void setBadgeId(int badgeId) {
        this.badgeId = badgeId;
    }

    /**
     * <p>Getter for the field <code>level</code>.</p>
     *
     * @return a int
     */
    public int getLevel() {
        return level;
    }

    /**
     * <p>Setter for the field <code>level</code>.</p>
     *
     * @param level
     *         a int
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * <p>Getter for the field <code>completionTime</code>.</p>
     *
     * @return a int
     */
    public int getCompletionTime() {
        return completionTime;
    }

    /**
     * <p>Setter for the field <code>completionTime</code>.</p>
     *
     * @param completionTime
     *         a int
     */
    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }

    /**
     * <p>Getter for the field <code>xp</code>.</p>
     *
     * @return a int
     */
    public int getXp() {
        return xp;
    }

    /**
     * <p>Setter for the field <code>xp</code>.</p>
     *
     * @param xp
     *         a int
     */
    public void setXp(int xp) {
        this.xp = xp;
    }

    /**
     * <p>Getter for the field <code>scarcity</code>.</p>
     *
     * @return a int
     */
    public int getScarcity() {
        return scarcity;
    }

    /**
     * <p>Setter for the field <code>scarcity</code>.</p>
     *
     * @param scarcity
     *         a int
     */
    public void setScarcity(int scarcity) {
        this.scarcity = scarcity;
    }
}
