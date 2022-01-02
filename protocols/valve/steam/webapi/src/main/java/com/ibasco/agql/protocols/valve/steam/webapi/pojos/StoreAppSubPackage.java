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

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public String getPercentSavingsText() {
        return percentSavingsText;
    }

    public void setPercentSavingsText(String percentSavingsText) {
        this.percentSavingsText = percentSavingsText;
    }

    public int getPercentSavings() {
        return percentSavings;
    }

    public void setPercentSavings(int percentSavings) {
        this.percentSavings = percentSavings;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public String getOptionDescription() {
        return optionDescription;
    }

    public void setOptionDescription(String optionDescription) {
        this.optionDescription = optionDescription;
    }

    public String getGetFreeLicense() {
        return getFreeLicense;
    }

    public void setGetFreeLicense(String getFreeLicense) {
        this.getFreeLicense = getFreeLicense;
    }

    public boolean isFreeLicense() {
        return freeLicense;
    }

    public void setFreeLicense(boolean freeLicense) {
        this.freeLicense = freeLicense;
    }

    public int getPriceInCentsWithDiscount() {
        return priceInCentsWithDiscount;
    }

    public void setPriceInCentsWithDiscount(int priceInCentsWithDiscount) {
        this.priceInCentsWithDiscount = priceInCentsWithDiscount;
    }
}
