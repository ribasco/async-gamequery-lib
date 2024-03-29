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
public class CocLocation {

    private long id;

    private String name;

    private boolean isCountry;

    private String countryCode;

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("countryCode", getCountryCode())
                .append("isCountry", isCountry())
                .toString();
    }

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
     * @param id
     *         a long
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
     * @param name
     *         a {@link java.lang.String} object
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>Getter for the field <code>countryCode</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * <p>isCountry.</p>
     *
     * @return a boolean
     */
    public boolean isCountry() {
        return isCountry;
    }

    /**
     * <p>setCountry.</p>
     *
     * @param country
     *         a boolean
     */
    public void setCountry(boolean country) {
        isCountry = country;
    }

    /**
     * <p>Setter for the field <code>countryCode</code>.</p>
     *
     * @param countryCode
     *         a {@link java.lang.String} object
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
