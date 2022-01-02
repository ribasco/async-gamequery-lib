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

/**
 * Created by raffy on 10/26/2016.
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

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(boolean externalUrl) {
        this.externalUrl = externalUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getFeedLabel() {
        return feedLabel;
    }

    public void setFeedLabel(String feedLabel) {
        this.feedLabel = feedLabel;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getFeedName() {
        return feedName;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }
}
