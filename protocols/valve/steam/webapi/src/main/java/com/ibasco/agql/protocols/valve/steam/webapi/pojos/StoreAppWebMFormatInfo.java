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

/**
 * <p>StoreAppWebMFormatInfo class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class StoreAppWebMFormatInfo {

    @SerializedName("480")
    private String lowResUrl;

    @SerializedName("max")
    private String maxResUrl;

    /**
     * <p>Getter for the field <code>lowResUrl</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getLowResUrl() {
        return lowResUrl;
    }

    /**
     * <p>Setter for the field <code>lowResUrl</code>.</p>
     *
     * @param lowResUrl
     *         a {@link java.lang.String} object
     */
    public void setLowResUrl(String lowResUrl) {
        this.lowResUrl = lowResUrl;
    }

    /**
     * <p>Getter for the field <code>maxResUrl</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getMaxResUrl() {
        return maxResUrl;
    }

    /**
     * <p>Setter for the field <code>maxResUrl</code>.</p>
     *
     * @param maxResUrl
     *         a {@link java.lang.String} object
     */
    public void setMaxResUrl(String maxResUrl) {
        this.maxResUrl = maxResUrl;
    }
}
