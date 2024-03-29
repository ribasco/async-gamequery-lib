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

package com.ibasco.agql.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

//TODO: Add a custom type adapter for this class

/**
 * <p>StoreAppReleaseDateInfo class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class StoreAppReleaseDateInfo {

    @SerializedName("coming_soon")
    private boolean comingSoon;

    @SerializedName("date")
    private String releaseDate;

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("comingSoon", isComingSoon())
                .append("releaseDate", getReleaseDate())
                .toString();
    }

    /**
     * <p>isComingSoon.</p>
     *
     * @return a boolean
     */
    public boolean isComingSoon() {
        return comingSoon;
    }

    /**
     * <p>Setter for the field <code>comingSoon</code>.</p>
     *
     * @param comingSoon
     *         a boolean
     */
    public void setComingSoon(boolean comingSoon) {
        this.comingSoon = comingSoon;
    }

    /**
     * <p>Getter for the field <code>releaseDate</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * <p>Setter for the field <code>releaseDate</code>.</p>
     *
     * @param releaseDate
     *         a {@link java.lang.String} object
     */
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
