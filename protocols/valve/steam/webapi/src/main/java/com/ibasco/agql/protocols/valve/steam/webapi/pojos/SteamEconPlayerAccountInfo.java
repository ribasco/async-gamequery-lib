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
 * <p>SteamEconPlayerAccountInfo class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SteamEconPlayerAccountInfo {

    @SerializedName("steamid")
    private long steamId;

    @SerializedName("personaname")
    private String personaName;

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("steamId", getSteamId())
                .append("personaName", getPersonaName())
                .toString();
    }

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
     * <p>Getter for the field <code>personaName</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getPersonaName() {
        return personaName;
    }

    /**
     * <p>Setter for the field <code>personaName</code>.</p>
     *
     * @param personaName
     *         a {@link java.lang.String} object
     */
    public void setPersonaName(String personaName) {
        this.personaName = personaName;
    }
}
