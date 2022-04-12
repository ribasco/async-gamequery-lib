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
 * <p>SteamEconItemsSMPopularItems class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SteamEconItemsSMPopularItems {
    @SerializedName("defindex")
    private int defIndex;
    private int order;

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
     * @param defIndex a int
     */
    public void setDefIndex(int defIndex) {
        this.defIndex = defIndex;
    }

    /**
     * <p>Getter for the field <code>order</code>.</p>
     *
     * @return a int
     */
    public int getOrder() {
        return order;
    }

    /**
     * <p>Setter for the field <code>order</code>.</p>
     *
     * @param order a int
     */
    public void setOrder(int order) {
        this.order = order;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
