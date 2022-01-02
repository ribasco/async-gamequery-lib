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

package com.ibasco.agql.core.utils;

@Deprecated
public class ByteUtils {

    /**
     * <p>Convert an integer to a primitive byte array type in Big-Endian</p>
     *
     * @param integer
     *         The integer to be converted
     *
     * @return A byte array representation of the integer specified
     */
    public static byte[] toByteArrayBE(int integer) {
        return new byte[] {
                (byte) ((integer >> 24) & 0xFF),
                (byte) ((integer >> 16) & 0xFF),
                (byte) ((integer >> 8) & 0xFF),
                (byte) (integer & 0xFF)
        };
    }

    /**
     * <p>Convert an integer to a primitive byte array type in Little-Endian</p>
     *
     * @param integer
     *         The integer to be converted
     *
     * @return A byte array representation of the integer specified
     */
    public static byte[] toByteArrayLE(int integer) {
        return new byte[] {
                (byte) (integer & 0xFF),
                (byte) ((integer >> 8) & 0xFF),
                (byte) ((integer >> 16) & 0xFF),
                (byte) ((integer >> 24) & 0xFF)
        };
    }

    /**
     * <p>Convert an array of bytes to it's Hex-String representation</p>
     *
     * @param data
     *         The array of bytes to be converted
     *
     * @return A Hex {@link String} representation of the byte array
     */
    public static String toFormattedHex(byte[] data) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            res.append("0x");
            res.append(String.format("%02x", data[i]).toUpperCase());
            if (i < data.length - 1)
                res.append(" ");
        }
        return res.toString();
    }
}
