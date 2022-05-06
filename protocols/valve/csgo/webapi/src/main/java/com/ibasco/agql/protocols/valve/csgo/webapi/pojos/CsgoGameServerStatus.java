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

package com.ibasco.agql.protocols.valve.csgo.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>CsgoGameServerStatus class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class CsgoGameServerStatus {

    @SerializedName("app")
    private CsgoAppStatus appStatus;

    @SerializedName("services")
    private CsgoAppServicesStatus appServicesStatus;

    @SerializedName("datacenters")
    private List<CsgoDatacenterStatus> datacenterStatus = new ArrayList<>();

    @SerializedName("matchmaking")
    private CsgoMatchmakingStatus matchmakingStatus;

    /**
     * <p>Getter for the field <code>appStatus</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.csgo.webapi.pojos.CsgoAppStatus} object
     */
    public CsgoAppStatus getAppStatus() {
        return appStatus;
    }

    /**
     * <p>Setter for the field <code>appStatus</code>.</p>
     *
     * @param appStatus
     *         a {@link com.ibasco.agql.protocols.valve.csgo.webapi.pojos.CsgoAppStatus} object
     */
    public void setAppStatus(CsgoAppStatus appStatus) {
        this.appStatus = appStatus;
    }

    /**
     * <p>Getter for the field <code>appServicesStatus</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.csgo.webapi.pojos.CsgoAppServicesStatus} object
     */
    public CsgoAppServicesStatus getAppServicesStatus() {
        return appServicesStatus;
    }

    /**
     * <p>Setter for the field <code>appServicesStatus</code>.</p>
     *
     * @param appServicesStatus
     *         a {@link com.ibasco.agql.protocols.valve.csgo.webapi.pojos.CsgoAppServicesStatus} object
     */
    public void setAppServicesStatus(CsgoAppServicesStatus appServicesStatus) {
        this.appServicesStatus = appServicesStatus;
    }

    /**
     * <p>Getter for the field <code>datacenterStatus</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<CsgoDatacenterStatus> getDatacenterStatus() {
        return datacenterStatus;
    }

    /**
     * <p>Setter for the field <code>datacenterStatus</code>.</p>
     *
     * @param datacenterStatus
     *         a {@link java.util.List} object
     */
    public void setDatacenterStatus(List<CsgoDatacenterStatus> datacenterStatus) {
        this.datacenterStatus = datacenterStatus;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
