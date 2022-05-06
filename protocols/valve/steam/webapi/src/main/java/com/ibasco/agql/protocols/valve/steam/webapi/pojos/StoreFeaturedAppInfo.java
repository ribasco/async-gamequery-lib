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

/**
 * <p>StoreFeaturedAppInfo class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class StoreFeaturedAppInfo {

    private int id;

    private int type;

    private String name;

    private boolean discounted;

    @SerializedName("discount_percent")
    private int discountPercent;

    @SerializedName("original_price")
    private int originalPrice;

    @SerializedName("final_price")
    private int finalPrice;

    private String currency;

    @SerializedName("large_capsule_image")
    private String largeCapsuleImageUrl;

    @SerializedName("small_capsule_image")
    private String smallCapsuleImageUrl;

    @SerializedName("windows_available")
    private boolean availableOnWindows;

    @SerializedName("mac_available")
    private boolean availableOnMac;

    @SerializedName("linux_available")
    private boolean availableOnLinux;

    @SerializedName("streamingvideo_available")
    private boolean streamingVideoAvailable;

    @SerializedName("discount_expiration")
    private long discountExpirationDate;

    @SerializedName("header_image")
    private String headerImageUrl;

    @SerializedName("controller_support")
    private String controllerSupport;

    /**
     * <p>Getter for the field <code>largeCapsuleImageUrl</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getLargeCapsuleImageUrl() {
        return largeCapsuleImageUrl;
    }

    /**
     * <p>Setter for the field <code>largeCapsuleImageUrl</code>.</p>
     *
     * @param largeCapsuleImageUrl
     *         a {@link java.lang.String} object
     */
    public void setLargeCapsuleImageUrl(String largeCapsuleImageUrl) {
        this.largeCapsuleImageUrl = largeCapsuleImageUrl;
    }

    /**
     * <p>Getter for the field <code>smallCapsuleImageUrl</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getSmallCapsuleImageUrl() {
        return smallCapsuleImageUrl;
    }

    /**
     * <p>Setter for the field <code>smallCapsuleImageUrl</code>.</p>
     *
     * @param smallCapsuleImageUrl
     *         a {@link java.lang.String} object
     */
    public void setSmallCapsuleImageUrl(String smallCapsuleImageUrl) {
        this.smallCapsuleImageUrl = smallCapsuleImageUrl;
    }

    /**
     * <p>isStreamingVideoAvailable.</p>
     *
     * @return a boolean
     */
    public boolean isStreamingVideoAvailable() {
        return streamingVideoAvailable;
    }

    /**
     * <p>Setter for the field <code>streamingVideoAvailable</code>.</p>
     *
     * @param streamingVideoAvailable
     *         a boolean
     */
    public void setStreamingVideoAvailable(boolean streamingVideoAvailable) {
        this.streamingVideoAvailable = streamingVideoAvailable;
    }

    /**
     * <p>Getter for the field <code>headerImageUrl</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getHeaderImageUrl() {
        return headerImageUrl;
    }

    /**
     * <p>Setter for the field <code>headerImageUrl</code>.</p>
     *
     * @param headerImageUrl
     *         a {@link java.lang.String} object
     */
    public void setHeaderImageUrl(String headerImageUrl) {
        this.headerImageUrl = headerImageUrl;
    }

    /**
     * <p>Getter for the field <code>controllerSupport</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getControllerSupport() {
        return controllerSupport;
    }

    /**
     * <p>Setter for the field <code>controllerSupport</code>.</p>
     *
     * @param controllerSupport
     *         a {@link java.lang.String} object
     */
    public void setControllerSupport(String controllerSupport) {
        this.controllerSupport = controllerSupport;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("id", getId())
                .append("type", getType())
                .append("name", getName())
                .append("isDiscounted", isDiscounted())
                .append("discountPct", getDiscountPercent())
                .append("origPrice", getOriginalPrice())
                .append("finalPrice", getFinalPrice())
                .append("currency", getCurrency())
                .append("isAvailableOnWindows", isAvailableOnWindows())
                .append("isAvailableOnMac", isAvailableOnMac())
                .append("isAvailableOnLinux", isAvailableOnLinux())
                .append("discountExpirationDate", getDiscountExpirationDate())
                .toString();
    }

    /**
     * <p>Getter for the field <code>id</code>.</p>
     *
     * @return a int
     */
    public int getId() {
        return id;
    }

    /**
     * <p>Setter for the field <code>id</code>.</p>
     *
     * @param id
     *         a int
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * <p>Getter for the field <code>type</code>.</p>
     *
     * @return a int
     */
    public int getType() {
        return type;
    }

    /**
     * <p>Setter for the field <code>type</code>.</p>
     *
     * @param type
     *         a int
     */
    public void setType(int type) {
        this.type = type;
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
     * <p>isDiscounted.</p>
     *
     * @return a boolean
     */
    public boolean isDiscounted() {
        return discounted;
    }

    /**
     * <p>Setter for the field <code>discounted</code>.</p>
     *
     * @param discounted
     *         a boolean
     */
    public void setDiscounted(boolean discounted) {
        this.discounted = discounted;
    }

    /**
     * <p>Getter for the field <code>discountPercent</code>.</p>
     *
     * @return a int
     */
    public int getDiscountPercent() {
        return discountPercent;
    }

    /**
     * <p>Setter for the field <code>discountPercent</code>.</p>
     *
     * @param discountPercent
     *         a int
     */
    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    /**
     * <p>Getter for the field <code>originalPrice</code>.</p>
     *
     * @return a int
     */
    public int getOriginalPrice() {
        return originalPrice;
    }

    /**
     * <p>Setter for the field <code>originalPrice</code>.</p>
     *
     * @param originalPrice
     *         a int
     */
    public void setOriginalPrice(int originalPrice) {
        this.originalPrice = originalPrice;
    }

    /**
     * <p>Getter for the field <code>finalPrice</code>.</p>
     *
     * @return a int
     */
    public int getFinalPrice() {
        return finalPrice;
    }

    /**
     * <p>Setter for the field <code>finalPrice</code>.</p>
     *
     * @param finalPrice
     *         a int
     */
    public void setFinalPrice(int finalPrice) {
        this.finalPrice = finalPrice;
    }

    /**
     * <p>Getter for the field <code>currency</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * <p>Setter for the field <code>currency</code>.</p>
     *
     * @param currency
     *         a {@link java.lang.String} object
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * <p>isAvailableOnWindows.</p>
     *
     * @return a boolean
     */
    public boolean isAvailableOnWindows() {
        return availableOnWindows;
    }

    /**
     * <p>Setter for the field <code>availableOnWindows</code>.</p>
     *
     * @param availableOnWindows
     *         a boolean
     */
    public void setAvailableOnWindows(boolean availableOnWindows) {
        this.availableOnWindows = availableOnWindows;
    }

    /**
     * <p>isAvailableOnMac.</p>
     *
     * @return a boolean
     */
    public boolean isAvailableOnMac() {
        return availableOnMac;
    }

    /**
     * <p>Setter for the field <code>availableOnMac</code>.</p>
     *
     * @param availableOnMac
     *         a boolean
     */
    public void setAvailableOnMac(boolean availableOnMac) {
        this.availableOnMac = availableOnMac;
    }

    /**
     * <p>isAvailableOnLinux.</p>
     *
     * @return a boolean
     */
    public boolean isAvailableOnLinux() {
        return availableOnLinux;
    }

    /**
     * <p>Setter for the field <code>availableOnLinux</code>.</p>
     *
     * @param availableOnLinux
     *         a boolean
     */
    public void setAvailableOnLinux(boolean availableOnLinux) {
        this.availableOnLinux = availableOnLinux;
    }

    /**
     * <p>Getter for the field <code>discountExpirationDate</code>.</p>
     *
     * @return a long
     */
    public long getDiscountExpirationDate() {
        return discountExpirationDate;
    }

    /**
     * <p>Setter for the field <code>discountExpirationDate</code>.</p>
     *
     * @param discountExpirationDate
     *         a long
     */
    public void setDiscountExpirationDate(long discountExpirationDate) {
        this.discountExpirationDate = discountExpirationDate;
    }
}
