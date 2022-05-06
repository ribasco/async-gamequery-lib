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
 * <p>StorePackageDetails class.</p>
 *
 * @author Rafael Luis Ibasco
 */
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

    /**
     * <p>Getter for the field <code>pageImageUrl</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getPageImageUrl() {
        return pageImageUrl;
    }

    /**
     * <p>Setter for the field <code>pageImageUrl</code>.</p>
     *
     * @param pageImageUrl
     *         a {@link java.lang.String} object
     */
    public void setPageImageUrl(String pageImageUrl) {
        this.pageImageUrl = pageImageUrl;
    }

    /**
     * <p>Getter for the field <code>smallLogoUrl</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getSmallLogoUrl() {
        return smallLogoUrl;
    }

    /**
     * <p>Setter for the field <code>smallLogoUrl</code>.</p>
     *
     * @param smallLogoUrl
     *         a {@link java.lang.String} object
     */
    public void setSmallLogoUrl(String smallLogoUrl) {
        this.smallLogoUrl = smallLogoUrl;
    }

    /**
     * <p>Getter for the field <code>controller</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreGameControllerInfo} object
     */
    public StoreGameControllerInfo getController() {
        return controller;
    }

    /**
     * <p>Setter for the field <code>controller</code>.</p>
     *
     * @param controller
     *         a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreGameControllerInfo} object
     */
    public void setController(StoreGameControllerInfo controller) {
        this.controller = controller;
    }

    /** {@inheritDoc} */
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
     * <p>Getter for the field <code>apps</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<StorePackageAppInfo> getApps() {
        return apps;
    }

    /**
     * <p>Setter for the field <code>apps</code>.</p>
     *
     * @param apps
     *         a {@link java.util.List} object
     */
    public void setApps(List<StorePackageAppInfo> apps) {
        this.apps = apps;
    }

    /**
     * <p>Getter for the field <code>priceInfo</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StorePackagePriceInfo} object
     */
    public StorePackagePriceInfo getPriceInfo() {
        return priceInfo;
    }

    /**
     * <p>Setter for the field <code>priceInfo</code>.</p>
     *
     * @param priceInfo
     *         a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StorePackagePriceInfo} object
     */
    public void setPriceInfo(StorePackagePriceInfo priceInfo) {
        this.priceInfo = priceInfo;
    }

    /**
     * <p>Getter for the field <code>platforms</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreAppPlatform} object
     */
    public StoreAppPlatform getPlatforms() {
        return platforms;
    }

    /**
     * <p>Setter for the field <code>platforms</code>.</p>
     *
     * @param platforms
     *         a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreAppPlatform} object
     */
    public void setPlatforms(StoreAppPlatform platforms) {
        this.platforms = platforms;
    }

    /**
     * <p>Getter for the field <code>releaseDateInfo</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreAppReleaseDateInfo} object
     */
    public StoreAppReleaseDateInfo getReleaseDateInfo() {
        return releaseDateInfo;
    }

    /**
     * <p>Setter for the field <code>releaseDateInfo</code>.</p>
     *
     * @param releaseDateInfo
     *         a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreAppReleaseDateInfo} object
     */
    public void setReleaseDateInfo(StoreAppReleaseDateInfo releaseDateInfo) {
        this.releaseDateInfo = releaseDateInfo;
    }
}
