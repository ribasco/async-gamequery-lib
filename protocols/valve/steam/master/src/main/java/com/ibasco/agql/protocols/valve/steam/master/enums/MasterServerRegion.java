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

package com.ibasco.agql.protocols.valve.steam.master.enums;

/**
 * Created by raffy on 9/11/2016.
 *
 * @author Rafael Luis Ibasco
 */
public enum MasterServerRegion {

    /**
     * US East Coast Region Code
     */
    REGION_US_EAST_COAST(0x00),
    /**
     * US West Coast Region Code
     */
    REGION_US_WEST_COAST(0x01),
    /**
     * South America Region Code
     */
    REGION_SOUTH_AMERICA(0x02),
    /**
     * Europe Region Code
     */
    REGION_EUROPE(0x03),
    /**
     * Asia Region Code
     */
    REGION_ASIA(0x04),
    /**
     * Australia Region Code
     */
    REGION_AUSTRALIA(0x05),
    /**
     * Middle East Region Code
     */
    REGION_MIDDLE_EAST(0x06),
    /**
     * Africa Region Code
     */
    REGION_AFRICA(0x07),
    /**
     * Code to display ALL Regions
     */
    REGION_ALL(0xFF);

    private final byte header;

    MasterServerRegion(int header) {
        this.header = (byte) header;
    }

    /**
     * <p>Getter for the field <code>header</code>.</p>
     *
     * @return a byte
     */
    public byte getHeader() {
        return header;
    }
}
