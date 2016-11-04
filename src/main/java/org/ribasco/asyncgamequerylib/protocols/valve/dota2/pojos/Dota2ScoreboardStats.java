
package org.ribasco.asyncgamequerylib.protocols.valve.dota2.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Dota2ScoreboardStats {

    @SerializedName("duration")
    private double duration;
    @SerializedName("roshan_respawn_timer")
    private int roshanRespawnTimer;
    @SerializedName("radiant")
    private Dota2ScoreboardTeamStats radiantTeamScoreStats;
    @SerializedName("dire")
    private Dota2ScoreboardTeamStats direTeamScoreStats;

    /**
     * @return The duration
     */
    public double getDuration() {
        return duration;
    }

    /**
     * @param duration The duration
     */
    public void setDuration(double duration) {
        this.duration = duration;
    }

    /**
     * @return The roshanRespawnTimer
     */
    public int getRoshanRespawnTimer() {
        return roshanRespawnTimer;
    }

    /**
     * @param roshanRespawnTimer The roshan_respawn_timer
     */
    public void setRoshanRespawnTimer(int roshanRespawnTimer) {
        this.roshanRespawnTimer = roshanRespawnTimer;
    }

    /**
     * @return The radiant
     */
    public Dota2ScoreboardTeamStats getRadiantTeamScoreStats() {
        return radiantTeamScoreStats;
    }

    /**
     * @param radiantTeamScoreStats The radiant
     */
    public void setRadiantTeamScoreStats(Dota2ScoreboardTeamStats radiantTeamScoreStats) {
        this.radiantTeamScoreStats = radiantTeamScoreStats;
    }

    public Dota2ScoreboardTeamStats getDireTeamScoreStats() {
        return direTeamScoreStats;
    }

    public void setDireTeamScoreStats(Dota2ScoreboardTeamStats direTeamScoreStats) {
        this.direTeamScoreStats = direTeamScoreStats;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

}
