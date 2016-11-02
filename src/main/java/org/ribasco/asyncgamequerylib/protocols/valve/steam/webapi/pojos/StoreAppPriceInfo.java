package org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class StoreAppPriceInfo {
    private String currency;
    @SerializedName("initial")
    private int initialPrice;
    @SerializedName("final")
    private int finalPrice;
    @SerializedName("discount_percent")
    private int discountPercent;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getInitialPrice() {
        return initialPrice;
    }

    public void setInitialPrice(int initialPrice) {
        this.initialPrice = initialPrice;
    }

    public int getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(int finalPrice) {
        this.finalPrice = finalPrice;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("currency", getCurrency())
                .append("initialPrice", getInitialPrice())
                .append("finalPrice", getFinalPrice())
                .append("discountPct", getDiscountPercent())
                .toString();
    }
}
