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

package com.ibasco.agql.protocols.supercell.coc.webapi.pojos;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jetbrains.annotations.ApiStatus;

/**
 * <p>CocAchievements class.</p>
 *
 * @author Rafael Luis Ibasco
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
public class CocAchievements {

    private String name;

    private int stars;

    private int value;

    private int target;

    private String completionInfo;

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name a {@link java.lang.String} object
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>Getter for the field <code>stars</code>.</p>
     *
     * @return a int
     */
    public int getStars() {
        return stars;
    }

    /**
     * <p>Setter for the field <code>stars</code>.</p>
     *
     * @param stars a int
     */
    public void setStars(int stars) {
        this.stars = stars;
    }

    /**
     * <p>Getter for the field <code>value</code>.</p>
     *
     * @return a int
     */
    public int getValue() {
        return value;
    }

    /**
     * <p>Setter for the field <code>value</code>.</p>
     *
     * @param value a int
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * <p>Getter for the field <code>target</code>.</p>
     *
     * @return a int
     */
    public int getTarget() {
        return target;
    }

    /**
     * <p>Setter for the field <code>target</code>.</p>
     *
     * @param target a int
     */
    public void setTarget(int target) {
        this.target = target;
    }

    /**
     * <p>Getter for the field <code>completionInfo</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getCompletionInfo() {
        return completionInfo;
    }

    /**
     * <p>Setter for the field <code>completionInfo</code>.</p>
     *
     * @param completionInfo a {@link java.lang.String} object
     */
    public void setCompletionInfo(String completionInfo) {
        this.completionInfo = completionInfo;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("name", getName())
                .append("stars", getStars())
                .append("value", getValue())
                .append("target", getTarget())
                .append("completionInfo", getCompletionInfo())
                .toString();
    }
}
