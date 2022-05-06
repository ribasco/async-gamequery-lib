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
public class ServerUpdateStatus extends SteamResponse {

    @SerializedName("up_to_date")
    private boolean upToDate;

    @SerializedName("version_is_listable")
    private boolean versionListable;

    @SerializedName("required_version")
    private int requiredVersion;

    private String message;

    /**
     * <p>isUpToDate.</p>
     *
     * @return a boolean
     */
    public boolean isUpToDate() {
        return upToDate;
    }

    /**
     * <p>Setter for the field <code>upToDate</code>.</p>
     *
     * @param upToDate
     *         a boolean
     */
    public void setUpToDate(boolean upToDate) {
        this.upToDate = upToDate;
    }

    /**
     * <p>isVersionListable.</p>
     *
     * @return a boolean
     */
    public boolean isVersionListable() {
        return versionListable;
    }

    /**
     * <p>Setter for the field <code>versionListable</code>.</p>
     *
     * @param versionListable
     *         a boolean
     */
    public void setVersionListable(boolean versionListable) {
        this.versionListable = versionListable;
    }

    /**
     * <p>Getter for the field <code>requiredVersion</code>.</p>
     *
     * @return a int
     */
    public int getRequiredVersion() {
        return requiredVersion;
    }

    /**
     * <p>Setter for the field <code>requiredVersion</code>.</p>
     *
     * @param requiredVersion
     *         a int
     */
    public void setRequiredVersion(int requiredVersion) {
        this.requiredVersion = requiredVersion;
    }

    /**
     * <p>Getter for the field <code>message</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getMessage() {
        return message;
    }

    /**
     * <p>Setter for the field <code>message</code>.</p>
     *
     * @param message
     *         a {@link java.lang.String} object
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
