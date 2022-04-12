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
 * <p>CocLeague class.</p>
 *
 * @author Rafael Luis Ibasco
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
public class CocLeague {

    private long id;

    private String name;

    private CocLeagueIconUrls iconUrls;

    /**
     * <p>Getter for the field <code>id</code>.</p>
     *
     * @return a long
     */
    public long getId() {
        return id;
    }

    /**
     * <p>Setter for the field <code>id</code>.</p>
     *
     * @param id a long
     */
    public void setId(long id) {
        this.id = id;
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
     * <p>Getter for the field <code>iconUrls</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocLeagueIconUrls} object
     */
    public CocLeagueIconUrls getIconUrls() {
        return iconUrls;
    }

    /**
     * <p>Setter for the field <code>iconUrls</code>.</p>
     *
     * @param iconUrls a {@link com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocLeagueIconUrls} object
     */
    public void setIconUrls(CocLeagueIconUrls iconUrls) {
        this.iconUrls = iconUrls;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("iconUrls", getIconUrls())
                .toString();
    }
}
