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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public Map<String, Integer> getPrices() {
        return prices;
    }

    public void setPrices(Map<String, Integer> prices) {
        this.prices = prices;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //TODO: Return a LocalDateTime instance instead of Date string
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<SteamKeyValuePair<String, String>> getClassList() {
        return classList;
    }

    public void setClassList(List<SteamKeyValuePair<String, String>> classList) {
        this.classList = classList;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("ClassId", getClassId())
                .append("Name", getName())
                .append("Date", getDate())
                .append("PriceCount", getPrices().size()).toString();
    }
}
