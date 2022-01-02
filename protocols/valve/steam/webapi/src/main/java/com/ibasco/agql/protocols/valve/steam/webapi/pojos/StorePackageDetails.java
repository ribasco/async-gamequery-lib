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

public class StorePackageDetails {
    private String name;
    @SerializedName("page_image")
    private String pageImageUrl;
    @SerializedName("small_logo")
    private String smallLogoUrl;
    private List<StorePackageAppInfo> apps = new ArrayList<>();
    @SerializedName("price")
    private StorePackagePriceInfo priceInfo;
    private StoreAppPlatform platforms;
    private StoreGameControllerInfo controller;
    @SerializedName("release_date")
    private StoreAppReleaseDateInfo releaseDateInfo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPageImageUrl() {
        return pageImageUrl;
    }

    public void setPageImageUrl(String pageImageUrl) {
        this.pageImageUrl = pageImageUrl;
    }

    public String getSmallLogoUrl() {
        return smallLogoUrl;
    }

    public void setSmallLogoUrl(String smallLogoUrl) {
        this.smallLogoUrl = smallLogoUrl;
    }

    public List<StorePackageAppInfo> getApps() {
        return apps;
    }

    public void setApps(List<StorePackageAppInfo> apps) {
        this.apps = apps;
    }

    public StorePackagePriceInfo getPriceInfo() {
        return priceInfo;
    }

    public void setPriceInfo(StorePackagePriceInfo priceInfo) {
        this.priceInfo = priceInfo;
    }

    public StoreAppPlatform getPlatforms() {
        return platforms;
    }

    public void setPlatforms(StoreAppPlatform platforms) {
        this.platforms = platforms;
    }

    public StoreGameControllerInfo getController() {
        return controller;
    }

    public void setController(StoreGameControllerInfo controller) {
        this.controller = controller;
    }

    public StoreAppReleaseDateInfo getReleaseDateInfo() {
        return releaseDateInfo;
    }

    public void setReleaseDateInfo(StoreAppReleaseDateInfo releaseDateInfo) {
        this.releaseDateInfo = releaseDateInfo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("name", getName())
                .append("packageApps", getApps())
                .append("priceInfo", getPriceInfo())
                .append("platforms", getPlatforms())
                .append("releaseDateInfo", getReleaseDateInfo())
                .toString();
    }
}
