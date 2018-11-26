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

package com.ibasco.agql.core.utils;

public class ByteUtils {
    /**
     * <p>Convert an integer to a primitive byte array type</p>
     *
     * @param integer The integer to be converted
     *
     * @return A byte array representation of the integer specified
     */
    public static byte[] byteArrayFromInteger(int integer) {
        return new byte[]{
                (byte) (integer >> 24),
                (byte) (integer >> 16),
                (byte) (integer >> 8),
                (byte) integer
        };
    }

    /**
     * <p>Convert an array of bytes to it's Hex-String representation</p>
     *
     * @param bytes The array of bytes to be converted
     *
     * @return A Hex {@link String} representation of the byte array
     */
    public static String bytesToHex(byte[] bytes) {
        return javax.xml.bind.DatatypeConverter.printHexBinary(bytes);
    }
}
