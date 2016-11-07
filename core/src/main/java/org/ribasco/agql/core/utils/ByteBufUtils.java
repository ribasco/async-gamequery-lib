/*
 * MIT License
 *
 * Copyright (c) 2016 Asynchronous Game Query Library
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

package org.ribasco.agql.core.utils;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public class ByteBufUtils {

    private static final Logger log = LoggerFactory.getLogger(ByteBufUtils.class);

    public static String readString(ByteBuf buffer) {
        return readString(buffer, CharsetUtil.ISO_8859_1, false, null);
    }

    public static String readString(ByteBuf buffer, Charset encoding) {
        return readString(buffer, encoding, false);
    }

    public static String readString(ByteBuf buffer, Charset encoding, boolean readNonNullTerminated) {
        return readString(buffer, encoding, readNonNullTerminated, null);
    }

    public static String readString(ByteBuf buffer, Charset encoding, boolean readNonNullTerminated, String defaultString) {
        int length = buffer.bytesBefore((byte) 0);
        if (length < 0) {
            if (readNonNullTerminated && buffer.readableBytes() > 0)
                length = buffer.readableBytes();
            else
                return null;
        }
        String data = buffer.readCharSequence(length, encoding).toString();
        //Discard the null terminator (if available)
        if (buffer.readableBytes() > 2 && buffer.getByte(buffer.readerIndex()) == 0)
            buffer.readByte();
        return data;
    }

    public static String readStringToEnd(ByteBuf buffer, Charset encoding) {
        int length = buffer.readableBytes();
        if (length > 0)
            return buffer.readCharSequence(length, encoding).toString();
        return null;
    }
}
