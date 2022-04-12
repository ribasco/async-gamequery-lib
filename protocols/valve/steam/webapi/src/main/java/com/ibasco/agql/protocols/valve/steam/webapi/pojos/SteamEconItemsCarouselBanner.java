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
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>SteamEconItemsCarouselBanner class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SteamEconItemsCarouselBanner {
    private String basefilename;
    private String action;
    private String placement;
    @SerializedName("action_param")
    private String actionParam;

    /**
     * <p>Getter for the field <code>basefilename</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getBasefilename() {
        return basefilename;
    }

    /**
     * <p>Setter for the field <code>basefilename</code>.</p>
     *
     * @param basefilename a {@link java.lang.String} object
     */
    public void setBasefilename(String basefilename) {
        this.basefilename = basefilename;
    }

    /**
     * <p>Getter for the field <code>action</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getAction() {
        return action;
    }

    /**
     * <p>Setter for the field <code>action</code>.</p>
     *
     * @param action a {@link java.lang.String} object
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * <p>Getter for the field <code>placement</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getPlacement() {
        return placement;
    }

    /**
     * <p>Setter for the field <code>placement</code>.</p>
     *
     * @param placement a {@link java.lang.String} object
     */
    public void setPlacement(String placement) {
        this.placement = placement;
    }

    /**
     * <p>Getter for the field <code>actionParam</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getActionParam() {
        return actionParam;
    }

    /**
     * <p>Setter for the field <code>actionParam</code>.</p>
     *
     * @param actionParam a {@link java.lang.String} object
     */
    public void setActionParam(String actionParam) {
        this.actionParam = actionParam;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
