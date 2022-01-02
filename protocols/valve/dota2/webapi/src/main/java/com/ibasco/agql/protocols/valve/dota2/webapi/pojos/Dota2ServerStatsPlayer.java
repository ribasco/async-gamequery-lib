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

package com.ibasco.agql.protocols.valve.dota2.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Dota2ServerStatsPlayer {
    @SerializedName("accountId")
    private long accountId;
    @SerializedName("playerid")
    private int playerid;
    @SerializedName("name")
    private String name;
    @SerializedName("team")
    private int team;
    @SerializedName("heroid")
    private int heroid;
    @SerializedName("level")
    private int level;
    @SerializedName("kill_count")
    private int killCount;
    @SerializedName("death_count")
    private int deathCount;
    @SerializedName("assists_count")
    private int assistsCount;
    @SerializedName("denies_count")
    private int deniesCount;
    @SerializedName("lh_count")
    private int lhCount;
    @SerializedName("gold")
    private int gold;
    @SerializedName("x")
    private double x;
    @SerializedName("y")
    private double y;

    /**
     * @return The accountId
     */
    public long getAccountId() {
        return accountId;
    }

    /**
     * @param accountId
     *         The accountId
     */
    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    /**
     * @return The playerid
     */
    public int getPlayerid() {
        return playerid;
    }

    /**
     * @param playerid
     *         The playerid
     */
    public void setPlayerid(int playerid) {
        this.playerid = playerid;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *         The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The team
     */
    public int getTeam() {
        return team;
    }

    /**
     * @param team
     *         The team
     */
    public void setTeam(int team) {
        this.team = team;
    }

    /**
     * @return The heroid
     */
    public int getHeroid() {
        return heroid;
    }

    /**
     * @param heroid
     *         The heroid
     */
    public void setHeroid(int heroid) {
        this.heroid = heroid;
    }

    /**
     * @return The level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @param level
     *         The level
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * @return The killCount
     */
    public int getKillCount() {
        return killCount;
    }

    /**
     * @param killCount
     *         The kill_count
     */
    public void setKillCount(int killCount) {
        this.killCount = killCount;
    }

    /**
     * @return The deathCount
     */
    public int getDeathCount() {
        return deathCount;
    }

    /**
     * @param deathCount
     *         The death_count
     */
    public void setDeathCount(int deathCount) {
        this.deathCount = deathCount;
    }

    /**
     * @return The assistsCount
     */
    public int getAssistsCount() {
        return assistsCount;
    }

    /**
     * @param assistsCount
     *         The assists_count
     */
    public void setAssistsCount(int assistsCount) {
        this.assistsCount = assistsCount;
    }

    /**
     * @return The deniesCount
     */
    public int getDeniesCount() {
        return deniesCount;
    }

    /**
     * @param deniesCount
     *         The denies_count
     */
    public void setDeniesCount(int deniesCount) {
        this.deniesCount = deniesCount;
    }

    /**
     * @return The lhCount
     */
    public int getLhCount() {
        return lhCount;
    }

    /**
     * @param lhCount
     *         The lh_count
     */
    public void setLhCount(int lhCount) {
        this.lhCount = lhCount;
    }

    /**
     * @return The gold
     */
    public int getGold() {
        return gold;
    }

    /**
     * @param gold
     *         The gold
     */
    public void setGold(int gold) {
        this.gold = gold;
    }

    /**
     * @return The x
     */
    public double getX() {
        return x;
    }

    /**
     * @param x
     *         The x
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return The y
     */
    public double getY() {
        return y;
    }

    /**
     * @param y
     *         The y
     */
    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

}
