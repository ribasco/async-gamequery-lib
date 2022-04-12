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
 * <p>CocSeason class.</p>
 *
 * @author Rafael Luis Ibasco
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
public class CocSeason {

    private int rank;

    private int trophies;

    private String id;

    /**
     * <p>Getter for the field <code>rank</code>.</p>
     *
     * @return a int
     */
    public int getRank() {
        return rank;
    }

    /**
     * <p>Setter for the field <code>rank</code>.</p>
     *
     * @param rank a int
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * <p>Getter for the field <code>trophies</code>.</p>
     *
     * @return a int
     */
    public int getTrophies() {
        return trophies;
    }

    /**
     * <p>Setter for the field <code>trophies</code>.</p>
     *
     * @param trophies a int
     */
    public void setTrophies(int trophies) {
        this.trophies = trophies;
    }

    /**
     * <p>Getter for the field <code>id</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getId() {
        return id;
    }

    /**
     * <p>Setter for the field <code>id</code>.</p>
     *
     * @param id a {@link java.lang.String} object
     */
    public void setId(String id) {
        this.id = id;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("id", getId())
                .append("rank", getRank())
                .append("trophies", getTrophies())
                .toString();
    }
}
