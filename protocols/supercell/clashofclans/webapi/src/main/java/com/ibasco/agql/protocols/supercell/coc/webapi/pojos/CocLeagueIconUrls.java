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
 * <p>CocLeagueIconUrls class.</p>
 *
 * @author Rafael Luis Ibasco
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
public class CocLeagueIconUrls {

    private String small;

    private String tiny;

    private String medium;

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("tiny", getTiny())
                .append("small", getSmall())
                .append("medium", getMedium())
                .toString();
    }

    /**
     * <p>Getter for the field <code>tiny</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getTiny() {
        return tiny;
    }

    /**
     * <p>Getter for the field <code>small</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getSmall() {
        return small;
    }

    /**
     * <p>Setter for the field <code>small</code>.</p>
     *
     * @param small
     *         a {@link java.lang.String} object
     */
    public void setSmall(String small) {
        this.small = small;
    }

    /**
     * <p>Getter for the field <code>medium</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getMedium() {
        return medium;
    }

    /**
     * <p>Setter for the field <code>medium</code>.</p>
     *
     * @param medium
     *         a {@link java.lang.String} object
     */
    public void setMedium(String medium) {
        this.medium = medium;
    }

    /**
     * <p>Setter for the field <code>tiny</code>.</p>
     *
     * @param tiny
     *         a {@link java.lang.String} object
     */
    public void setTiny(String tiny) {
        this.tiny = tiny;
    }
}
