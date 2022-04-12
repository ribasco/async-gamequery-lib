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
 * <p>StoreAppSubPackage class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class StoreAppSubPackage {
    @SerializedName("packageid")
    private int packageId;
    @SerializedName("percent_savings_text")
    private String percentSavingsText;
    @SerializedName("percent_savings")
    private int percentSavings;
    @SerializedName("option_text")
    private String optionText;
    @SerializedName("option_description")
    private String optionDescription;
    @SerializedName("can_get_free_license")
    private String getFreeLicense; //boolean value
    @SerializedName("is_free_license")
    private boolean freeLicense;
    @SerializedName("price_in_cents_with_discount")
    private int priceInCentsWithDiscount;

    /**
     * <p>Getter for the field <code>packageId</code>.</p>
     *
     * @return a int
     */
    public int getPackageId() {
        return packageId;
    }

    /**
     * <p>Setter for the field <code>packageId</code>.</p>
     *
     * @param packageId a int
     */
    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    /**
     * <p>Getter for the field <code>percentSavingsText</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getPercentSavingsText() {
        return percentSavingsText;
    }

    /**
     * <p>Setter for the field <code>percentSavingsText</code>.</p>
     *
     * @param percentSavingsText a {@link java.lang.String} object
     */
    public void setPercentSavingsText(String percentSavingsText) {
        this.percentSavingsText = percentSavingsText;
    }

    /**
     * <p>Getter for the field <code>percentSavings</code>.</p>
     *
     * @return a int
     */
    public int getPercentSavings() {
        return percentSavings;
    }

    /**
     * <p>Setter for the field <code>percentSavings</code>.</p>
     *
     * @param percentSavings a int
     */
    public void setPercentSavings(int percentSavings) {
        this.percentSavings = percentSavings;
    }

    /**
     * <p>Getter for the field <code>optionText</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getOptionText() {
        return optionText;
    }

    /**
     * <p>Setter for the field <code>optionText</code>.</p>
     *
     * @param optionText a {@link java.lang.String} object
     */
    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    /**
     * <p>Getter for the field <code>optionDescription</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getOptionDescription() {
        return optionDescription;
    }

    /**
     * <p>Setter for the field <code>optionDescription</code>.</p>
     *
     * @param optionDescription a {@link java.lang.String} object
     */
    public void setOptionDescription(String optionDescription) {
        this.optionDescription = optionDescription;
    }

    /**
     * <p>Getter for the field <code>getFreeLicense</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getGetFreeLicense() {
        return getFreeLicense;
    }

    /**
     * <p>Setter for the field <code>getFreeLicense</code>.</p>
     *
     * @param getFreeLicense a {@link java.lang.String} object
     */
    public void setGetFreeLicense(String getFreeLicense) {
        this.getFreeLicense = getFreeLicense;
    }

    /**
     * <p>isFreeLicense.</p>
     *
     * @return a boolean
     */
    public boolean isFreeLicense() {
        return freeLicense;
    }

    /**
     * <p>Setter for the field <code>freeLicense</code>.</p>
     *
     * @param freeLicense a boolean
     */
    public void setFreeLicense(boolean freeLicense) {
        this.freeLicense = freeLicense;
    }

    /**
     * <p>Getter for the field <code>priceInCentsWithDiscount</code>.</p>
     *
     * @return a int
     */
    public int getPriceInCentsWithDiscount() {
        return priceInCentsWithDiscount;
    }

    /**
     * <p>Setter for the field <code>priceInCentsWithDiscount</code>.</p>
     *
     * @param priceInCentsWithDiscount a int
     */
    public void setPriceInCentsWithDiscount(int priceInCentsWithDiscount) {
        this.priceInCentsWithDiscount = priceInCentsWithDiscount;
    }
}
