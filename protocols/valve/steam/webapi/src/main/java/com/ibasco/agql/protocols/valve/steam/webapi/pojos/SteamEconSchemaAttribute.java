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
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>SteamEconSchemaAttribute class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SteamEconSchemaAttribute {

    private String name;

    @SerializedName("defindex")
    private int defIndex;

    @SerializedName("attribute_class")
    private String attributeClass;

    @SerializedName("description_string")
    private String descriptionString;

    @SerializedName("description_format")
    private String descriptionFormat;

    @SerializedName("effect_type")
    private String effectType;

    @SerializedName("hidden")
    private boolean hidden;

    @SerializedName("stored_as_integer")
    private boolean storedAsInteger;

    @SerializedName("minvalue")
    private double minValue;

    @SerializedName("maxvalue")
    private double maxValue;

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
     * <p>Getter for the field <code>defIndex</code>.</p>
     *
     * @return a int
     */
    public int getDefIndex() {
        return defIndex;
    }

    /**
     * <p>Setter for the field <code>defIndex</code>.</p>
     *
     * @param defIndex
     *         a int
     */
    public void setDefIndex(int defIndex) {
        this.defIndex = defIndex;
    }

    /**
     * <p>Getter for the field <code>attributeClass</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getAttributeClass() {
        return attributeClass;
    }

    /**
     * <p>Setter for the field <code>attributeClass</code>.</p>
     *
     * @param attributeClass
     *         a {@link java.lang.String} object
     */
    public void setAttributeClass(String attributeClass) {
        this.attributeClass = attributeClass;
    }

    /**
     * <p>Getter for the field <code>descriptionString</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getDescriptionString() {
        return descriptionString;
    }

    /**
     * <p>Setter for the field <code>descriptionString</code>.</p>
     *
     * @param descriptionString
     *         a {@link java.lang.String} object
     */
    public void setDescriptionString(String descriptionString) {
        this.descriptionString = descriptionString;
    }

    /**
     * <p>Getter for the field <code>descriptionFormat</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getDescriptionFormat() {
        return descriptionFormat;
    }

    /**
     * <p>Setter for the field <code>descriptionFormat</code>.</p>
     *
     * @param descriptionFormat
     *         a {@link java.lang.String} object
     */
    public void setDescriptionFormat(String descriptionFormat) {
        this.descriptionFormat = descriptionFormat;
    }

    /**
     * <p>Getter for the field <code>effectType</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getEffectType() {
        return effectType;
    }

    /**
     * <p>Setter for the field <code>effectType</code>.</p>
     *
     * @param effectType
     *         a {@link java.lang.String} object
     */
    public void setEffectType(String effectType) {
        this.effectType = effectType;
    }

    /**
     * <p>isHidden.</p>
     *
     * @return a boolean
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * <p>Setter for the field <code>hidden</code>.</p>
     *
     * @param hidden
     *         a boolean
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    /**
     * <p>isStoredAsInteger.</p>
     *
     * @return a boolean
     */
    public boolean isStoredAsInteger() {
        return storedAsInteger;
    }

    /**
     * <p>Setter for the field <code>storedAsInteger</code>.</p>
     *
     * @param storedAsInteger
     *         a boolean
     */
    public void setStoredAsInteger(boolean storedAsInteger) {
        this.storedAsInteger = storedAsInteger;
    }

    /**
     * <p>Getter for the field <code>minValue</code>.</p>
     *
     * @return a double
     */
    public double getMinValue() {
        return minValue;
    }

    /**
     * <p>Setter for the field <code>minValue</code>.</p>
     *
     * @param minValue
     *         a double
     */
    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    /**
     * <p>Getter for the field <code>maxValue</code>.</p>
     *
     * @return a double
     */
    public double getMaxValue() {
        return maxValue;
    }

    /**
     * <p>Setter for the field <code>maxValue</code>.</p>
     *
     * @param maxValue
     *         a double
     */
    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
