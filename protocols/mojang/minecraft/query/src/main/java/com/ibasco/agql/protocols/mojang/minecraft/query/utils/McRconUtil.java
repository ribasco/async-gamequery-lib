/*
 *   MIT License
 *
 *   Copyright (c) 2016 Asynchronous Game Query Library
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in all
 *   copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   SOFTWARE.
 */

package com.ibasco.agql.protocols.mojang.minecraft.query.utils;

import com.ibasco.agql.protocols.mojang.minecraft.query.McRconResponsePacket;
import org.apache.commons.lang3.RandomUtils;

/**
 * Source RCON Utility functions
 */
public class McRconUtil {
    /**
     * A reserved request id representing a special rcon terminator packet
     */
    public static final int RCON_TERMINATOR_RID = 8445800;

    /**
     * The minimum allowable value for an rcon request id
     */
    private static final int RCON_ID_MIN_RANGE = 100000000;

    /**
     * The maximum allowable value for an rcon request id
     */
    private static final int RCON_ID_MAX_RANGE = 999999999;

    /**
     * <p>Checks if the response packet is a valid rcon terminator packet</p>
     *
     * @param packet
     *         The {@link McRconResponsePacket} to check
     *
     * @return <code>true</code> if its a terminator packet
     */
    public static boolean isTerminator(McRconResponsePacket packet) {
        return isTerminator(packet.getId());
    }

    /**
     * <p>Checks if the rcon request id represents a terminator packet</p>
     *
     * @param requestId
     *         An integer representing an Rcon Request Id
     *
     * @return <code>true</code> if the given id represents a terminator
     */
    public static boolean isTerminator(int requestId) {
        return requestId == RCON_TERMINATOR_RID;
    }

    /**
     * <p>Checks if the request id is within the valid range</p>
     *
     * @param requestId
     *         An integer representing a request id
     *
     * @return <code>true</code> if the given request id is within the valid range
     */
    public static boolean isValidRequestId(int requestId) {
        return requestId >= RCON_ID_MIN_RANGE && requestId <= RCON_ID_MAX_RANGE;
    }

    /**
     * A utility method to generate random request ids
     *
     * @return An random integer ranging from 100000000 to 999999999
     */
    public static int createRequestId() {
        return RandomUtils.nextInt(RCON_ID_MIN_RANGE, RCON_ID_MAX_RANGE);
    }
}
