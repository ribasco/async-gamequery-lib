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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>Dota2TeamMemberDetails class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class Dota2TeamMemberDetails {

    @SerializedName("account_id")
    @Expose
    private long accountId;
    @SerializedName("time_joined")
    @Expose
    private long timeJoined;
    @SerializedName("admin")
    @Expose
    private boolean admin;
    @SerializedName("sub")
    @Expose
    private boolean sub;

    /**
     * <p>Getter for the field <code>accountId</code>.</p>
     *
     * @return The accountId
     */
    public long getAccountId() {
        return accountId;
    }

    /**
     * <p>Setter for the field <code>accountId</code>.</p>
     *
     * @param accountId
     *         The account_id
     */
    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    /**
     * <p>Getter for the field <code>timeJoined</code>.</p>
     *
     * @return The timeJoined
     */
    public long getTimeJoined() {
        return timeJoined;
    }

    /**
     * <p>Setter for the field <code>timeJoined</code>.</p>
     *
     * @param timeJoined
     *         The time_joined
     */
    public void setTimeJoined(long timeJoined) {
        this.timeJoined = timeJoined;
    }

    /**
     * <p>isAdmin.</p>
     *
     * @return The admin
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * <p>Setter for the field <code>admin</code>.</p>
     *
     * @param admin
     *         The admin
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    /**
     * <p>isSub.</p>
     *
     * @return The sub
     */
    public boolean isSub() {
        return sub;
    }

    /**
     * <p>Setter for the field <code>sub</code>.</p>
     *
     * @param sub
     *         The sub
     */
    public void setSub(boolean sub) {
        this.sub = sub;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

}
