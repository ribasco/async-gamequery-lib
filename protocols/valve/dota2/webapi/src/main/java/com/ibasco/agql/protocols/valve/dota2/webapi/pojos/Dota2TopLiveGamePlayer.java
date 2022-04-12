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
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>Dota2TopLiveGamePlayer class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class Dota2TopLiveGamePlayer {

    @SerializedName("account_id")
    private int accountId;
    @SerializedName("hero_id")
    private int heroId;

    /**
     * <p>Getter for the field <code>accountId</code>.</p>
     *
     * @return The accountId
     */
    public int getAccountId() {
        return accountId;
    }

    /**
     * <p>Setter for the field <code>accountId</code>.</p>
     *
     * @param accountId
     *         The account_id
     */
    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    /**
     * <p>Getter for the field <code>heroId</code>.</p>
     *
     * @return The heroId
     */
    public int getHeroId() {
        return heroId;
    }

    /**
     * <p>Setter for the field <code>heroId</code>.</p>
     *
     * @param heroId
     *         The hero_id
     */
    public void setHeroId(int heroId) {
        this.heroId = heroId;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

}
