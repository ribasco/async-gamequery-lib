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

public class SteamEconPlayerItemAttribute<T> {
    @SerializedName("defindex")
    private int defIndex;
    private T value;
    @SerializedName("float_value")
    private float floatValue;
    @SerializedName("account_info")
    private SteamEconPlayerAccountInfo accountInfo;

    public int getDefIndex() {
        return defIndex;
    }

    public void setDefIndex(int defIndex) {
        this.defIndex = defIndex;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public float getFloatValue() {
        return floatValue;
    }

    public void setFloatValue(float floatValue) {
        this.floatValue = floatValue;
    }

    public SteamEconPlayerAccountInfo getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(SteamEconPlayerAccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("defindex", getDefIndex())
                .append("value", getValue())
                .append("floatValue", getFloatValue())
                .append("accountInfo", getAccountInfo())
                .toString();
    }
}
