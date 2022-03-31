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

package com.ibasco.agql.protocols.valve.source.query.protocols.players;

import com.ibasco.agql.core.util.NettyUtil;
import com.ibasco.agql.protocols.valve.source.query.SourceQuery;
import com.ibasco.agql.protocols.valve.source.query.handlers.SourceQueryAuthDecoder;
import com.ibasco.agql.protocols.valve.source.query.packets.SourceQuerySinglePacket;
import com.ibasco.agql.protocols.valve.source.query.pojos.SourcePlayer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SourceQueryPlayersDecoder extends SourceQueryAuthDecoder<SourceQueryPlayerRequest> {

    public SourceQueryPlayersDecoder() {
        super(SourceQueryPlayerRequest.class, SourceQuery.SOURCE_QUERY_PLAYER_RES);
    }

    @Override
    protected Object decodeQueryPacket(ChannelHandlerContext ctx, SourceQueryPlayerRequest request, SourceQuerySinglePacket packet) {
        //TODO: Caused by: java.lang.IndexOutOfBoundsException: readerIndex(712) + length(4) exceeds writerIndex(712): UnpooledSlicedByteBuf(ridx: 712, widx: 712, cap: 712/712, unwrapped: PooledUnsafeDirectByteBuf(ridx: 5, widx: 717, cap: 2048))
        ByteBuf payload = packet.content();
        ArrayList<SourcePlayer> playerList = new ArrayList<>();
        //some servers send an empty info response packet, so we also return an empty response
        if (payload.readableBytes() > 0) {
            int playereCount = payload.readUnsignedByte();
            debug("Parsing Player Data (Player count: {})", playereCount);
            for (int i = 0; i < playereCount; i++) {
                if (payload.readableBytes() < 10)
                    break;
                int index = payload.readUnsignedByte();
                String name = NettyUtil.readString(payload, StandardCharsets.UTF_8);
                int score = payload.readIntLE();
                float duration = payload.readFloatLE();
                playerList.add(new SourcePlayer(index, name, score, duration));
            }
        } else {
            debug("Received an empty INFO response");
        }
        return new SourceQueryPlayerResponse(playerList);
    }
}
