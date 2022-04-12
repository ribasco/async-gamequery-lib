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

import java.util.ArrayList;
import java.util.List;

/**
 * <p>StoreSaleDetails class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class StoreSaleDetails {
    private int status;
    private int id;
    private String name;
    @SerializedName("available")
    private String availableDate;
    @SerializedName("items")
    private List<StoreSaleApp> appsOnSale = new ArrayList<>();

    /**
     * <p>Getter for the field <code>status</code>.</p>
     *
     * @return a int
     */
    public int getStatus() {
        return status;
    }

    /**
     * <p>Setter for the field <code>status</code>.</p>
     *
     * @param status a int
     */
    public void setStatus(int status) {
        this.status = status;
    }

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
     * <p>Getter for the field <code>availableDate</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getAvailableDate() {
        return availableDate;
    }

    /**
     * <p>Setter for the field <code>availableDate</code>.</p>
     *
     * @param availableDate a {@link java.lang.String} object
     */
    public void setAvailableDate(String availableDate) {
        this.availableDate = availableDate;
    }

    /**
     * <p>Getter for the field <code>appsOnSale</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<StoreSaleApp> getAppsOnSale() {
        return appsOnSale;
    }

    /**
     * <p>Setter for the field <code>appsOnSale</code>.</p>
     *
     * @param appsOnSale a {@link java.util.List} object
     */
    public void setAppsOnSale(List<StoreSaleApp> appsOnSale) {
        this.appsOnSale = appsOnSale;
    }
}
