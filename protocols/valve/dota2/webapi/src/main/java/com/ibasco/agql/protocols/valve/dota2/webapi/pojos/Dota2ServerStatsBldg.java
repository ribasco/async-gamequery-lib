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

/**
 * <p>Dota2ServerStatsBldg class.</p>
 *
 * @author Rafael Luis Ibasco
 */
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
     * <p>Getter for the field <code>heading</code>.</p>
     *
     * @return The heading
     */
    public double getHeading() {
        return heading;
    }

    /**
     * <p>Setter for the field <code>heading</code>.</p>
     *
     * @param heading
     *         The heading
     */
    public void setHeading(double heading) {
        this.heading = heading;
    }

    /**
     * <p>Getter for the field <code>type</code>.</p>
     *
     * @return The type
     */
    public int getType() {
        return type;
    }

    /**
     * <p>Setter for the field <code>type</code>.</p>
     *
     * @param type
     *         The type
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * <p>Getter for the field <code>lane</code>.</p>
     *
     * @return The lane
     */
    public int getLane() {
        return lane;
    }

    /**
     * <p>Setter for the field <code>lane</code>.</p>
     *
     * @param lane
     *         The lane
     */
    public void setLane(int lane) {
        this.lane = lane;
    }

    /**
     * <p>Getter for the field <code>tier</code>.</p>
     *
     * @return The tier
     */
    public int getTier() {
        return tier;
    }

    /**
     * <p>Setter for the field <code>tier</code>.</p>
     *
     * @param tier
     *         The tier
     */
    public void setTier(int tier) {
        this.tier = tier;
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

    /**
     * <p>isDestroyed.</p>
     *
     * @return The destroyed
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * <p>Setter for the field <code>destroyed</code>.</p>
     *
     * @param destroyed
     *         The destroyed
     */
    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
