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

package com.ibasco.agql.protocols.valve.source.query.protocols.rules;

import com.ibasco.agql.core.util.Netty;
import com.ibasco.agql.core.util.Pair;
import com.ibasco.agql.core.util.Strings;
import com.ibasco.agql.protocols.valve.source.query.SourceQuery;
import com.ibasco.agql.protocols.valve.source.query.handlers.SourceQueryAuthDecoder;
import com.ibasco.agql.protocols.valve.source.query.packets.SourceQuerySinglePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"SameParameterValue", "DuplicatedCode"})
public class SourceQueryRulesDecoder extends SourceQueryAuthDecoder<SourceQueryRulesRequest> {

    public SourceQueryRulesDecoder() {
        super(SourceQueryRulesRequest.class, SourceQuery.SOURCE_QUERY_RULES_RES);
    }

    @Override
    protected Object decodeQueryPacket(ChannelHandlerContext ctx, SourceQueryRulesRequest request, SourceQuerySinglePacket msg) throws Exception {
        ByteBuf payload = msg.content();
        Map<String, String> rules = new HashMap<>();
        int expectedCount = -1;
        //some servers send an empty info response packet, so we also return an empty response
        if (payload.isReadable()) {
            if (isDebugEnabled())
                debug("RULES Dump\n{}", Netty.prettyHexDump(payload));
            expectedCount = payload.readShortLE();
            for (int i = 0; i < expectedCount; i++) {
                //make sure we have more data to read
                if (!payload.isReadable())
                    break;
                Pair<String, String> rule = new Pair<>();
                decodeField("ruleName", payload, Netty::readString, rule::setFirst, null);
                decodeField("ruleValue", payload, Netty::readString, rule::setSecond, null);
                if (!Strings.isBlank(rule.getFirst()) && !Strings.isBlank(rule.getSecond()))
                    rules.put(rule.getFirst(), rule.getSecond());
            }
            if (expectedCount >= 0 && expectedCount != rules.size())
                debug("Did not get expected number of rules from the server (Expected: {}, Actual: {})", expectedCount, rules.size());
            debug("Successfully decoded a total of '{}' source rules (expected size: {})", rules.size(), expectedCount);
        } else {
            debug("Received an empty RULES response");
        }
        return new SourceQueryRulesResponse(rules, expectedCount);
    }
}
