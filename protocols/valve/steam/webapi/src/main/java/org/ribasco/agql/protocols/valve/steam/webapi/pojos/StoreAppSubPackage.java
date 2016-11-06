package org.ribasco.agql.protocols.valve.steam.webapi.pojos;

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
