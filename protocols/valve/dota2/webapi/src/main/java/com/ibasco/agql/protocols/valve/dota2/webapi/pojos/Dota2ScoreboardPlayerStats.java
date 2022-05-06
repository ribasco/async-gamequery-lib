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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>Dota2ScoreboardPlayerStats class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class Dota2ScoreboardPlayerStats {

    @SerializedName("player_slot")
    @Expose
    private int playerSlot;

    @SerializedName("account_id")
    @Expose
    private int accountId;

    @SerializedName("hero_id")
    @Expose
    private int heroId;

    @SerializedName("kills")
    @Expose
    private int kills;

    @SerializedName("death")
    @Expose
    private int death;

    @SerializedName("assists")
    @Expose
    private int assists;

    @SerializedName("last_hits")
    @Expose
    private int lastHits;

    @SerializedName("denies")
    @Expose
    private int denies;

    @SerializedName("gold")
    @Expose
    private int gold;

    @SerializedName("level")
    @Expose
    private int level;

    @SerializedName("gold_per_min")
    @Expose
    private int goldPerMin;

    @SerializedName("xp_per_min")
    @Expose
    private int xpPerMin;

    @SerializedName("ultimate_state")
    @Expose
    private int ultimateState;

    @SerializedName("ultimate_cooldown")
    @Expose
    private int ultimateCooldown;

    @SerializedName("item0")
    @Expose
    private int item0;

    @SerializedName("item1")
    @Expose
    private int item1;

    @SerializedName("item2")
    @Expose
    private int item2;

    @SerializedName("item3")
    @Expose
    private int item3;

    @SerializedName("item4")
    @Expose
    private int item4;

    @SerializedName("item5")
    @Expose
    private int item5;

    @SerializedName("respawn_timer")
    @Expose
    private int respawnTimer;

    @SerializedName("position_x")
    @Expose
    private double positionX;

    @SerializedName("position_y")
    @Expose
    private double positionY;

    @SerializedName("net_worth")
    @Expose
    private int netWorth;

    /**
     * <p>Getter for the field <code>playerSlot</code>.</p>
     *
     * @return The playerSlot
     */
    public int getPlayerSlot() {
        return playerSlot;
    }

    /**
     * <p>Setter for the field <code>playerSlot</code>.</p>
     *
     * @param playerSlot
     *         The player_slot
     */
    public void setPlayerSlot(int playerSlot) {
        this.playerSlot = playerSlot;
    }

    /**
     * <p>Getter for the field <code>accountId</code>.</p>
     *
     * @return The accountId
     */
    public int getAccountId() {
        return accountId;
    }

    /**
     * <p>Setter for the field <code>accountId</code>.</p>
     *
     * @param accountId
     *         The account_id
     */
    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    /**
     * <p>Getter for the field <code>heroId</code>.</p>
     *
     * @return The heroId
     */
    public int getHeroId() {
        return heroId;
    }

    /**
     * <p>Setter for the field <code>heroId</code>.</p>
     *
     * @param heroId
     *         The hero_id
     */
    public void setHeroId(int heroId) {
        this.heroId = heroId;
    }

    /**
     * <p>Getter for the field <code>kills</code>.</p>
     *
     * @return The kills
     */
    public int getKills() {
        return kills;
    }

    /**
     * <p>Setter for the field <code>kills</code>.</p>
     *
     * @param kills
     *         The kills
     */
    public void setKills(int kills) {
        this.kills = kills;
    }

    /**
     * <p>Getter for the field <code>death</code>.</p>
     *
     * @return The death
     */
    public int getDeath() {
        return death;
    }

    /**
     * <p>Setter for the field <code>death</code>.</p>
     *
     * @param death
     *         The death
     */
    public void setDeath(int death) {
        this.death = death;
    }

    /**
     * <p>Getter for the field <code>assists</code>.</p>
     *
     * @return The assists
     */
    public int getAssists() {
        return assists;
    }

    /**
     * <p>Setter for the field <code>assists</code>.</p>
     *
     * @param assists
     *         The assists
     */
    public void setAssists(int assists) {
        this.assists = assists;
    }

    /**
     * <p>Getter for the field <code>lastHits</code>.</p>
     *
     * @return The lastHits
     */
    public int getLastHits() {
        return lastHits;
    }

    /**
     * <p>Setter for the field <code>lastHits</code>.</p>
     *
     * @param lastHits
     *         The last_hits
     */
    public void setLastHits(int lastHits) {
        this.lastHits = lastHits;
    }

    /**
     * <p>Getter for the field <code>denies</code>.</p>
     *
     * @return The denies
     */
    public int getDenies() {
        return denies;
    }

    /**
     * <p>Setter for the field <code>denies</code>.</p>
     *
     * @param denies
     *         The denies
     */
    public void setDenies(int denies) {
        this.denies = denies;
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
     * <p>Getter for the field <code>goldPerMin</code>.</p>
     *
     * @return The goldPerMin
     */
    public int getGoldPerMin() {
        return goldPerMin;
    }

    /**
     * <p>Setter for the field <code>goldPerMin</code>.</p>
     *
     * @param goldPerMin
     *         The gold_per_min
     */
    public void setGoldPerMin(int goldPerMin) {
        this.goldPerMin = goldPerMin;
    }

    /**
     * <p>Getter for the field <code>xpPerMin</code>.</p>
     *
     * @return The xpPerMin
     */
    public int getXpPerMin() {
        return xpPerMin;
    }

    /**
     * <p>Setter for the field <code>xpPerMin</code>.</p>
     *
     * @param xpPerMin
     *         The xp_per_min
     */
    public void setXpPerMin(int xpPerMin) {
        this.xpPerMin = xpPerMin;
    }

    /**
     * <p>Getter for the field <code>ultimateState</code>.</p>
     *
     * @return The ultimateState
     */
    public int getUltimateState() {
        return ultimateState;
    }

    /**
     * <p>Setter for the field <code>ultimateState</code>.</p>
     *
     * @param ultimateState
     *         The ultimate_state
     */
    public void setUltimateState(int ultimateState) {
        this.ultimateState = ultimateState;
    }

    /**
     * <p>Getter for the field <code>ultimateCooldown</code>.</p>
     *
     * @return The ultimateCooldown
     */
    public int getUltimateCooldown() {
        return ultimateCooldown;
    }

    /**
     * <p>Setter for the field <code>ultimateCooldown</code>.</p>
     *
     * @param ultimateCooldown
     *         The ultimate_cooldown
     */
    public void setUltimateCooldown(int ultimateCooldown) {
        this.ultimateCooldown = ultimateCooldown;
    }

    /**
     * <p>Getter for the field <code>item0</code>.</p>
     *
     * @return The item0
     */
    public int getItem0() {
        return item0;
    }

    /**
     * <p>Setter for the field <code>item0</code>.</p>
     *
     * @param item0
     *         The item0
     */
    public void setItem0(int item0) {
        this.item0 = item0;
    }

    /**
     * <p>Getter for the field <code>item1</code>.</p>
     *
     * @return The item1
     */
    public int getItem1() {
        return item1;
    }

    /**
     * <p>Setter for the field <code>item1</code>.</p>
     *
     * @param item1
     *         The item1
     */
    public void setItem1(int item1) {
        this.item1 = item1;
    }

    /**
     * <p>Getter for the field <code>item2</code>.</p>
     *
     * @return The item2
     */
    public int getItem2() {
        return item2;
    }

    /**
     * <p>Setter for the field <code>item2</code>.</p>
     *
     * @param item2
     *         The item2
     */
    public void setItem2(int item2) {
        this.item2 = item2;
    }

    /**
     * <p>Getter for the field <code>item3</code>.</p>
     *
     * @return The item3
     */
    public int getItem3() {
        return item3;
    }

    /**
     * <p>Setter for the field <code>item3</code>.</p>
     *
     * @param item3
     *         The item3
     */
    public void setItem3(int item3) {
        this.item3 = item3;
    }

    /**
     * <p>Getter for the field <code>item4</code>.</p>
     *
     * @return The item4
     */
    public int getItem4() {
        return item4;
    }

    /**
     * <p>Setter for the field <code>item4</code>.</p>
     *
     * @param item4
     *         The item4
     */
    public void setItem4(int item4) {
        this.item4 = item4;
    }

    /**
     * <p>Getter for the field <code>item5</code>.</p>
     *
     * @return The item5
     */
    public int getItem5() {
        return item5;
    }

    /**
     * <p>Setter for the field <code>item5</code>.</p>
     *
     * @param item5
     *         The item5
     */
    public void setItem5(int item5) {
        this.item5 = item5;
    }

    /**
     * <p>Getter for the field <code>respawnTimer</code>.</p>
     *
     * @return The respawnTimer
     */
    public int getRespawnTimer() {
        return respawnTimer;
    }

    /**
     * <p>Setter for the field <code>respawnTimer</code>.</p>
     *
     * @param respawnTimer
     *         The respawn_timer
     */
    public void setRespawnTimer(int respawnTimer) {
        this.respawnTimer = respawnTimer;
    }

    /**
     * <p>Getter for the field <code>positionX</code>.</p>
     *
     * @return The positionX
     */
    public double getPositionX() {
        return positionX;
    }

    /**
     * <p>Setter for the field <code>positionX</code>.</p>
     *
     * @param positionX
     *         The position_x
     */
    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    /**
     * <p>Getter for the field <code>positionY</code>.</p>
     *
     * @return The positionY
     */
    public double getPositionY() {
        return positionY;
    }

    /**
     * <p>Setter for the field <code>positionY</code>.</p>
     *
     * @param positionY
     *         The position_y
     */
    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    /**
     * <p>Getter for the field <code>netWorth</code>.</p>
     *
     * @return The netWorth
     */
    public int getNetWorth() {
        return netWorth;
    }

    /**
     * <p>Setter for the field <code>netWorth</code>.</p>
     *
     * @param netWorth
     *         The net_worth
     */
    public void setNetWorth(int netWorth) {
        this.netWorth = netWorth;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

}
