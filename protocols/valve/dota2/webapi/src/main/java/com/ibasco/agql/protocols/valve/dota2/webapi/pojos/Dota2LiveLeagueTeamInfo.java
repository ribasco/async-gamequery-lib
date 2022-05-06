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
 * <p>Dota2LiveLeagueTeamInfo class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class Dota2LiveLeagueTeamInfo {

    @SerializedName("team_name")
    private String teamName;

    @SerializedName("team_id")
    private long teamId;

    @SerializedName("team_logo")
    private long teamLogo;

    private boolean complete;

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
     * @param teamName
     *         a {@link java.lang.String} object
     */
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    /**
     * <p>Getter for the field <code>teamId</code>.</p>
     *
     * @return a long
     */
    public long getTeamId() {
        return teamId;
    }

    /**
     * <p>Setter for the field <code>teamId</code>.</p>
     *
     * @param teamId
     *         a long
     */
    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    /**
     * <p>Getter for the field <code>teamLogo</code>.</p>
     *
     * @return a long
     */
    public long getTeamLogo() {
        return teamLogo;
    }

    /**
     * <p>Setter for the field <code>teamLogo</code>.</p>
     *
     * @param teamLogo
     *         a long
     */
    public void setTeamLogo(long teamLogo) {
        this.teamLogo = teamLogo;
    }

    /**
     * <p>isComplete.</p>
     *
     * @return a boolean
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     * <p>Setter for the field <code>complete</code>.</p>
     *
     * @param complete
     *         a boolean
     */
    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
