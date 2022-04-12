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
 * <p>Dota2GamePlayer class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class Dota2GamePlayer {
    @SerializedName("account_id")
    private long accountId;
    @SerializedName("name")
    private String name;
    @SerializedName("hero_id")
    private int heroId;
    private int team;

    /**
     * <p>Getter for the field <code>accountId</code>.</p>
     *
     * @return a long
     */
    public long getAccountId() {
        return accountId;
    }

    /**
     * <p>Setter for the field <code>accountId</code>.</p>
     *
     * @param accountId a long
     */
    public void setAccountId(long accountId) {
        this.accountId = accountId;
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
     * @param name a {@link java.lang.String} object
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>Getter for the field <code>heroId</code>.</p>
     *
     * @return a int
     */
    public int getHeroId() {
        return heroId;
    }

    /**
     * <p>Setter for the field <code>heroId</code>.</p>
     *
     * @param heroId a int
     */
    public void setHeroId(int heroId) {
        this.heroId = heroId;
    }

    /**
     * <p>Getter for the field <code>team</code>.</p>
     *
     * @return a int
     */
    public int getTeam() {
        return team;
    }

    /**
     * <p>Setter for the field <code>team</code>.</p>
     *
     * @param team a int
     */
    public void setTeam(int team) {
        this.team = team;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
