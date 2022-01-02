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

package com.ibasco.agql.protocols.valve.source.query.protocols.challenge;

import com.ibasco.agql.protocols.valve.source.query.SourceQuery;
import com.ibasco.agql.protocols.valve.source.query.handlers.SourceQueryDecoder;
import com.ibasco.agql.protocols.valve.source.query.message.SourceQueryRequest;
import com.ibasco.agql.protocols.valve.source.query.packets.SourceQuerySinglePacket;
import io.netty.channel.ChannelHandlerContext;

public class SourceQueryChallengeDecoder extends SourceQueryDecoder<SourceQueryRequest> {

    @Override
    protected boolean acceptPacket(SourceQueryMessage msg) {
        return msg.hasRequest(SourceQueryChallengeRequest.class) && msg.hasHeader(SourceQuery.SOURCE_QUERY_CHALLENGE_RES);
    }

    @Override
    protected Object decodePacket(ChannelHandlerContext ctx, SourceQueryRequest request, SourceQuerySinglePacket packet) {
        return new SourceQueryChallengeResponse(packet.content().readIntLE(), null);
    }
}
