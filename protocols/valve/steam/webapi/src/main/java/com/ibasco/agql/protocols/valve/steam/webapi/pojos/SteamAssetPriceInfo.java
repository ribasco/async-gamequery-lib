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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>SteamAssetPriceInfo class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SteamAssetPriceInfo {
    @SerializedName("prices")
    private Map<String, Integer> prices;
    @SerializedName("name")
    private String name;
    @SerializedName("date")
    private String date;
    @SerializedName("class")
    private List<SteamKeyValuePair<String, String>> classList = new ArrayList<>();
    @SerializedName("classid")
    private String classId;

    /**
     * <p>Getter for the field <code>prices</code>.</p>
     *
     * @return a {@link java.util.Map} object
     */
    public Map<String, Integer> getPrices() {
        return prices;
    }

    /**
     * <p>Setter for the field <code>prices</code>.</p>
     *
     * @param prices a {@link java.util.Map} object
     */
    public void setPrices(Map<String, Integer> prices) {
        this.prices = prices;
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

    //TODO: Return a LocalDateTime instance instead of Date string
    /**
     * <p>Getter for the field <code>date</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getDate() {
        return date;
    }

    /**
     * <p>Setter for the field <code>date</code>.</p>
     *
     * @param date a {@link java.lang.String} object
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * <p>Getter for the field <code>classList</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<SteamKeyValuePair<String, String>> getClassList() {
        return classList;
    }

    /**
     * <p>Setter for the field <code>classList</code>.</p>
     *
     * @param classList a {@link java.util.List} object
     */
    public void setClassList(List<SteamKeyValuePair<String, String>> classList) {
        this.classList = classList;
    }

    /**
     * <p>Getter for the field <code>classId</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getClassId() {
        return classId;
    }

    /**
     * <p>Setter for the field <code>classId</code>.</p>
     *
     * @param classId a {@link java.lang.String} object
     */
    public void setClassId(String classId) {
        this.classId = classId;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("ClassId", getClassId())
                .append("Name", getName())
                .append("Date", getDate())
                .append("PriceCount", getPrices().size()).toString();
    }
}
