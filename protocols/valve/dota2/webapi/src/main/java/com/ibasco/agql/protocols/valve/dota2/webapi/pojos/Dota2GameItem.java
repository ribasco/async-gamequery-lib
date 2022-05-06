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

package com.ibasco.agql.protocols.valve.dota2.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

//TODO: Use type adapters for this class to convert the interger values to boolean types

/**
 * <p>Dota2GameItem class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class Dota2GameItem {

    private int id;

    private String name;

    private int cost;

    @SerializedName("secret_shop")
    private int secretShop;

    @SerializedName("side_shop")
    private int sideShop;

    private int recipe;

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
     * @param id
     *         a int
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
     * @param name
     *         a {@link java.lang.String} object
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>Getter for the field <code>cost</code>.</p>
     *
     * @return a int
     */
    public int getCost() {
        return cost;
    }

    /**
     * <p>Setter for the field <code>cost</code>.</p>
     *
     * @param cost
     *         a int
     */
    public void setCost(int cost) {
        this.cost = cost;
    }

    /**
     * <p>Getter for the field <code>secretShop</code>.</p>
     *
     * @return a int
     */
    public int getSecretShop() {
        return secretShop;
    }

    /**
     * <p>Setter for the field <code>secretShop</code>.</p>
     *
     * @param secretShop
     *         a int
     */
    public void setSecretShop(int secretShop) {
        this.secretShop = secretShop;
    }

    /**
     * <p>Getter for the field <code>recipe</code>.</p>
     *
     * @return a int
     */
    public int getRecipe() {
        return recipe;
    }

    /**
     * <p>Setter for the field <code>recipe</code>.</p>
     *
     * @param recipe
     *         a int
     */
    public void setRecipe(int recipe) {
        this.recipe = recipe;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
