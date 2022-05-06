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

package com.ibasco.agql.protocols.valve.dota2.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>Dota2ServerStatsPlayer class.</p>
 *
 * @author Rafael Luis Ibasco
 */
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
     * <p>Getter for the field <code>accountId</code>.</p>
     *
     * @return The accountId
     */
    public long getAccountId() {
        return accountId;
    }

    /**
     * <p>Setter for the field <code>accountId</code>.</p>
     *
     * @param accountId
     *         The accountId
     */
    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    /**
     * <p>Getter for the field <code>playerid</code>.</p>
     *
     * @return The playerid
     */
    public int getPlayerid() {
        return playerid;
    }

    /**
     * <p>Setter for the field <code>playerid</code>.</p>
     *
     * @param playerid
     *         The playerid
     */
    public void setPlayerid(int playerid) {
        this.playerid = playerid;
    }

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name
     *         The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>Getter for the field <code>team</code>.</p>
     *
     * @return The team
     */
    public int getTeam() {
        return team;
    }

    /**
     * <p>Setter for the field <code>team</code>.</p>
     *
     * @param team
     *         The team
     */
    public void setTeam(int team) {
        this.team = team;
    }

    /**
     * <p>Getter for the field <code>heroid</code>.</p>
     *
     * @return The heroid
     */
    public int getHeroid() {
        return heroid;
    }

    /**
     * <p>Setter for the field <code>heroid</code>.</p>
     *
     * @param heroid
     *         The heroid
     */
    public void setHeroid(int heroid) {
        this.heroid = heroid;
    }

    /**
     * <p>Getter for the field <code>level</code>.</p>
     *
     * @return The level
     */
    public int getLevel() {
        return level;
    }

    /**
     * <p>Setter for the field <code>level</code>.</p>
     *
     * @param level
     *         The level
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * <p>Getter for the field <code>killCount</code>.</p>
     *
     * @return The killCount
     */
    public int getKillCount() {
        return killCount;
    }

    /**
     * <p>Setter for the field <code>killCount</code>.</p>
     *
     * @param killCount
     *         The kill_count
     */
    public void setKillCount(int killCount) {
        this.killCount = killCount;
    }

    /**
     * <p>Getter for the field <code>deathCount</code>.</p>
     *
     * @return The deathCount
     */
    public int getDeathCount() {
        return deathCount;
    }

    /**
     * <p>Setter for the field <code>deathCount</code>.</p>
     *
     * @param deathCount
     *         The death_count
     */
    public void setDeathCount(int deathCount) {
        this.deathCount = deathCount;
    }

    /**
     * <p>Getter for the field <code>assistsCount</code>.</p>
     *
     * @return The assistsCount
     */
    public int getAssistsCount() {
        return assistsCount;
    }

    /**
     * <p>Setter for the field <code>assistsCount</code>.</p>
     *
     * @param assistsCount
     *         The assists_count
     */
    public void setAssistsCount(int assistsCount) {
        this.assistsCount = assistsCount;
    }

    /**
     * <p>Getter for the field <code>deniesCount</code>.</p>
     *
     * @return The deniesCount
     */
    public int getDeniesCount() {
        return deniesCount;
    }

    /**
     * <p>Setter for the field <code>deniesCount</code>.</p>
     *
     * @param deniesCount
     *         The denies_count
     */
    public void setDeniesCount(int deniesCount) {
        this.deniesCount = deniesCount;
    }

    /**
     * <p>Getter for the field <code>lhCount</code>.</p>
     *
     * @return The lhCount
     */
    public int getLhCount() {
        return lhCount;
    }

    /**
     * <p>Setter for the field <code>lhCount</code>.</p>
     *
     * @param lhCount
     *         The lh_count
     */
    public void setLhCount(int lhCount) {
        this.lhCount = lhCount;
    }

    /**
     * <p>Getter for the field <code>gold</code>.</p>
     *
     * @return The gold
     */
    public int getGold() {
        return gold;
    }

    /**
     * <p>Setter for the field <code>gold</code>.</p>
     *
     * @param gold
     *         The gold
     */
    public void setGold(int gold) {
        this.gold = gold;
    }

    /**
     * <p>Getter for the field <code>x</code>.</p>
     *
     * @return The x
     */
    public double getX() {
        return x;
    }

    /**
     * <p>Setter for the field <code>x</code>.</p>
     *
     * @param x
     *         The x
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * <p>Getter for the field <code>y</code>.</p>
     *
     * @return The y
     */
    public double getY() {
        return y;
    }

    /**
     * <p>Setter for the field <code>y</code>.</p>
     *
     * @param y
     *         The y
     */
    public void setY(double y) {
        this.y = y;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

}
