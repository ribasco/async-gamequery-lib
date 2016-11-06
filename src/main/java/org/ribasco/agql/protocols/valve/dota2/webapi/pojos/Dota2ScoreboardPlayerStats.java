
package org.ribasco.agql.protocols.valve.dota2.webapi.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
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
     * @return The accountId
     */
    public int getAccountId() {
        return accountId;
    }

    /**
     * @param accountId The account_id
     */
    public void setAccountId(int accountId) {
        this.accountId = accountId;
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
     * @return The death
     */
    public int getDeath() {
        return death;
    }

    /**
     * @param death The death
     */
    public void setDeath(int death) {
        this.death = death;
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
     * @return The ultimateState
     */
    public int getUltimateState() {
        return ultimateState;
    }

    /**
     * @param ultimateState The ultimate_state
     */
    public void setUltimateState(int ultimateState) {
        this.ultimateState = ultimateState;
    }

    /**
     * @return The ultimateCooldown
     */
    public int getUltimateCooldown() {
        return ultimateCooldown;
    }

    /**
     * @param ultimateCooldown The ultimate_cooldown
     */
    public void setUltimateCooldown(int ultimateCooldown) {
        this.ultimateCooldown = ultimateCooldown;
    }

    /**
     * @return The item0
     */
    public int getItem0() {
        return item0;
    }

    /**
     * @param item0 The item0
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
     * @param item1 The item1
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
     * @param item2 The item2
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
     * @param item3 The item3
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
     * @param item4 The item4
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
     * @param item5 The item5
     */
    public void setItem5(int item5) {
        this.item5 = item5;
    }

    /**
     * @return The respawnTimer
     */
    public int getRespawnTimer() {
        return respawnTimer;
    }

    /**
     * @param respawnTimer The respawn_timer
     */
    public void setRespawnTimer(int respawnTimer) {
        this.respawnTimer = respawnTimer;
    }

    /**
     * @return The positionX
     */
    public double getPositionX() {
        return positionX;
    }

    /**
     * @param positionX The position_x
     */
    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    /**
     * @return The positionY
     */
    public double getPositionY() {
        return positionY;
    }

    /**
     * @param positionY The position_y
     */
    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    /**
     * @return The netWorth
     */
    public int getNetWorth() {
        return netWorth;
    }

    /**
     * @param netWorth The net_worth
     */
    public void setNetWorth(int netWorth) {
        this.netWorth = netWorth;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

}
