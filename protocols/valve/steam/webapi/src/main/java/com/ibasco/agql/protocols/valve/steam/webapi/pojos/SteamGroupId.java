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
 * Created by raffy on 10/27/2016.
 *
 * @author Rafael Luis Ibasco
 */
public class SteamGroupId {

    @SerializedName("gid")
    private String groupId;

    /**
     * <p>getGroupIdAsLong.</p>
     *
     * @return a {@link java.lang.Long} object
     */
    public Long getGroupIdAsLong() {
        return Long.valueOf(groupId);
    }

    /**
     * <p>Getter for the field <code>groupId</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * <p>Setter for the field <code>groupId</code>.</p>
     *
     * @param groupId
     *         a {@link java.lang.String} object
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "SteamGroupId{" +
                "groupId='" + groupId + '\'' +
                '}';
    }
}
