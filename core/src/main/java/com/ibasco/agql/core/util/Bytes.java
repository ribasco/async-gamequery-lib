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

package com.ibasco.agql.core.util;

import java.nio.ByteOrder;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

/**
 * <p>Bytes class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class Bytes {

    /**
     * <p>getSizeDescriptionSI.</p>
     *
     * @param bytes
     *         a long
     *
     * @return a {@link java.lang.String} object
     */
    public static String getSizeDescriptionSI(long bytes) {
        if (-1000 < bytes && bytes < 1000) {
            return bytes + " B";
        }
        CharacterIterator ci = new StringCharacterIterator("kMGTPE");
        while (bytes <= -999_950 || bytes >= 999_950) {
            bytes /= 1000;
            ci.next();
        }
        return String.format("%.1f %cB", bytes / 1000.0, ci.current());
    }

    /**
     * <p>getSizeDescriptionBin.</p>
     *
     * @param bytes
     *         a long
     *
     * @return a {@link java.lang.String} object
     */
    public static String getSizeDescriptionBin(long bytes) {
        long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        if (absB < 1024) {
            return bytes + " B";
        }
        long value = absB;
        CharacterIterator ci = new StringCharacterIterator("KMGTPE");
        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }
        value *= Long.signum(bytes);
        return String.format("%.1f %cB", value / 1024.0, ci.current());
    }

    /**
     * <p>toInteger.</p>
     *
     * @param bytes
     *         an array of {@code byte} objects
     *
     * @return a int
     */
    public static int toInteger(byte[] bytes) {
        return ((bytes[0] & 0xFF) << 24) |
                ((bytes[1] & 0xFF) << 16) |
                ((bytes[2] & 0xFF) << 8) |
                ((bytes[3] & 0xFF));
    }

    /**
     * <p>toIntegerLE.</p>
     *
     * @param bytes
     *         an array of {@code byte} objects
     *
     * @return a int
     */
    public static int toIntegerLE(byte[] bytes) {
        return ((bytes[3] & 0xFF) << 24) |
                ((bytes[2] & 0xFF) << 16) |
                ((bytes[1] & 0xFF) << 8) |
                ((bytes[0] & 0xFF));
    }

    /**
     * <p>toHexString.</p>
     *
     * @param data
     *         a int
     *
     * @return a {@link java.lang.String} object
     */
    public static String toHexString(int data) {
        return toHexString(data, null);
    }

    /**
     * <p>toHexString.</p>
     *
     * @param data
     *         a int
     * @param order
     *         a {@link java.nio.ByteOrder} object
     *
     * @return a {@link java.lang.String} object
     */
    public static String toHexString(int data, ByteOrder order) {
        if (order == null)
            order = ByteOrder.nativeOrder();
        if (ByteOrder.BIG_ENDIAN.equals(order))
            return toHexString(toByteArray(data));
        else
            return toHexString(toByteArrayLE(data));
    }

    /**
     * <p>toHexString.</p>
     *
     * @param data
     *         a byte
     *
     * @return a {@link java.lang.String} object
     */
    public static String toHexString(byte... data) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            res.append("0x");
            res.append(String.format("%02x", data[i]).toUpperCase());
            if (i < data.length - 1)
                res.append(" ");
        }
        return res.toString();
    }

    /**
     * <p>toByteArray.</p>
     *
     * @param value
     *         a int
     *
     * @return an array of {@code byte} objects
     */
    public static byte[] toByteArray(int value) {
        return new byte[] {
                (byte) ((value >> 24) & 0xff),
                (byte) ((value >> 16) & 0xff),
                (byte) ((value >> 8) & 0xff),
                (byte) (value & 0xff)
        };
    }

    /**
     * <p>toByteArrayLE.</p>
     *
     * @param value
     *         a int
     *
     * @return an array of {@code byte} objects
     */
    public static byte[] toByteArrayLE(int value) {
        return new byte[] {
                (byte) (value & 0xff),
                (byte) ((value >> 8) & 0xff),
                (byte) ((value >> 16) & 0xff),
                (byte) ((value >> 24) & 0xff)
        };
    }
}
