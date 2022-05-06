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

package com.ibasco.agql.protocols.valve.dota2.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>Dota2BroadcasterInfo class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class Dota2BroadcasterInfo {

    @SerializedName("account_id")
    private long accountId;

    //TODO: Use a type adapter to convert this to a long type
    @SerializedName("server_steam_id")
    private String serverSteamId;

    private boolean live;

    /**
     * <p>Getter for the field <code>accountId</code>.</p>
     *
     * @return a long
     */
    public long getAccountId() {
        return accountId;
    }

    /**
     * <p>Setter for the field <code>accountId</code>.</p>
     *
     * @param accountId
     *         a long
     */
    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    /**
     * <p>Getter for the field <code>serverSteamId</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getServerSteamId() {
        return serverSteamId;
    }

    /**
     * <p>Setter for the field <code>serverSteamId</code>.</p>
     *
     * @param serverSteamId
     *         a {@link java.lang.String} object
     */
    public void setServerSteamId(String serverSteamId) {
        this.serverSteamId = serverSteamId;
    }

    /**
     * <p>isLive.</p>
     *
     * @return a boolean
     */
    public boolean isLive() {
        return live;
    }

    /**
     * <p>Setter for the field <code>live</code>.</p>
     *
     * @param live
     *         a boolean
     */
    public void setLive(boolean live) {
        this.live = live;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
