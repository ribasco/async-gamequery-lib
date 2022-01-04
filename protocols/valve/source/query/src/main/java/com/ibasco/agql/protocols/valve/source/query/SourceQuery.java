/*
 * Copyright 2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.protocols.valve.source.query;

import com.ibasco.agql.core.util.Option;
import com.ibasco.agql.protocols.valve.source.query.enums.SourceChallengeType;
import com.ibasco.agql.protocols.valve.source.query.message.SourceQueryRequest;
import com.ibasco.agql.protocols.valve.source.query.protocols.challenge.SourceQueryChallengeRequest;
import com.ibasco.agql.protocols.valve.source.query.protocols.info.SourceQueryInfoRequest;
import com.ibasco.agql.protocols.valve.source.query.protocols.players.SourceQueryPlayerRequest;
import com.ibasco.agql.protocols.valve.source.query.protocols.rules.SourceQueryRulesRequest;
import org.jetbrains.annotations.ApiStatus;

/**
 * Utility class for Source Query module
 *
 * @author Rafael Luis Ibasco
 */
@ApiStatus.Internal
public final class SourceQuery {

    public static final Option<Boolean> TEST_QUERY_OPTION = Option.createOption("testQueryOptin", true, true, true);

    public static final int SOURCE_PACKET_TYPE_SINGLE = 0xFFFFFFFF;

    public static final int SOURCE_PACKET_TYPE_SPLIT = 0xFFFFFFFE;

    public static final int SOURCE_QUERY_INFO_REQ = 0x54;

    public static final int SOURCE_QUERY_INFO_RES = 0x49;

    public static final int SOURCE_QUERY_PLAYER_REQ = 0x55;

    public static final int SOURCE_QUERY_PLAYER_RES = 0x44;

    public static final int SOURCE_QUERY_RULES_REQ = 0x56;

    public static final int SOURCE_QUERY_RULES_RES = 0x45;

    public static final int SOURCE_QUERY_CHALLENGE_REQ = 0x57;

    public static final int SOURCE_QUERY_CHALLENGE_RES = 0x41;

    public static final String SOURCE_QUERY_INFO_PAYLOAD = "Source Engine Query\0";

    public static final int A2S_INFO_EDF_PORT = 0x80;

    public static final int A2S_INFO_EDF_STEAMID = 0x10;

    public static final int A2S_INFO_EDF_SOURCETV = 0x40;

    public static final int A2S_INFO_EDF_TAGS = 0x20;

    public static final int A2S_INFO_EDF_GAMEID = 0x01;

    public static boolean isValidPacketType(int type) {
        return type == SOURCE_PACKET_TYPE_SINGLE || type == SOURCE_PACKET_TYPE_SPLIT;
    }

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

    public static boolean isInvalidHeader(int header) {
        return !isValidRequest(header) && !isValidResponse(header);
    }

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
