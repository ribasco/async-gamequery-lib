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

package com.ibasco.agql.protocols.valve.steam.webapi.interfaces.steamstore.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class SteamStoreAppResponse {

    @SerializedName("apps")
    @Expose
    private List<SteamStoreApp> apps = new ArrayList<>();

    @SerializedName("have_more_results")
    @Expose
    private Boolean haveMoreResults;

    @SerializedName("last_appid")
    @Expose
    private Integer lastAppid;

    public List<SteamStoreApp> getApps() {
        return apps;
    }

    public void setApps(List<SteamStoreApp> apps) {
        this.apps = apps;
    }

    public Boolean getHaveMoreResults() {
        return haveMoreResults;
    }

    public void setHaveMoreResults(Boolean haveMoreResults) {
        this.haveMoreResults = haveMoreResults;
    }

    public Integer getLastAppid() {
        return lastAppid;
    }

    public void setLastAppid(Integer lastAppid) {
        this.lastAppid = lastAppid;
    }
}
