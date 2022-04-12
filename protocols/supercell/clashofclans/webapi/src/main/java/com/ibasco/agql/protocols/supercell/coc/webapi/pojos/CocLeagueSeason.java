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

package com.ibasco.agql.protocols.supercell.coc.webapi.pojos;

import org.jetbrains.annotations.ApiStatus;

/**
 * <p>CocLeagueSeason class.</p>
 *
 * @author Rafael Luis Ibasco
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
public class CocLeagueSeason {

    private String seasonDate;

    /**
     * <p>Getter for the field <code>seasonDate</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getSeasonDate() {
        return seasonDate;
    }

    /**
     * <p>Setter for the field <code>seasonDate</code>.</p>
     *
     * @param seasonDate a {@link java.lang.String} object
     */
    public void setSeasonDate(String seasonDate) {
        this.seasonDate = seasonDate;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return seasonDate;
    }
}
