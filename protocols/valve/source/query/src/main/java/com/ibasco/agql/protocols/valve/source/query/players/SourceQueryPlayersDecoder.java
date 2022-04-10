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

package com.ibasco.agql.protocols.valve.source.query.players;

import com.ibasco.agql.core.util.Netty;
import com.ibasco.agql.protocols.valve.source.query.SourceQuery;
import com.ibasco.agql.protocols.valve.source.query.common.handlers.SourceQueryAuthDecoder;
import com.ibasco.agql.protocols.valve.source.query.common.packets.SourceQuerySinglePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SourceQueryPlayersDecoder extends SourceQueryAuthDecoder<SourceQueryPlayerRequest> {

    private static final Logger log = LoggerFactory.getLogger(SourceQueryPlayersDecoder.class);

    public SourceQueryPlayersDecoder() {
        super(SourceQueryPlayerRequest.class, SourceQuery.SOURCE_QUERY_PLAYER_RES);
    }

    @Override
    protected Object decodeQueryPacket(ChannelHandlerContext ctx, SourceQueryPlayerRequest request, SourceQuerySinglePacket packet) throws Exception {
        ByteBuf payload = packet.content();
        ArrayList<SourcePlayer> playerList = new ArrayList<>();
        //some servers send an empty info response packet, so we also return an empty response
        if (payload.readableBytes() > 0) {
            if (isDebugEnabled())
                debug("PLAYERS Dump\n{}", Netty.prettyHexDump(payload));

            int playereCount = payload.readUnsignedByte();
            debug(log, "Decoding player payload data (Player count: {})", playereCount);
            for (int i = 0; i < playereCount; i++) {
                if (payload.readableBytes() < 10)
                    break;
                debug(log, "Decoding player #{}", i);
                short index = decodeField(i + ") Index", (short) -1, payload, ByteBuf::readUnsignedByte);
                String name = decodeField(i + ") Name", "N/A", payload, buf -> Netty.readString(buf, StandardCharsets.UTF_8));
                int score = decodeField(i + ") Score", -1, payload, ByteBuf::readIntLE);
                float duration = decodeField(i + ") Duration", -1f, payload, ByteBuf::readFloatLE);
                playerList.add(new SourcePlayer(index, name, score, duration));
            }
        } else {
            debug(log, "Received an empty PLAYERS response");
        }
        return new SourceQueryPlayerResponse(playerList);
    }
}
