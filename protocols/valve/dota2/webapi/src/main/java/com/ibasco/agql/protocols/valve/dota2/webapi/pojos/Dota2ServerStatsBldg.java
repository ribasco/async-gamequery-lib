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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Dota2ServerStatsBldg {

    @SerializedName("team")
    @Expose
    private int team;
    @SerializedName("heading")
    @Expose
    private double heading;
    @SerializedName("type")
    @Expose
    private int type;
    @SerializedName("lane")
    @Expose
    private int lane;
    @SerializedName("tier")
    @Expose
    private int tier;
    @SerializedName("x")
    @Expose
    private double x;
    @SerializedName("y")
    @Expose
    private double y;
    @SerializedName("destroyed")
    @Expose
    private boolean destroyed;

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
     * @return The heading
     */
    public double getHeading() {
        return heading;
    }

    /**
     * @param heading
     *         The heading
     */
    public void setHeading(double heading) {
        this.heading = heading;
    }

    /**
     * @return The type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type
     *         The type
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return The lane
     */
    public int getLane() {
        return lane;
    }

    /**
     * @param lane
     *         The lane
     */
    public void setLane(int lane) {
        this.lane = lane;
    }

    /**
     * @return The tier
     */
    public int getTier() {
        return tier;
    }

    /**
     * @param tier
     *         The tier
     */
    public void setTier(int tier) {
        this.tier = tier;
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

    /**
     * @return The destroyed
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * @param destroyed
     *         The destroyed
     */
    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
