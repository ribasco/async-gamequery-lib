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

import com.ribasco.gamecrawler.protocols.valve.server.packets.requests.*;
import com.ribasco.gamecrawler.protocols.valve.server.packets.response.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by raffy on 9/5/2016.
 */
@Deprecated
public class SourceConstants {
    public static final Map<Byte, Class<? extends SourceResponsePacket>> HEADER_RESPONSE_MAP;
    public static final Map<Byte, Class<? extends SourceRequestPacket>> HEADER_REQUEST_MAP;
    public static final Map<Class<? extends SourceRequestPacket>, Class<? extends SourceResponsePacket>> REQUEST_RESPONSE_MAP;

    //TODO: Move all these to enum classes
    public static final byte RESPONSE_CHALLENGE_HEADER = 0x41;
    public static final byte RESPONSE_INFO_HEADER = 0x49;
    public static final byte RESPONSE_MASTER_HEADER = 0x66;
    public static final byte RESPONSE_MASTER_SECONDARY_HEADER = 0x0A;
    public static final byte RESPONSE_PLAYER_HEADER = 0x44;
    public static final byte RESPONSE_RULES_HEADER = 0x45;
    public static final byte REQUEST_CHALLENGE_HEADER = 0x57;
    public static final byte REQUEST_INFO_HEADER = 0x54;
    public static final byte REQUEST_MASTER_HEADER = 0x31;
    public static final byte REQUEST_PLAYER_HEADER = 0x55;
    public static final byte REQUEST_RULES_HEADER = 0x56;

    //Configure our mappings
    static {
        Map<Byte, Class<? extends SourceResponsePacket>> R_HEADER_RESPONSE_MAP = new HashMap<>();
        Map<Byte, Class<? extends SourceRequestPacket>> R_HEADER_REQUEST_MAP = new HashMap<>();
        Map<Class<? extends SourceRequestPacket>, Class<? extends SourceResponsePacket>> R_REQUEST_RESPONSE_MAP = new HashMap<>();

        //Configure HEADER <-> REQUEST Mappings
        R_HEADER_REQUEST_MAP.put(REQUEST_MASTER_HEADER, SourceMasterRequestPacket.class);
        R_HEADER_REQUEST_MAP.put(REQUEST_CHALLENGE_HEADER, SourceChallengeRequestPacket.class);
        R_HEADER_REQUEST_MAP.put(REQUEST_INFO_HEADER, SourceInfoRequestPacket.class);
        R_HEADER_REQUEST_MAP.put(REQUEST_PLAYER_HEADER, SourcePlayerRequestPacket.class);
        R_HEADER_REQUEST_MAP.put(REQUEST_RULES_HEADER, SourceRulesRequestPacket.class);

        //Configure HEADER <-> RESPONSE Mappings
        R_HEADER_RESPONSE_MAP.put(RESPONSE_MASTER_HEADER, SourceMasterResponsePacket.class);
        R_HEADER_RESPONSE_MAP.put(RESPONSE_CHALLENGE_HEADER, SourceChallengeResponsePacket.class);
        R_HEADER_RESPONSE_MAP.put(RESPONSE_INFO_HEADER, SourceInfoResponsePacket.class);
        R_HEADER_RESPONSE_MAP.put(RESPONSE_PLAYER_HEADER, SourcePlayerResponsePacket.class);
        R_HEADER_RESPONSE_MAP.put(RESPONSE_RULES_HEADER, SourceRulesResponsePacket.class);

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


}
