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
 * Created by raffy on 10/28/2016.
 *
 * @author Rafael Luis Ibasco
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
public class CocClanBasicInfo {

    private String tag;

    private String name;

    private int clanLevel;

    private CocClanBadgeUrls badgeUrls;

    /**
     * <p>Getter for the field <code>clanLevel</code>.</p>
     *
     * @return a int
     */
    public int getClanLevel() {
        return clanLevel;
    }

    /**
     * <p>Setter for the field <code>clanLevel</code>.</p>
     *
     * @param clanLevel
     *         a int
     */
    public void setClanLevel(int clanLevel) {
        this.clanLevel = clanLevel;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("tag", getTag())
                .append("name", getName())
                .append("badgeUrls", getBadgeUrls())
                .toString();
    }

    /**
     * <p>Getter for the field <code>tag</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getTag() {
        return tag;
    }

    /**
     * <p>Setter for the field <code>tag</code>.</p>
     *
     * @param tag
     *         a {@link java.lang.String} object
     */
    public void setTag(String tag) {
        this.tag = tag;
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
     * <p>Getter for the field <code>badgeUrls</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocClanBadgeUrls} object
     */
    public CocClanBadgeUrls getBadgeUrls() {
        return badgeUrls;
    }

    /**
     * <p>Setter for the field <code>badgeUrls</code>.</p>
     *
     * @param badgeUrls
     *         a {@link com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocClanBadgeUrls} object
     */
    public void setBadgeUrls(CocClanBadgeUrls badgeUrls) {
        this.badgeUrls = badgeUrls;
    }
}
