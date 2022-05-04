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

package com.ibasco.agql.protocols.valve.steam.webapi.interfaces.steamstore.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SteamStoreApp {

    @SerializedName("appid")
    @Expose
    private Integer appid;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("last_modified")
    @Expose
    private Integer lastModified;

    @SerializedName("price_change_number")
    @Expose
    private Integer priceChangeNumber;

    public Integer getAppid() {
        return appid;
    }

    public void setAppid(Integer appid) {
        this.appid = appid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLastModified() {
        return lastModified;
    }

    public void setLastModified(Integer lastModified) {
        this.lastModified = lastModified;
    }

    public Integer getPriceChangeNumber() {
        return priceChangeNumber;
    }

    public void setPriceChangeNumber(Integer priceChangeNumber) {
        this.priceChangeNumber = priceChangeNumber;
    }

}