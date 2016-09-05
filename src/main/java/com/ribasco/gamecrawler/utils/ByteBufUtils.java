package com.ribasco.gamecrawler.utils;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

/**
 * Created by raffy on 9/6/2016.
 */
public class ByteBufUtils {
    public static String getString(ByteBuf buffer) {
        int length = buffer.bytesBefore((byte) 0);
        String data = buffer.readCharSequence(length + 1, CharsetUtil.ISO_8859_1).toString();
        return data;
    }
}
