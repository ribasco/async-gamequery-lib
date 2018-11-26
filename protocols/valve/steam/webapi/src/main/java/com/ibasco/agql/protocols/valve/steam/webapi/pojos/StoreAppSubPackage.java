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
