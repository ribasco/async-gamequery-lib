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
public class SteamGameAchievement {
    @SerializedName("name")
    private String name;
    @SerializedName("percent")
    private double percentage;

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name a {@link java.lang.String} object
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>Getter for the field <code>percentage</code>.</p>
     *
     * @return a double
     */
    public double getPercentage() {
        return percentage;
    }

    /**
     * <p>Setter for the field <code>percentage</code>.</p>
     *
     * @param percentage a double
     */
    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}
