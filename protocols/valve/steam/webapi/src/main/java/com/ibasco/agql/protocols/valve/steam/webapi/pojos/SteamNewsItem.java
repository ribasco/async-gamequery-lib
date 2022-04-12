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
 * Created by raffy on 10/26/2016.
 *
 * @author Rafael Luis Ibasco
 */
public class SteamNewsItem {
    @SerializedName("gid")
    private String gid;
    @SerializedName("title")
    private String title;
    @SerializedName("url")
    private String url;
    @SerializedName("is_external_url")
    private boolean externalUrl;
    @SerializedName("author")
    private String author;
    @SerializedName("contents")
    private String contents;
    @SerializedName("feedlabel")
    private String feedLabel;
    @SerializedName("date")
    private int date;
    @SerializedName("feedname")
    private String feedName;

    /**
     * <p>Getter for the field <code>gid</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getGid() {
        return gid;
    }

    /**
     * <p>Setter for the field <code>gid</code>.</p>
     *
     * @param gid a {@link java.lang.String} object
     */
    public void setGid(String gid) {
        this.gid = gid;
    }

    /**
     * <p>Getter for the field <code>title</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getTitle() {
        return title;
    }

    /**
     * <p>Setter for the field <code>title</code>.</p>
     *
     * @param title a {@link java.lang.String} object
     */
    public void setTitle(String title) {
        this.title = title;
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

    /**
     * <p>isExternalUrl.</p>
     *
     * @return a boolean
     */
    public boolean isExternalUrl() {
        return externalUrl;
    }

    /**
     * <p>Setter for the field <code>externalUrl</code>.</p>
     *
     * @param externalUrl a boolean
     */
    public void setExternalUrl(boolean externalUrl) {
        this.externalUrl = externalUrl;
    }

    /**
     * <p>Getter for the field <code>author</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getAuthor() {
        return author;
    }

    /**
     * <p>Setter for the field <code>author</code>.</p>
     *
     * @param author a {@link java.lang.String} object
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * <p>Getter for the field <code>contents</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getContents() {
        return contents;
    }

    /**
     * <p>Setter for the field <code>contents</code>.</p>
     *
     * @param contents a {@link java.lang.String} object
     */
    public void setContents(String contents) {
        this.contents = contents;
    }

    /**
     * <p>Getter for the field <code>feedLabel</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getFeedLabel() {
        return feedLabel;
    }

    /**
     * <p>Setter for the field <code>feedLabel</code>.</p>
     *
     * @param feedLabel a {@link java.lang.String} object
     */
    public void setFeedLabel(String feedLabel) {
        this.feedLabel = feedLabel;
    }

    /**
     * <p>Getter for the field <code>date</code>.</p>
     *
     * @return a int
     */
    public int getDate() {
        return date;
    }

    /**
     * <p>Setter for the field <code>date</code>.</p>
     *
     * @param date a int
     */
    public void setDate(int date) {
        this.date = date;
    }

    /**
     * <p>Getter for the field <code>feedName</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getFeedName() {
        return feedName;
    }

    /**
     * <p>Setter for the field <code>feedName</code>.</p>
     *
     * @param feedName a {@link java.lang.String} object
     */
    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }
}
