/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Ibasco
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
 **************************************************************************************************/

package com.ribasco.gamecrawler.protocols.valve.server;

import com.ribasco.gamecrawler.protocols.GameRequestPacket;
import com.ribasco.gamecrawler.protocols.Response;
import com.ribasco.gamecrawler.protocols.valve.server.enums.SourceRequest;
import com.ribasco.gamecrawler.protocols.valve.server.enums.SourceResponse;
import com.ribasco.gamecrawler.protocols.valve.server.packets.request.*;
import com.ribasco.gamecrawler.protocols.valve.server.packets.response.*;
import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.reflect.ConstructorUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by raffy on 9/5/2016.
 */
//REVIEW: Further review of this class get needed
public class SourceMapper {
    public static final Map<SourceResponse, Class<? extends SourceResponsePacket>> HEADER_RESPONSE_MAP;
    public static final Map<SourceRequest, Class<? extends SourceRequestPacket>> HEADER_REQUEST_MAP;
    public static final Map<Class<? extends SourceRequestPacket>, Class<? extends SourceResponsePacket>> REQUEST_RESPONSE_MAP;

    //Configure our mappings
    static {
        final Map<SourceResponse, Class<? extends SourceResponsePacket>> R_HEADER_RESPONSE_MAP = new HashMap<>();
        final Map<SourceRequest, Class<? extends SourceRequestPacket>> R_HEADER_REQUEST_MAP = new HashMap<>();
        final Map<Class<? extends SourceRequestPacket>, Class<? extends SourceResponsePacket>> R_REQUEST_RESPONSE_MAP = new HashMap<>();

        //Configure HEADER <-> REQUEST Mappings
        R_HEADER_REQUEST_MAP.put(SourceRequest.MASTER, SourceMasterRequestPacket.class);
        R_HEADER_REQUEST_MAP.put(SourceRequest.CHALLENGE, SourceChallengeRequestPacket.class);
        R_HEADER_REQUEST_MAP.put(SourceRequest.INFO, SourceInfoRequestPacket.class);
        R_HEADER_REQUEST_MAP.put(SourceRequest.PLAYER, SourcePlayerRequestPacket.class);
        R_HEADER_REQUEST_MAP.put(SourceRequest.RULES, SourceRulesRequestPacket.class);

        //Configure HEADER <-> RESPONSE Mappings
        R_HEADER_RESPONSE_MAP.put(SourceResponse.MASTER, SourceMasterResponsePacket.class);
        R_HEADER_RESPONSE_MAP.put(SourceResponse.CHALLENGE, SourceChallengeResponsePacket.class);
        R_HEADER_RESPONSE_MAP.put(SourceResponse.INFO, SourceInfoResponsePacket.class);
        R_HEADER_RESPONSE_MAP.put(SourceResponse.PLAYER, SourcePlayerResponsePacket.class);
        R_HEADER_RESPONSE_MAP.put(SourceResponse.RULES, SourceRulesResponsePacket.class);

        //Configure REQUEST <-> RESPONSE Mappings
        R_REQUEST_RESPONSE_MAP.put(SourceChallengeRequestPacket.class, SourceChallengeResponsePacket.class);
        R_REQUEST_RESPONSE_MAP.put(SourceInfoRequestPacket.class, SourceInfoResponsePacket.class);
        R_REQUEST_RESPONSE_MAP.put(SourceMasterRequestPacket.class, SourceMasterResponsePacket.class);
        R_REQUEST_RESPONSE_MAP.put(SourcePlayerRequestPacket.class, SourcePlayerResponsePacket.class);
        R_REQUEST_RESPONSE_MAP.put(SourceRulesRequestPacket.class, SourceRulesResponsePacket.class);

        //Create immutable maps
        HEADER_REQUEST_MAP = Collections.unmodifiableMap(R_HEADER_REQUEST_MAP);
        HEADER_RESPONSE_MAP = Collections.unmodifiableMap(R_HEADER_RESPONSE_MAP);
        REQUEST_RESPONSE_MAP = Collections.unmodifiableMap(R_REQUEST_RESPONSE_MAP);
    }

    public static Class<? extends SourceRequestPacket> getRequestClass(Class<? extends Response> responseClass) {
        return REQUEST_RESPONSE_MAP.entrySet()
                .stream()
                .filter(map -> map.getValue().equals(responseClass))
                .map(Map.Entry::getKey)
                .findFirst().orElse(null);
    }

    public static Class<? extends SourceResponsePacket> getResponseClass(Class<? extends GameRequestPacket> requestClass) {
        if (requestClass == null)
            return null;
        return REQUEST_RESPONSE_MAP.get(requestClass);
    }

    public static boolean isValidResponsePacket(ByteBuf packet) {
        byte packetHeader = packet.getByte(packet.readerIndex());
        return HEADER_RESPONSE_MAP.containsKey(SourceResponse.get(packetHeader));
    }

    public static SourceResponsePacket getResponsePacket(ByteBuf packet) {
        //Verify that the packet get valid
        if (!isValidResponsePacket(packet)) {
            return null;
        }
        //Retrieve the response header
        byte packetHeader = packet.readByte();
        SourceResponsePacket handler;
        try {
            Class<? extends SourceResponsePacket> cResponsePacket = HEADER_RESPONSE_MAP.get(SourceResponse.get(packetHeader));
            if (cResponsePacket == null)
                throw new RuntimeException(String.format("No mapping found for header '%s'", Integer.toHexString(packetHeader)));
            handler = ConstructorUtils.invokeConstructor(cResponsePacket, packet);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException(e);
        }

        return handler;
    }
}
