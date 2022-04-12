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
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>Dota2FantasyPlayerInfo class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class Dota2FantasyPlayerInfo {
    @SerializedName("Name")
    private String name;
    @SerializedName("TeamName")
    private String teamName;
    @SerializedName("TeamTag")
    private String teamTag;
    @SerializedName("Sponsor")
    private String sponsor;
    @SerializedName("FantasyRole")
    private int fantasyRole;

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
     * <p>Getter for the field <code>teamName</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getTeamName() {
        return teamName;
    }

    /**
     * <p>Setter for the field <code>teamName</code>.</p>
     *
     * @param teamName a {@link java.lang.String} object
     */
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    /**
     * <p>Getter for the field <code>teamTag</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getTeamTag() {
        return teamTag;
    }

    /**
     * <p>Setter for the field <code>teamTag</code>.</p>
     *
     * @param teamTag a {@link java.lang.String} object
     */
    public void setTeamTag(String teamTag) {
        this.teamTag = teamTag;
    }

    /**
     * <p>Getter for the field <code>sponsor</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getSponsor() {
        return sponsor;
    }

    /**
     * <p>Setter for the field <code>sponsor</code>.</p>
     *
     * @param sponsor a {@link java.lang.String} object
     */
    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    /**
     * <p>Getter for the field <code>fantasyRole</code>.</p>
     *
     * @return a int
     */
    public int getFantasyRole() {
        return fantasyRole;
    }

    /**
     * <p>Setter for the field <code>fantasyRole</code>.</p>
     *
     * @param fantasyRole a int
     */
    public void setFantasyRole(int fantasyRole) {
        this.fantasyRole = fantasyRole;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
