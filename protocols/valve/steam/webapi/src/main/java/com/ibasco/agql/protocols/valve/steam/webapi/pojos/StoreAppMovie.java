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
 * <p>StoreAppMovie class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class StoreAppMovie {

    private int id;

    private String name;

    @SerializedName("thumbnail")
    private String thumbnailUrl;

    @SerializedName("webm")
    private StoreAppWebMFormatInfo webMFormatInfo;

    private boolean highlight;

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
     * <p>Getter for the field <code>thumbnailUrl</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    /**
     * <p>Setter for the field <code>thumbnailUrl</code>.</p>
     *
     * @param thumbnailUrl
     *         a {@link java.lang.String} object
     */
    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    /**
     * <p>Getter for the field <code>webMFormatInfo</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreAppWebMFormatInfo} object
     */
    public StoreAppWebMFormatInfo getWebMFormatInfo() {
        return webMFormatInfo;
    }

    /**
     * <p>Setter for the field <code>webMFormatInfo</code>.</p>
     *
     * @param webMFormatInfo
     *         a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreAppWebMFormatInfo} object
     */
    public void setWebMFormatInfo(StoreAppWebMFormatInfo webMFormatInfo) {
        this.webMFormatInfo = webMFormatInfo;
    }

    /**
     * <p>isHighlight.</p>
     *
     * @return a boolean
     */
    public boolean isHighlight() {
        return highlight;
    }

    /**
     * <p>Setter for the field <code>highlight</code>.</p>
     *
     * @param highlight
     *         a boolean
     */
    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }
}
