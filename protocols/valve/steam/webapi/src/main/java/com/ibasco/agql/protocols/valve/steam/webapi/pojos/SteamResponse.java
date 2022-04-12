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
public abstract class SteamResponse {
    @SerializedName("success")
    private boolean success;

    /**
     * <p>isSuccess.</p>
     *
     * @return a boolean
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * <p>Setter for the field <code>success</code>.</p>
     *
     * @param success a boolean
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }
}
