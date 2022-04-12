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
 * <p>SteamEconItemsSMCarouselData class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SteamEconItemsSMCarouselData {
    @SerializedName("max_display_banners")
    private int maxDisplayBanners;
    private List<SteamEconItemsCarouselBanner> banners = new ArrayList<>();

    /**
     * <p>Getter for the field <code>maxDisplayBanners</code>.</p>
     *
     * @return a int
     */
    public int getMaxDisplayBanners() {
        return maxDisplayBanners;
    }

    /**
     * <p>Setter for the field <code>maxDisplayBanners</code>.</p>
     *
     * @param maxDisplayBanners a int
     */
    public void setMaxDisplayBanners(int maxDisplayBanners) {
        this.maxDisplayBanners = maxDisplayBanners;
    }

    /**
     * <p>Getter for the field <code>banners</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<SteamEconItemsCarouselBanner> getBanners() {
        return banners;
    }

    /**
     * <p>Setter for the field <code>banners</code>.</p>
     *
     * @param banners a {@link java.util.List} object
     */
    public void setBanners(List<SteamEconItemsCarouselBanner> banners) {
        this.banners = banners;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
