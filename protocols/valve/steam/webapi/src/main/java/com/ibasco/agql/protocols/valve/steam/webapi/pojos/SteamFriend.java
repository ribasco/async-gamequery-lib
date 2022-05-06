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
public class SteamFriend {

    @SerializedName("steamid")
    private long steamId;

    private String relationship;

    @SerializedName("friend_since")
    private long friendSince;

    /**
     * <p>Getter for the field <code>steamId</code>.</p>
     *
     * @return a long
     */
    public long getSteamId() {
        return steamId;
    }

    /**
     * <p>Setter for the field <code>steamId</code>.</p>
     *
     * @param steamId
     *         a long
     */
    public void setSteamId(long steamId) {
        this.steamId = steamId;
    }

    /**
     * <p>Getter for the field <code>relationship</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getRelationship() {
        return relationship;
    }

    /**
     * <p>Setter for the field <code>relationship</code>.</p>
     *
     * @param relationship
     *         a {@link java.lang.String} object
     */
    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    /**
     * <p>Getter for the field <code>friendSince</code>.</p>
     *
     * @return a long
     */
    public long getFriendSince() {
        return friendSince;
    }

    /**
     * <p>Setter for the field <code>friendSince</code>.</p>
     *
     * @param friendSince
     *         a long
     */
    public void setFriendSince(long friendSince) {
        this.friendSince = friendSince;
    }
}
