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
public class ServerUpdateStatus extends SteamResponse {
    @SerializedName("up_to_date")
    private boolean upToDate;

    @SerializedName("version_is_listable")
    private boolean versionListable;

    @SerializedName("required_version")
    private int requiredVersion;

    private String message;

    public boolean isUpToDate() {
        return upToDate;
    }

    public void setUpToDate(boolean upToDate) {
        this.upToDate = upToDate;
    }

    public boolean isVersionListable() {
        return versionListable;
    }

    public void setVersionListable(boolean versionListable) {
        this.versionListable = versionListable;
    }

    public int getRequiredVersion() {
        return requiredVersion;
    }

    public void setRequiredVersion(int requiredVersion) {
        this.requiredVersion = requiredVersion;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
