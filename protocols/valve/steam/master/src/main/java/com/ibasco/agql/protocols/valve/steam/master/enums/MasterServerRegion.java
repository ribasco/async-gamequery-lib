/*
 * MIT License
 *
 * Copyright (c) 2018 Asynchronous Game Query Library
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ibasco.agql.protocols.valve.steam.master.enums;

/**
 * Created by raffy on 9/11/2016.
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

    public byte getHeader() {
        return header;
    }
}
