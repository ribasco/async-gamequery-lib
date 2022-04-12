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

/**
 * <p>StoreDailyDealsInfo class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class StoreDailyDealsInfo {
    private int id;
    private int type;
    private boolean discounted;
    private String currency;
    @SerializedName("original_price")
    private int originalPrice;
    @SerializedName("final_price")
    private int finalPrice;
    @SerializedName("discount_percent")
    private int discountPercentage;
    private String name;
    @SerializedName("header_image")
    private String headerImageUrl;
    @SerializedName("purchase_package")
    private int purchasePackage;

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
     * @param id a int
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
     * @param type a int
     */
    public void setType(int type) {
        this.type = type;
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
     * @param discounted a boolean
     */
    public void setDiscounted(boolean discounted) {
        this.discounted = discounted;
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
     * @param currency a {@link java.lang.String} object
     */
    public void setCurrency(String currency) {
        this.currency = currency;
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
     * @param originalPrice a int
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
     * @param finalPrice a int
     */
    public void setFinalPrice(int finalPrice) {
        this.finalPrice = finalPrice;
    }

    /**
     * <p>Getter for the field <code>discountPercentage</code>.</p>
     *
     * @return a int
     */
    public int getDiscountPercentage() {
        return discountPercentage;
    }

    /**
     * <p>Setter for the field <code>discountPercentage</code>.</p>
     *
     * @param discountPercentage a int
     */
    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
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
     * @param name a {@link java.lang.String} object
     */
    public void setName(String name) {
        this.name = name;
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
     * @param headerImageUrl a {@link java.lang.String} object
     */
    public void setHeaderImageUrl(String headerImageUrl) {
        this.headerImageUrl = headerImageUrl;
    }

    /**
     * <p>Getter for the field <code>purchasePackage</code>.</p>
     *
     * @return a int
     */
    public int getPurchasePackage() {
        return purchasePackage;
    }

    /**
     * <p>Setter for the field <code>purchasePackage</code>.</p>
     *
     * @param purchasePackage a int
     */
    public void setPurchasePackage(int purchasePackage) {
        this.purchasePackage = purchasePackage;
    }
}
