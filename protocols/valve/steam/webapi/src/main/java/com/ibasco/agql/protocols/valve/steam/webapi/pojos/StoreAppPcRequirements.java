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

/**
 * <p>StoreAppPcRequirements class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class StoreAppPcRequirements {

    private String minimum;

    private String recommended;

    /**
     * <p>Getter for the field <code>minimum</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getMinimum() {
        return minimum;
    }

    /**
     * <p>Setter for the field <code>minimum</code>.</p>
     *
     * @param minimum
     *         a {@link java.lang.String} object
     */
    public void setMinimum(String minimum) {
        this.minimum = minimum;
    }

    /**
     * <p>Getter for the field <code>recommended</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getRecommended() {
        return recommended;
    }

    /**
     * <p>Setter for the field <code>recommended</code>.</p>
     *
     * @param recommended
     *         a {@link java.lang.String} object
     */
    public void setRecommended(String recommended) {
        this.recommended = recommended;
    }
}
