/*
 * Copyright 2018-2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
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

    public List<StoreFeaturedAppInfo> getLargeCapsulesGames() {
        return largeCapsulesGames;
    }

    public void setLargeCapsulesGames(List<StoreFeaturedAppInfo> largeCapsulesGames) {
        this.largeCapsulesGames = largeCapsulesGames;
    }

    public List<StoreFeaturedAppInfo> getWindowsFeaturedGames() {
        return windowsFeaturedGames;
    }

    public void setWindowsFeaturedGames(List<StoreFeaturedAppInfo> windowsFeaturedGames) {
        this.windowsFeaturedGames = windowsFeaturedGames;
    }

    public List<StoreFeaturedAppInfo> getMacFeaturedGames() {
        return macFeaturedGames;
    }

    public void setMacFeaturedGames(List<StoreFeaturedAppInfo> macFeaturedGames) {
        this.macFeaturedGames = macFeaturedGames;
    }

    public List<StoreFeaturedAppInfo> getLinuxFeaturedGames() {
        return linuxFeaturedGames;
    }

    public void setLinuxFeaturedGames(List<StoreFeaturedAppInfo> linuxFeaturedGames) {
        this.linuxFeaturedGames = linuxFeaturedGames;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

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
