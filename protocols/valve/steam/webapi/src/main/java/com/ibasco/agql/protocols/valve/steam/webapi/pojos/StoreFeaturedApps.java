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

import java.util.ArrayList;
import java.util.List;

/**
 * <p>StoreFeaturedApps class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class StoreFeaturedApps {
    @SerializedName("large_capsules")
    private List<StoreFeaturedAppInfo> largeCapsulesGames = new ArrayList<>();
    @SerializedName("featured_win")
    private List<StoreFeaturedAppInfo> windowsFeaturedGames = new ArrayList<>();
    @SerializedName("featured_mac")
    private List<StoreFeaturedAppInfo> macFeaturedGames = new ArrayList<>();
    @SerializedName("featured_linux")
    private List<StoreFeaturedAppInfo> linuxFeaturedGames = new ArrayList<>();
    private String layout;
    private int status;

    /**
     * <p>Getter for the field <code>largeCapsulesGames</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<StoreFeaturedAppInfo> getLargeCapsulesGames() {
        return largeCapsulesGames;
    }

    /**
     * <p>Setter for the field <code>largeCapsulesGames</code>.</p>
     *
     * @param largeCapsulesGames a {@link java.util.List} object
     */
    public void setLargeCapsulesGames(List<StoreFeaturedAppInfo> largeCapsulesGames) {
        this.largeCapsulesGames = largeCapsulesGames;
    }

    /**
     * <p>Getter for the field <code>windowsFeaturedGames</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<StoreFeaturedAppInfo> getWindowsFeaturedGames() {
        return windowsFeaturedGames;
    }

    /**
     * <p>Setter for the field <code>windowsFeaturedGames</code>.</p>
     *
     * @param windowsFeaturedGames a {@link java.util.List} object
     */
    public void setWindowsFeaturedGames(List<StoreFeaturedAppInfo> windowsFeaturedGames) {
        this.windowsFeaturedGames = windowsFeaturedGames;
    }

    /**
     * <p>Getter for the field <code>macFeaturedGames</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<StoreFeaturedAppInfo> getMacFeaturedGames() {
        return macFeaturedGames;
    }

    /**
     * <p>Setter for the field <code>macFeaturedGames</code>.</p>
     *
     * @param macFeaturedGames a {@link java.util.List} object
     */
    public void setMacFeaturedGames(List<StoreFeaturedAppInfo> macFeaturedGames) {
        this.macFeaturedGames = macFeaturedGames;
    }

    /**
     * <p>Getter for the field <code>linuxFeaturedGames</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<StoreFeaturedAppInfo> getLinuxFeaturedGames() {
        return linuxFeaturedGames;
    }

    /**
     * <p>Setter for the field <code>linuxFeaturedGames</code>.</p>
     *
     * @param linuxFeaturedGames a {@link java.util.List} object
     */
    public void setLinuxFeaturedGames(List<StoreFeaturedAppInfo> linuxFeaturedGames) {
        this.linuxFeaturedGames = linuxFeaturedGames;
    }

    /**
     * <p>Getter for the field <code>layout</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getLayout() {
        return layout;
    }

    /**
     * <p>Setter for the field <code>layout</code>.</p>
     *
     * @param layout a {@link java.lang.String} object
     */
    public void setLayout(String layout) {
        this.layout = layout;
    }

    /**
     * <p>Getter for the field <code>status</code>.</p>
     *
     * @return a int
     */
    public int getStatus() {
        return status;
    }

    /**
     * <p>Setter for the field <code>status</code>.</p>
     *
     * @param status a int
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("totalLargeCapsuleGames", getLargeCapsulesGames().size())
                .append("totalWindowsFeaturedGames", getWindowsFeaturedGames().size())
                .append("totalMacFeaturedGames", getMacFeaturedGames().size())
                .append("totalLinuxFeaturedGames", getLinuxFeaturedGames().size())
                .append("layout", getLayout())
                .append("status", getStatus())
                .toString();
    }
}
