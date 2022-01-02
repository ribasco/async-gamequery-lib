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
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDefIndex() {
        return defIndex;
    }

    public void setDefIndex(int defIndex) {
        this.defIndex = defIndex;
    }

    public String getAttributeClass() {
        return attributeClass;
    }

    public void setAttributeClass(String attributeClass) {
        this.attributeClass = attributeClass;
    }

    public String getDescriptionString() {
        return descriptionString;
    }

    public void setDescriptionString(String descriptionString) {
        this.descriptionString = descriptionString;
    }

    public String getDescriptionFormat() {
        return descriptionFormat;
    }

    public void setDescriptionFormat(String descriptionFormat) {
        this.descriptionFormat = descriptionFormat;
    }

    public String getEffectType() {
        return effectType;
    }

    public void setEffectType(String effectType) {
        this.effectType = effectType;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isStoredAsInteger() {
        return storedAsInteger;
    }

    public void setStoredAsInteger(boolean storedAsInteger) {
        this.storedAsInteger = storedAsInteger;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
