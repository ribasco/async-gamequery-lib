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
 * <p>Dota2ScoreboardStats class.</p>
 *
 * @author Rafael Luis Ibasco
 */
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
     * <p>Getter for the field <code>duration</code>.</p>
     *
     * @return The duration
     */
    public double getDuration() {
        return duration;
    }

    /**
     * <p>Setter for the field <code>duration</code>.</p>
     *
     * @param duration
     *         The duration
     */
    public void setDuration(double duration) {
        this.duration = duration;
    }

    /**
     * <p>Getter for the field <code>roshanRespawnTimer</code>.</p>
     *
     * @return The roshanRespawnTimer
     */
    public int getRoshanRespawnTimer() {
        return roshanRespawnTimer;
    }

    /**
     * <p>Setter for the field <code>roshanRespawnTimer</code>.</p>
     *
     * @param roshanRespawnTimer
     *         The roshan_respawn_timer
     */
    public void setRoshanRespawnTimer(int roshanRespawnTimer) {
        this.roshanRespawnTimer = roshanRespawnTimer;
    }

    /**
     * <p>Getter for the field <code>radiantTeamScoreStats</code>.</p>
     *
     * @return The radiant
     */
    public Dota2ScoreboardTeamStats getRadiantTeamScoreStats() {
        return radiantTeamScoreStats;
    }

    /**
     * <p>Setter for the field <code>radiantTeamScoreStats</code>.</p>
     *
     * @param radiantTeamScoreStats
     *         The radiant
     */
    public void setRadiantTeamScoreStats(Dota2ScoreboardTeamStats radiantTeamScoreStats) {
        this.radiantTeamScoreStats = radiantTeamScoreStats;
    }

    /**
     * <p>Getter for the field <code>direTeamScoreStats</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.dota2.webapi.pojos.Dota2ScoreboardTeamStats} object
     */
    public Dota2ScoreboardTeamStats getDireTeamScoreStats() {
        return direTeamScoreStats;
    }

    /**
     * <p>Setter for the field <code>direTeamScoreStats</code>.</p>
     *
     * @param direTeamScoreStats a {@link com.ibasco.agql.protocols.valve.dota2.webapi.pojos.Dota2ScoreboardTeamStats} object
     */
    public void setDireTeamScoreStats(Dota2ScoreboardTeamStats direTeamScoreStats) {
        this.direTeamScoreStats = direTeamScoreStats;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

}
