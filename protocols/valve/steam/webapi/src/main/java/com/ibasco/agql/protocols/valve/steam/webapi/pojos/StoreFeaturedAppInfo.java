/*
 * MIT License
 *
 * Copyright (c) 2018 Asynchronous Game Query Library
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
