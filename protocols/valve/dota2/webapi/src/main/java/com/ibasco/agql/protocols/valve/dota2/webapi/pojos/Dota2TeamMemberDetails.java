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

package com.ibasco.agql.protocols.valve.dota2.webapi.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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
     * @return The accountId
     */
    public long getAccountId() {
        return accountId;
    }

    /**
     * @param accountId
     *         The account_id
     */
    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    /**
     * @return The timeJoined
     */
    public long getTimeJoined() {
        return timeJoined;
    }

    /**
     * @param timeJoined
     *         The time_joined
     */
    public void setTimeJoined(long timeJoined) {
        this.timeJoined = timeJoined;
    }

    /**
     * @return The admin
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * @param admin
     *         The admin
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    /**
     * @return The sub
     */
    public boolean isSub() {
        return sub;
    }

    /**
     * @param sub
     *         The sub
     */
    public void setSub(boolean sub) {
        this.sub = sub;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

}
