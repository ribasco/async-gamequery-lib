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

package com.ibasco.agql.protocols.valve.source.query;

import com.ibasco.agql.protocols.valve.source.query.challenge.SourceQueryChallengeRequest;
import com.ibasco.agql.protocols.valve.source.query.common.enums.SourceChallengeType;
import com.ibasco.agql.protocols.valve.source.query.common.message.SourceQueryRequest;
import com.ibasco.agql.protocols.valve.source.query.info.SourceQueryInfoRequest;
import com.ibasco.agql.protocols.valve.source.query.players.SourceQueryPlayerRequest;
import com.ibasco.agql.protocols.valve.source.query.rules.SourceQueryRulesRequest;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * Utility class for Source Query module
 *
 * @author Rafael Luis Ibasco
 */
@ApiStatus.Internal
public final class SourceQuery {

    /** Constant <code>INFO_QUERY</code> */
    public static final Marker INFO_QUERY = MarkerFactory.getMarker("INFO_QUERY");

    /** Constant <code>SOURCE_PACKET_TYPE_SINGLE=0xFFFFFFFF</code> */
    public static final int SOURCE_PACKET_TYPE_SINGLE = 0xFFFFFFFF;

    /** Constant <code>SOURCE_PACKET_TYPE_SPLIT=0xFFFFFFFE</code> */
    public static final int SOURCE_PACKET_TYPE_SPLIT = 0xFFFFFFFE;

    /** Constant <code>SOURCE_QUERY_INFO_REQ=0x54</code> */
    public static final int SOURCE_QUERY_INFO_REQ = 0x54;

    /** Constant <code>SOURCE_QUERY_INFO_RES=0x49</code> */
    public static final int SOURCE_QUERY_INFO_RES = 0x49;

    /** Constant <code>SOURCE_QUERY_PLAYER_REQ=0x55</code> */
    public static final int SOURCE_QUERY_PLAYER_REQ = 0x55;

    /** Constant <code>SOURCE_QUERY_PLAYER_RES=0x44</code> */
    public static final int SOURCE_QUERY_PLAYER_RES = 0x44;

    /** Constant <code>SOURCE_QUERY_RULES_REQ=0x56</code> */
    public static final int SOURCE_QUERY_RULES_REQ = 0x56;

    /** Constant <code>SOURCE_QUERY_RULES_RES=0x45</code> */
    public static final int SOURCE_QUERY_RULES_RES = 0x45;

    /** Constant <code>SOURCE_QUERY_CHALLENGE_REQ=0x57</code> */
    public static final int SOURCE_QUERY_CHALLENGE_REQ = 0x57;

    /** Constant <code>SOURCE_QUERY_CHALLENGE_RES=0x41</code> */
    public static final int SOURCE_QUERY_CHALLENGE_RES = 0x41;

    /** Constant <code>SOURCE_QUERY_INFO_PAYLOAD="Source Engine Query\0"</code> */
    public static final String SOURCE_QUERY_INFO_PAYLOAD = "Source Engine Query\0";

    /** Constant <code>A2S_INFO_EDF_PORT=0x80</code> */
    public static final int A2S_INFO_EDF_PORT = 0x80;

    /** Constant <code>A2S_INFO_EDF_STEAMID=0x10</code> */
    public static final int A2S_INFO_EDF_STEAMID = 0x10;

    /** Constant <code>A2S_INFO_EDF_SOURCETV=0x40</code> */
    public static final int A2S_INFO_EDF_SOURCETV = 0x40;

    /** Constant <code>A2S_INFO_EDF_TAGS=0x20</code> */
    public static final int A2S_INFO_EDF_TAGS = 0x20;

    /** Constant <code>A2S_INFO_EDF_GAMEID=0x01</code> */
    public static final int A2S_INFO_EDF_GAMEID = 0x01;

    /**
     * <p>isValidPacketType.</p>
     *
     * @param type a int
     * @return a boolean
     */
    public static boolean isValidPacketType(int type) {
        return type == SOURCE_PACKET_TYPE_SINGLE || type == SOURCE_PACKET_TYPE_SPLIT;
    }

    /**
     * <p>isValidResponse.</p>
     *
     * @param header a int
     * @return a boolean
     */
    public static boolean isValidResponse(int header) {
        switch (header) {
            case SOURCE_QUERY_INFO_RES:
            case SOURCE_QUERY_PLAYER_RES:
            case SOURCE_QUERY_CHALLENGE_RES:
            case SOURCE_QUERY_RULES_RES:
                return true;
        }
        return false;
    }

    /**
     * <p>isValidRequest.</p>
     *
     * @param header a int
     * @return a boolean
     */
    public static boolean isValidRequest(int header) {
        switch (header) {
            case SOURCE_QUERY_INFO_REQ:
            case SOURCE_QUERY_PLAYER_REQ:
            case SOURCE_QUERY_CHALLENGE_REQ:
            case SOURCE_QUERY_RULES_REQ:
                return true;
        }
        return false;
    }

    /**
     * <p>isInvalidHeader.</p>
     *
     * @param header a int
     * @return a boolean
     */
    public static boolean isInvalidHeader(int header) {
        return !isValidRequest(header) && !isValidResponse(header);
    }

    /**
     * <p>getChallengeType.</p>
     *
     * @param requestClass a {@link java.lang.Class} object
     * @return a {@link com.ibasco.agql.protocols.valve.source.query.common.enums.SourceChallengeType} object
     */
    public static SourceChallengeType getChallengeType(Class<? extends SourceQueryRequest> requestClass) {
        SourceChallengeType type;
        if (SourceQueryInfoRequest.class.equals(requestClass)) {
            type = SourceChallengeType.INFO;
        } else if (SourceQueryPlayerRequest.class.equals(requestClass)) {
            type = SourceChallengeType.PLAYER;
        } else if (SourceQueryRulesRequest.class.equals(requestClass)) {
            type = SourceChallengeType.RULES;
        } else if (SourceQueryChallengeRequest.class.equals(requestClass)) {
            type = SourceChallengeType.CHALLENGE;
        } else {
            type = null;
        }
        return type;
    }
}
