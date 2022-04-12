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
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>StoreAppSpotlightInfo class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class StoreAppSpotlightInfo {
    private String name;
    @SerializedName("header_image")
    private String headerImageUrl;
    private String body;
    private String url;

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
     * @param name a {@link java.lang.String} object
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>Getter for the field <code>headerImageUrl</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getHeaderImageUrl() {
        return headerImageUrl;
    }

    /**
     * <p>Setter for the field <code>headerImageUrl</code>.</p>
     *
     * @param headerImageUrl a {@link java.lang.String} object
     */
    public void setHeaderImageUrl(String headerImageUrl) {
        this.headerImageUrl = headerImageUrl;
    }

    /**
     * <p>Getter for the field <code>body</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getBody() {
        return body;
    }

    /**
     * <p>Setter for the field <code>body</code>.</p>
     *
     * @param body a {@link java.lang.String} object
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * <p>Getter for the field <code>url</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getUrl() {
        return url;
    }

    /**
     * <p>Setter for the field <code>url</code>.</p>
     *
     * @param url a {@link java.lang.String} object
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("name", getName())
                .append("headerImageUrl", getHeaderImageUrl())
                .append("body", getBody())
                .append("url", getUrl())
                .toString();
    }
}
