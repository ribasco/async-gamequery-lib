/*
 * Copyright 2022-2022 Asynchronous Game Query Library
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

package com.ibasco.agql.protocols.valve.source.query.protocols.rules;

import com.ibasco.agql.core.util.NettyUtil;
import com.ibasco.agql.protocols.valve.source.query.SourceQuery;
import com.ibasco.agql.protocols.valve.source.query.handlers.SourceQueryAuthDecoder;
import com.ibasco.agql.protocols.valve.source.query.packets.SourceQuerySinglePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

public class SourceQueryRulesDecoder extends SourceQueryAuthDecoder<SourceQueryRulesRequest> {

    public SourceQueryRulesDecoder() {
        super(SourceQueryRulesRequest.class, SourceQuery.SOURCE_QUERY_RULES_RES);
    }

    @Override
    protected Object decodeQueryPacket(ChannelHandlerContext ctx, SourceQueryRulesRequest request, SourceQuerySinglePacket msg) {
        ByteBuf payload = msg.content();
        int noOfRules = payload.readShortLE();

        Map<String, String> rules = new HashMap<>();
        for (int i = 0; i < noOfRules; i++) {
            String name = NettyUtil.readString(payload);
            String value = NettyUtil.readString(payload);
            rules.put(name, value);
        }
        assert noOfRules == rules.size();

        debug("Successfully decoded a total of {} source rules", rules.size());
        return new SourceQueryRulesResponse(rules);
    }
}
