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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDiscounted() {
        return discounted;
    }

    public void setDiscounted(boolean discounted) {
        this.discounted = discounted;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(int originalPrice) {
        this.originalPrice = originalPrice;
    }

    public int getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(int finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLargeCapsuleImageUrl() {
        return largeCapsuleImageUrl;
    }

    public void setLargeCapsuleImageUrl(String largeCapsuleImageUrl) {
        this.largeCapsuleImageUrl = largeCapsuleImageUrl;
    }

    public String getSmallCapsuleImageUrl() {
        return smallCapsuleImageUrl;
    }

    public void setSmallCapsuleImageUrl(String smallCapsuleImageUrl) {
        this.smallCapsuleImageUrl = smallCapsuleImageUrl;
    }

    public boolean isAvailableOnWindows() {
        return availableOnWindows;
    }

    public void setAvailableOnWindows(boolean availableOnWindows) {
        this.availableOnWindows = availableOnWindows;
    }

    public boolean isAvailableOnMac() {
        return availableOnMac;
    }

    public void setAvailableOnMac(boolean availableOnMac) {
        this.availableOnMac = availableOnMac;
    }

    public boolean isAvailableOnLinux() {
        return availableOnLinux;
    }

    public void setAvailableOnLinux(boolean availableOnLinux) {
        this.availableOnLinux = availableOnLinux;
    }

    public boolean isStreamingVideoAvailable() {
        return streamingVideoAvailable;
    }

    public void setStreamingVideoAvailable(boolean streamingVideoAvailable) {
        this.streamingVideoAvailable = streamingVideoAvailable;
    }

    public long getDiscountExpirationDate() {
        return discountExpirationDate;
    }

    public void setDiscountExpirationDate(long discountExpirationDate) {
        this.discountExpirationDate = discountExpirationDate;
    }

    public String getHeaderImageUrl() {
        return headerImageUrl;
    }

    public void setHeaderImageUrl(String headerImageUrl) {
        this.headerImageUrl = headerImageUrl;
    }

    public String getControllerSupport() {
        return controllerSupport;
    }

    public void setControllerSupport(String controllerSupport) {
        this.controllerSupport = controllerSupport;
    }

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
}
