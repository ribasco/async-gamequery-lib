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
import java.util.ArrayList;
import java.util.List;

/**
 * <p>SteamEconItemsSMHomepage class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SteamEconItemsSMHomepage {

    @SerializedName("home_category_id")
    private long categoryId;

    @SerializedName("popular_items")
    private List<SteamEconItemsSMPopularItems> popularItems = new ArrayList<>();

    /**
     * <p>Getter for the field <code>categoryId</code>.</p>
     *
     * @return a long
     */
    public long getCategoryId() {
        return categoryId;
    }

    /**
     * <p>Setter for the field <code>categoryId</code>.</p>
     *
     * @param categoryId
     *         a long
     */
    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * <p>Getter for the field <code>popularItems</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<SteamEconItemsSMPopularItems> getPopularItems() {
        return popularItems;
    }

    /**
     * <p>Setter for the field <code>popularItems</code>.</p>
     *
     * @param popularItems
     *         a {@link java.util.List} object
     */
    public void setPopularItems(List<SteamEconItemsSMPopularItems> popularItems) {
        this.popularItems = popularItems;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
