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
 * <p>Dota2League class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class Dota2League {

    @SerializedName("leagueid")
    private int leagueId;

    private String name;

    private String description;

    private String tournamentUrl;

    private int itemdef;

    /**
     * <p>Getter for the field <code>leagueId</code>.</p>
     *
     * @return a int
     */
    public int getLeagueId() {
        return leagueId;
    }

    /**
     * <p>Setter for the field <code>leagueId</code>.</p>
     *
     * @param leagueId
     *         a int
     */
    public void setLeagueId(int leagueId) {
        this.leagueId = leagueId;
    }

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
     * @param name
     *         a {@link java.lang.String} object
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>Getter for the field <code>description</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getDescription() {
        return description;
    }

    /**
     * <p>Setter for the field <code>description</code>.</p>
     *
     * @param description
     *         a {@link java.lang.String} object
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * <p>Getter for the field <code>tournamentUrl</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getTournamentUrl() {
        return tournamentUrl;
    }

    /**
     * <p>Setter for the field <code>tournamentUrl</code>.</p>
     *
     * @param tournamentUrl
     *         a {@link java.lang.String} object
     */
    public void setTournamentUrl(String tournamentUrl) {
        this.tournamentUrl = tournamentUrl;
    }

    /**
     * <p>Getter for the field <code>itemdef</code>.</p>
     *
     * @return a int
     */
    public int getItemdef() {
        return itemdef;
    }

    /**
     * <p>Setter for the field <code>itemdef</code>.</p>
     *
     * @param itemdef
     *         a int
     */
    public void setItemdef(int itemdef) {
        this.itemdef = itemdef;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
