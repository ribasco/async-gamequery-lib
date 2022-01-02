/*
 * Copyright (c) 2018-2022 Asynchronous Game Query Library
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

package com.ibasco.agql.core.utils;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

@Deprecated
public class ByteBufUtils {

    private static final Logger log = LoggerFactory.getLogger(ByteBufUtils.class);

    public static String readString(ByteBuf buffer) {
        return readString(buffer, CharsetUtil.UTF_8, false, null);
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
