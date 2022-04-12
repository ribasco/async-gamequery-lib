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
 * <p>StorePackagePriceInfo class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class StorePackagePriceInfo {
    private String currency;
    @SerializedName("initial")
    private int initialPrice;
    @SerializedName("final")
    private int finalPrice;
    @SerializedName("discount_percent")
    private int discountPercent;
    private int individual;

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
     * <p>Getter for the field <code>initialPrice</code>.</p>
     *
     * @return a int
     */
    public int getInitialPrice() {
        return initialPrice;
    }

    /**
     * <p>Setter for the field <code>initialPrice</code>.</p>
     *
     * @param initialPrice a int
     */
    public void setInitialPrice(int initialPrice) {
        this.initialPrice = initialPrice;
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
     * @param discountPercent a int
     */
    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    /**
     * <p>Getter for the field <code>individual</code>.</p>
     *
     * @return a int
     */
    public int getIndividual() {
        return individual;
    }

    /**
     * <p>Setter for the field <code>individual</code>.</p>
     *
     * @param individual a int
     */
    public void setIndividual(int individual) {
        this.individual = individual;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("currency", getCurrency())
                .append("initialPrice", getInitialPrice())
                .append("finalPrice", getFinalPrice())
                .append("individual", getIndividual())
                .toString();
    }
}
