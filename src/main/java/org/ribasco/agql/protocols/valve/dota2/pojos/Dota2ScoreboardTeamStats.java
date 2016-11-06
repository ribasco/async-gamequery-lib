
package org.ribasco.agql.protocols.valve.dota2.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class Dota2ScoreboardTeamStats {

    @SerializedName("score")
    private int score;

    @SerializedName("tower_state")
    private int towerState;

    @SerializedName("barracks_state")
    private int barracksState;

    @SerializedName("picks")
    private List<Dota2ScoreboardTeamPicks> picks = new ArrayList<>();

    @SerializedName("bans")
    private List<Dota2ScoreboardTeamBan> bans = new ArrayList<>();

    @SerializedName("players")
    private List<Dota2ScoreboardPlayerStats> players = new ArrayList<>();

    @SerializedName("abilities")
    private List<Dota2ScoreboardTeamAbility> abilities = new ArrayList<>();

    /**
     * @return The score
     */
    public int getScore() {
        return score;
    }

    /**
     * @param score The score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * @return The towerState
     */
    public int getTowerState() {
        return towerState;
    }

    /**
     * @param towerState The tower_state
     */
    public void setTowerState(int towerState) {
        this.towerState = towerState;
    }

    /**
     * @return The barracksState
     */
    public int getBarracksState() {
        return barracksState;
    }

    /**
     * @param barracksState The barracks_state
     */
    public void setBarracksState(int barracksState) {
        this.barracksState = barracksState;
    }

    /**
     * @return The picks
     */
    public List<Dota2ScoreboardTeamPicks> getPicks() {
        return picks;
    }

    /**
     * @param picks The picks
     */
    public void setPicks(List<Dota2ScoreboardTeamPicks> picks) {
        this.picks = picks;
    }

    /**
     * @return The bans
     */
    public List<Dota2ScoreboardTeamBan> getBans() {
        return bans;
    }

    /**
     * @param bans The bans
     */
    public void setBans(List<Dota2ScoreboardTeamBan> bans) {
        this.bans = bans;
    }

    /**
     * @return The players
     */
    public List<Dota2ScoreboardPlayerStats> getPlayers() {
        return players;
    }

    /**
     * @param players The players
     */
    public void setPlayers(List<Dota2ScoreboardPlayerStats> players) {
        this.players = players;
    }

    /**
     * @return The abilities
     */
    public List<Dota2ScoreboardTeamAbility> getAbilities() {
        return abilities;
    }

    /**
     * @param abilities The abilities
     */
    public void setAbilities(List<Dota2ScoreboardTeamAbility> abilities) {
        this.abilities = abilities;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
