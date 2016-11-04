
package org.ribasco.asyncgamequerylib.protocols.valve.dota2.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.annotation.Generated;
import java.util.List;

@Generated("org.jsonschema2pojo")
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
    private List<Dota2MatchAbilityUpgrade> abilityUpgrades;

    /**
     * @return The accountId
     */
    public long getAccountId() {
        return accountId;
    }

    /**
     * @param accountId The account_id
     */
    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    /**
     * @return The playerSlot
     */
    public int getPlayerSlot() {
        return playerSlot;
    }

    /**
     * @param playerSlot The player_slot
     */
    public void setPlayerSlot(int playerSlot) {
        this.playerSlot = playerSlot;
    }

    /**
     * @return The heroId
     */
    public int getHeroId() {
        return heroId;
    }

    /**
     * @param heroId The hero_id
     */
    public void setHeroId(int heroId) {
        this.heroId = heroId;
    }

    /**
     * @return The item0
     */
    public int getItem0() {
        return item0;
    }

    /**
     * @param item0 The item_0
     */
    public void setItem0(int item0) {
        this.item0 = item0;
    }

    /**
     * @return The item1
     */
    public int getItem1() {
        return item1;
    }

    /**
     * @param item1 The item_1
     */
    public void setItem1(int item1) {
        this.item1 = item1;
    }

    /**
     * @return The item2
     */
    public int getItem2() {
        return item2;
    }

    /**
     * @param item2 The item_2
     */
    public void setItem2(int item2) {
        this.item2 = item2;
    }

    /**
     * @return The item3
     */
    public int getItem3() {
        return item3;
    }

    /**
     * @param item3 The item_3
     */
    public void setItem3(int item3) {
        this.item3 = item3;
    }

    /**
     * @return The item4
     */
    public int getItem4() {
        return item4;
    }

    /**
     * @param item4 The item_4
     */
    public void setItem4(int item4) {
        this.item4 = item4;
    }

    /**
     * @return The item5
     */
    public int getItem5() {
        return item5;
    }

    /**
     * @param item5 The item_5
     */
    public void setItem5(int item5) {
        this.item5 = item5;
    }

    /**
     * @return The kills
     */
    public int getKills() {
        return kills;
    }

    /**
     * @param kills The kills
     */
    public void setKills(int kills) {
        this.kills = kills;
    }

    /**
     * @return The deaths
     */
    public int getDeaths() {
        return deaths;
    }

    /**
     * @param deaths The deaths
     */
    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    /**
     * @return The assists
     */
    public int getAssists() {
        return assists;
    }

    /**
     * @param assists The assists
     */
    public void setAssists(int assists) {
        this.assists = assists;
    }

    /**
     * @return The leaverStatus
     */
    public int getLeaverStatus() {
        return leaverStatus;
    }

    /**
     * @param leaverStatus The leaver_status
     */
    public void setLeaverStatus(int leaverStatus) {
        this.leaverStatus = leaverStatus;
    }

    /**
     * @return The lastHits
     */
    public int getLastHits() {
        return lastHits;
    }

    /**
     * @param lastHits The last_hits
     */
    public void setLastHits(int lastHits) {
        this.lastHits = lastHits;
    }

    /**
     * @return The denies
     */
    public int getDenies() {
        return denies;
    }

    /**
     * @param denies The denies
     */
    public void setDenies(int denies) {
        this.denies = denies;
    }

    /**
     * @return The goldPerMin
     */
    public int getGoldPerMin() {
        return goldPerMin;
    }

    /**
     * @param goldPerMin The gold_per_min
     */
    public void setGoldPerMin(int goldPerMin) {
        this.goldPerMin = goldPerMin;
    }

    /**
     * @return The xpPerMin
     */
    public int getXpPerMin() {
        return xpPerMin;
    }

    /**
     * @param xpPerMin The xp_per_min
     */
    public void setXpPerMin(int xpPerMin) {
        this.xpPerMin = xpPerMin;
    }

    /**
     * @return The level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @param level The level
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * @return The heroDamage
     */
    public int getHeroDamage() {
        return heroDamage;
    }

    /**
     * @param heroDamage The hero_damage
     */
    public void setHeroDamage(int heroDamage) {
        this.heroDamage = heroDamage;
    }

    /**
     * @return The towerDamage
     */
    public int getTowerDamage() {
        return towerDamage;
    }

    /**
     * @param towerDamage The tower_damage
     */
    public void setTowerDamage(int towerDamage) {
        this.towerDamage = towerDamage;
    }

    /**
     * @return The heroHealing
     */
    public int getHeroHealing() {
        return heroHealing;
    }

    /**
     * @param heroHealing The hero_healing
     */
    public void setHeroHealing(int heroHealing) {
        this.heroHealing = heroHealing;
    }

    /**
     * @return The gold
     */
    public int getGold() {
        return gold;
    }

    /**
     * @param gold The gold
     */
    public void setGold(int gold) {
        this.gold = gold;
    }

    /**
     * @return The goldSpent
     */
    public int getGoldSpent() {
        return goldSpent;
    }

    /**
     * @param goldSpent The gold_spent
     */
    public void setGoldSpent(int goldSpent) {
        this.goldSpent = goldSpent;
    }

    /**
     * @return The scaledHeroDamage
     */
    public int getScaledHeroDamage() {
        return scaledHeroDamage;
    }

    /**
     * @param scaledHeroDamage The scaled_hero_damage
     */
    public void setScaledHeroDamage(int scaledHeroDamage) {
        this.scaledHeroDamage = scaledHeroDamage;
    }

    /**
     * @return The scaledTowerDamage
     */
    public int getScaledTowerDamage() {
        return scaledTowerDamage;
    }

    /**
     * @param scaledTowerDamage The scaled_tower_damage
     */
    public void setScaledTowerDamage(int scaledTowerDamage) {
        this.scaledTowerDamage = scaledTowerDamage;
    }

    /**
     * @return The scaledHeroHealing
     */
    public int getScaledHeroHealing() {
        return scaledHeroHealing;
    }

    /**
     * @param scaledHeroHealing The scaled_hero_healing
     */
    public void setScaledHeroHealing(int scaledHeroHealing) {
        this.scaledHeroHealing = scaledHeroHealing;
    }

    /**
     * @return The abilityUpgrades
     */
    public List<Dota2MatchAbilityUpgrade> getAbilityUpgrades() {
        return abilityUpgrades;
    }

    /**
     * @param abilityUpgrades The ability_upgrades
     */
    public void setAbilityUpgrades(List<Dota2MatchAbilityUpgrade> abilityUpgrades) {
        this.abilityUpgrades = abilityUpgrades;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

}
