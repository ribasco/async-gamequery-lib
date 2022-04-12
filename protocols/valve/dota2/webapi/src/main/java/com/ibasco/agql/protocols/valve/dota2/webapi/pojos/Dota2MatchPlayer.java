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

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Dota2MatchPlayer class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class Dota2MatchPlayer {

    @SerializedName("account_id")
    private long accountId;
    @SerializedName("player_slot")
    private int playerSlot;
    @SerializedName("hero_id")
    private int heroId;
    @SerializedName("item_0")
    private int item0;
    @SerializedName("item_1")
    private int item1;
    @SerializedName("item_2")
    private int item2;
    @SerializedName("item_3")
    private int item3;
    @SerializedName("item_4")
    private int item4;
    @SerializedName("item_5")
    private int item5;
    @SerializedName("kills")
    private int kills;
    @SerializedName("deaths")
    private int deaths;
    @SerializedName("assists")
    private int assists;
    @SerializedName("leaver_status")
    private int leaverStatus;
    @SerializedName("last_hits")
    private int lastHits;
    @SerializedName("denies")
    private int denies;
    @SerializedName("gold_per_min")
    private int goldPerMin;
    @SerializedName("xp_per_min")
    private int xpPerMin;
    @SerializedName("level")
    private int level;
    @SerializedName("hero_damage")
    private int heroDamage;
    @SerializedName("tower_damage")
    private int towerDamage;
    @SerializedName("hero_healing")
    private int heroHealing;
    @SerializedName("gold")
    private int gold;
    @SerializedName("gold_spent")
    private int goldSpent;
    @SerializedName("scaled_hero_damage")
    private int scaledHeroDamage;
    @SerializedName("scaled_tower_damage")
    private int scaledTowerDamage;
    @SerializedName("scaled_hero_healing")
    private int scaledHeroHealing;
    @SerializedName("ability_upgrades")
    private List<Dota2MatchAbilityUpgrade> abilityUpgrades = new ArrayList<>();

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
     *         The account_id
     */
    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

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
     *         The item_0
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
     *         The item_1
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
     *         The item_2
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
     *         The item_3
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
     *         The item_4
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
     *         The item_5
     */
    public void setItem5(int item5) {
        this.item5 = item5;
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
     * <p>Getter for the field <code>deaths</code>.</p>
     *
     * @return The deaths
     */
    public int getDeaths() {
        return deaths;
    }

    /**
     * <p>Setter for the field <code>deaths</code>.</p>
     *
     * @param deaths
     *         The deaths
     */
    public void setDeaths(int deaths) {
        this.deaths = deaths;
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
     * <p>Getter for the field <code>leaverStatus</code>.</p>
     *
     * @return The leaverStatus
     */
    public int getLeaverStatus() {
        return leaverStatus;
    }

    /**
     * <p>Setter for the field <code>leaverStatus</code>.</p>
     *
     * @param leaverStatus
     *         The leaver_status
     */
    public void setLeaverStatus(int leaverStatus) {
        this.leaverStatus = leaverStatus;
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
     * <p>Getter for the field <code>heroDamage</code>.</p>
     *
     * @return The heroDamage
     */
    public int getHeroDamage() {
        return heroDamage;
    }

    /**
     * <p>Setter for the field <code>heroDamage</code>.</p>
     *
     * @param heroDamage
     *         The hero_damage
     */
    public void setHeroDamage(int heroDamage) {
        this.heroDamage = heroDamage;
    }

    /**
     * <p>Getter for the field <code>towerDamage</code>.</p>
     *
     * @return The towerDamage
     */
    public int getTowerDamage() {
        return towerDamage;
    }

    /**
     * <p>Setter for the field <code>towerDamage</code>.</p>
     *
     * @param towerDamage
     *         The tower_damage
     */
    public void setTowerDamage(int towerDamage) {
        this.towerDamage = towerDamage;
    }

    /**
     * <p>Getter for the field <code>heroHealing</code>.</p>
     *
     * @return The heroHealing
     */
    public int getHeroHealing() {
        return heroHealing;
    }

    /**
     * <p>Setter for the field <code>heroHealing</code>.</p>
     *
     * @param heroHealing
     *         The hero_healing
     */
    public void setHeroHealing(int heroHealing) {
        this.heroHealing = heroHealing;
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
     * <p>Getter for the field <code>goldSpent</code>.</p>
     *
     * @return The goldSpent
     */
    public int getGoldSpent() {
        return goldSpent;
    }

    /**
     * <p>Setter for the field <code>goldSpent</code>.</p>
     *
     * @param goldSpent
     *         The gold_spent
     */
    public void setGoldSpent(int goldSpent) {
        this.goldSpent = goldSpent;
    }

    /**
     * <p>Getter for the field <code>scaledHeroDamage</code>.</p>
     *
     * @return The scaledHeroDamage
     */
    public int getScaledHeroDamage() {
        return scaledHeroDamage;
    }

    /**
     * <p>Setter for the field <code>scaledHeroDamage</code>.</p>
     *
     * @param scaledHeroDamage
     *         The scaled_hero_damage
     */
    public void setScaledHeroDamage(int scaledHeroDamage) {
        this.scaledHeroDamage = scaledHeroDamage;
    }

    /**
     * <p>Getter for the field <code>scaledTowerDamage</code>.</p>
     *
     * @return The scaledTowerDamage
     */
    public int getScaledTowerDamage() {
        return scaledTowerDamage;
    }

    /**
     * <p>Setter for the field <code>scaledTowerDamage</code>.</p>
     *
     * @param scaledTowerDamage
     *         The scaled_tower_damage
     */
    public void setScaledTowerDamage(int scaledTowerDamage) {
        this.scaledTowerDamage = scaledTowerDamage;
    }

    /**
     * <p>Getter for the field <code>scaledHeroHealing</code>.</p>
     *
     * @return The scaledHeroHealing
     */
    public int getScaledHeroHealing() {
        return scaledHeroHealing;
    }

    /**
     * <p>Setter for the field <code>scaledHeroHealing</code>.</p>
     *
     * @param scaledHeroHealing
     *         The scaled_hero_healing
     */
    public void setScaledHeroHealing(int scaledHeroHealing) {
        this.scaledHeroHealing = scaledHeroHealing;
    }

    /**
     * <p>Getter for the field <code>abilityUpgrades</code>.</p>
     *
     * @return The abilityUpgrades
     */
    public List<Dota2MatchAbilityUpgrade> getAbilityUpgrades() {
        return abilityUpgrades;
    }

    /**
     * <p>Setter for the field <code>abilityUpgrades</code>.</p>
     *
     * @param abilityUpgrades
     *         The ability_upgrades
     */
    public void setAbilityUpgrades(List<Dota2MatchAbilityUpgrade> abilityUpgrades) {
        this.abilityUpgrades = abilityUpgrades;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

}
