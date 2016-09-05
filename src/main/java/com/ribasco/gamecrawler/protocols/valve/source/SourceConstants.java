package com.ribasco.gamecrawler.protocols.valve.source;

import com.ribasco.gamecrawler.protocols.valve.source.requests.*;
import com.ribasco.gamecrawler.protocols.valve.source.response.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by raffy on 9/5/2016.
 */
public class SourceConstants {
    public static final Map<Byte, Class<? extends SourceResponsePacket>> HEADER_RESPONSE_MAP;
    public static final Map<Byte, Class<? extends SourceRequest>> HEADER_REQUEST_MAP;
    public static final Map<Class<? extends SourceRequest>, Class<? extends SourceResponsePacket>> REQUEST_RESPONSE_MAP;

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
        Map<Byte, Class<? extends SourceRequest>> R_HEADER_REQUEST_MAP = new HashMap<>();
        Map<Class<? extends SourceRequest>, Class<? extends SourceResponsePacket>> R_REQUEST_RESPONSE_MAP = new HashMap<>();

        //Configure HEADER <-> REQUEST Mappings
        R_HEADER_REQUEST_MAP.put(REQUEST_MASTER_HEADER, SourceMasterRequest.class);
        R_HEADER_REQUEST_MAP.put(REQUEST_CHALLENGE_HEADER, SourceChallengeRequest.class);
        R_HEADER_REQUEST_MAP.put(REQUEST_INFO_HEADER, SourceInfoRequest.class);
        R_HEADER_REQUEST_MAP.put(REQUEST_PLAYER_HEADER, SourcePlayerRequest.class);
        R_HEADER_REQUEST_MAP.put(REQUEST_RULES_HEADER, SourceRulesRequest.class);

        //Configure HEADER <-> RESPONSE Mappings
        R_HEADER_RESPONSE_MAP.put(RESPONSE_MASTER_HEADER, SourceMasterResponsePacket.class);
        R_HEADER_RESPONSE_MAP.put(RESPONSE_CHALLENGE_HEADER, SourceChallengeResponsePacket.class);
        R_HEADER_RESPONSE_MAP.put(RESPONSE_INFO_HEADER, SourceInfoResponsePacket.class);
        R_HEADER_RESPONSE_MAP.put(RESPONSE_PLAYER_HEADER, SourcePlayerResponsePacket.class);
        R_HEADER_RESPONSE_MAP.put(RESPONSE_RULES_HEADER, SourceRulesResponsePacket.class);

        //Configure REQUEST <-> RESPONSE Mappings
        R_REQUEST_RESPONSE_MAP.put(SourceChallengeRequest.class, SourceChallengeResponsePacket.class);
        R_REQUEST_RESPONSE_MAP.put(SourceInfoRequest.class, SourceInfoResponsePacket.class);
        R_REQUEST_RESPONSE_MAP.put(SourceMasterRequest.class, SourceMasterResponsePacket.class);
        R_REQUEST_RESPONSE_MAP.put(SourcePlayerRequest.class, SourcePlayerResponsePacket.class);
        R_REQUEST_RESPONSE_MAP.put(SourceRulesRequest.class, SourceRulesResponsePacket.class);

        //Create immutable maps
        HEADER_REQUEST_MAP = Collections.unmodifiableMap(R_HEADER_REQUEST_MAP);
        HEADER_RESPONSE_MAP = Collections.unmodifiableMap(R_HEADER_RESPONSE_MAP);
        REQUEST_RESPONSE_MAP = Collections.unmodifiableMap(R_REQUEST_RESPONSE_MAP);
    }


}
