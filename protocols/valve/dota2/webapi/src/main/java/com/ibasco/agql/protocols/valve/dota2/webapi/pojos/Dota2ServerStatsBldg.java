/*
 * MIT License
 *
 * Copyright (c) 2016 Asynchronous Game Query Library
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
