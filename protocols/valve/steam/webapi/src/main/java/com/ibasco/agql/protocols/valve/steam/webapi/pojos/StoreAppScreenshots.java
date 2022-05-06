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
 * <p>StoreAppScreenshots class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class StoreAppScreenshots {

    private int id;

    @SerializedName("path_thumbnail")
    private String thumbnailPath;

    @SerializedName("path_full")
    private String fullPath;

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
     * <p>Getter for the field <code>thumbnailPath</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getThumbnailPath() {
        return thumbnailPath;
    }

    /**
     * <p>Setter for the field <code>thumbnailPath</code>.</p>
     *
     * @param thumbnailPath
     *         a {@link java.lang.String} object
     */
    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    /**
     * <p>Getter for the field <code>fullPath</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getFullPath() {
        return fullPath;
    }

    /**
     * <p>Setter for the field <code>fullPath</code>.</p>
     *
     * @param fullPath
     *         a {@link java.lang.String} object
     */
    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }
}
