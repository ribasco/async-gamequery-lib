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

import com.ibasco.agql.core.util.Functions;
import com.ibasco.agql.core.util.NettyUtil;
import com.ibasco.agql.core.util.Pair;
import com.ibasco.agql.core.util.Strings;
import com.ibasco.agql.protocols.valve.source.query.SourceQuery;
import com.ibasco.agql.protocols.valve.source.query.handlers.SourceQueryAuthDecoder;
import com.ibasco.agql.protocols.valve.source.query.packets.SourceQuerySinglePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings({"SameParameterValue", "DuplicatedCode"})
public class SourceQueryRulesDecoder extends SourceQueryAuthDecoder<SourceQueryRulesRequest> {

    public SourceQueryRulesDecoder() {
        super(SourceQueryRulesRequest.class, SourceQuery.SOURCE_QUERY_RULES_RES);
    }

    @Override
    protected Object decodeQueryPacket(ChannelHandlerContext ctx, SourceQueryRulesRequest request, SourceQuerySinglePacket msg) {
        ByteBuf payload = msg.content();
        Map<String, String> rules = new HashMap<>();
        //some servers send an empty info response packet, so we also return an empty response
        if (payload.isReadable()) {
            int noOfRules = payload.readShortLE();
            for (int i = 0; i < noOfRules; i++) {
                //make sure we have more data to read
                if (!payload.isReadable())
                    break;
                Pair<String, String> rule = new Pair<>();
                decodeField("ruleName", payload, NettyUtil::readString, rule::setFirst, null);
                decodeField("ruleValue", payload, NettyUtil::readString, rule::setSecond, null);
                if (!Strings.isBlank(rule.getFirst()) && !Strings.isBlank(rule.getSecond()))
                    rules.put(rule.getFirst(), rule.getSecond());
            }
            debug("Successfully decoded a total of {} source rules", rules.size());
        } else {
            debug("Received an empty RULES response");
        }
        return new SourceQueryRulesResponse(rules);
    }

    private <A, B> void decodeField(String name, ByteBuf buf, Function<ByteBuf, A> reader, Consumer<B> writer, Function<A, B> transformer) {
        if (!buf.isReadable()) {
            debug("Skipped decoding for field '{}'. Buffer no longer readable (Reader Index: {}, Readable Bytes: {})", name, buf.readerIndex(), buf.readableBytes());
            return;
        }
        int startPosition = buf.readerIndex();
        debug("Decoding field '{}' at index position '{}'", name, startPosition);
        if (transformer == null)
            transformer = Functions::cast;
        try {
            A fromValue = reader.apply(buf);
            B toValue = transformer.apply(fromValue);
            writer.accept(toValue);
            debug("Saved decoded field '{}'", name);
        } catch (Throwable e) {
            error("Failed to decode field '{}' at position '{}'", startPosition);
        }
    }
}
