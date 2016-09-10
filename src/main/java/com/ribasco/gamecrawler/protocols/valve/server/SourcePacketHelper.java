/*
 * MIT License
 *
 * Copyright (c) [year] [fullname]
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ribasco.gamecrawler.protocols.valve.server;

import com.ribasco.gamecrawler.protocols.GameRequestPacket;
import com.ribasco.gamecrawler.protocols.Response;
import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Created by raffy on 9/5/2016.
 */
public class SourcePacketHelper {
    private static final Logger log = LoggerFactory.getLogger(SourcePacketHelper.class);

    public static Class<? extends SourceRequestPacket> getRequestClass(Class<? extends Response> responseClass) {
        Class<? extends SourceRequestPacket> requestClass = SourceConstants.REQUEST_RESPONSE_MAP.entrySet()
                .stream()
                .filter(map -> map.getValue().equals(responseClass))
                .map(Map.Entry::getKey)
                .findFirst().orElse(null);
        return requestClass;
    }

    public static Class<? extends SourceResponsePacket> getResponseClass(Class<? extends GameRequestPacket> requestClass) {
        if (requestClass == null)
            return null;
        return SourceConstants.REQUEST_RESPONSE_MAP.get(requestClass);
    }

    public static boolean isValidResponsePacket(ByteBuf packet) {
        byte packetHeader = packet.getByte(packet.readerIndex());
        return SourceConstants.HEADER_RESPONSE_MAP.containsKey(packetHeader);
    }

    public static SourceResponsePacket getResponsePacket(ByteBuf packet) {
        //Verify that the packet is valid
        if (!isValidResponsePacket(packet)) {
            return null;
        }

        //Retrieve the response header
        byte packetHeader = packet.readByte();
        SourceResponsePacket handler = null;
        try {
            Class<? extends SourceResponsePacket> cResponsePacket = SourceConstants.HEADER_RESPONSE_MAP.get(packetHeader);
            if (cResponsePacket == null)
                throw new RuntimeException("Response Packet Class Missing");
            handler = ConstructorUtils.invokeConstructor(cResponsePacket, packet);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        return handler;
    }

}
