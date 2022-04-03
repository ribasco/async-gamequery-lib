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

import com.ibasco.agql.core.util.ByteUtil;
import com.ibasco.agql.core.util.NettyUtil;
import com.ibasco.agql.protocols.valve.source.query.SourceQuery;
import com.ibasco.agql.protocols.valve.source.query.handlers.SourceQueryAuthDecoder;
import com.ibasco.agql.protocols.valve.source.query.packets.SourceQuerySinglePacket;
import com.ibasco.agql.protocols.valve.source.query.pojos.SourcePlayer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.function.Function;

public class SourceQueryPlayersDecoder extends SourceQueryAuthDecoder<SourceQueryPlayerRequest> {

    private static final Logger log = LoggerFactory.getLogger(SourceQueryPlayersDecoder.class);

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
            debug(log,"Decoding player payload data (Player count: {})", playereCount);
            for (int i = 0; i < playereCount; i++) {
                if (payload.readableBytes() < 10)
                    break;
                debug(log, "Decoding player #{}", i);
                short index = decodeField(i, "Index", (short) -1, payload, ByteBuf::readUnsignedByte);
                String name = decodeField(i, "Name", "N/A", payload, buf -> NettyUtil.readString(buf, StandardCharsets.UTF_8));
                int score = decodeField(i, "Score", -1, payload, ByteBuf::readIntLE);
                float duration = decodeField(i, "Duration", -1f, payload, ByteBuf::readFloatLE);
                playerList.add(new SourcePlayer(index, name, score, duration));
            }
        } else {
            debug(log, "Received an empty PLAYERS response");
        }
        return new SourceQueryPlayerResponse(playerList);
    }

    private <V> V decodeField(int index, String name, V defaultValue, ByteBuf payload, Function<ByteBuf, V> decoder) {
        int readerIndex = payload.readerIndex();
        if (!payload.isReadable()) {
            debug("Skipped decoding for field '{}'. Buffer no longer readable (Reader Index: {}, Readable Bytes: {})", name, payload.readerIndex(), payload.readableBytes());
            return defaultValue;
        }
        V res;
        try {
            res = decoder.apply(payload);
        } catch (Throwable error) {
            try {
                payload.markReaderIndex();
                payload.readerIndex(readerIndex);
                byte[] payloadData = NettyUtil.getBufferContents(payload);
                String payloadDump = ByteUtil.toHexString(payloadData);
                error("{}) Failed to decode packet into player data ({} bytes):\n{}", index, payloadData.length, payloadDump, error);
            } finally {
                payload.resetReaderIndex();
                res = null;
            }
        }
        res = res == null ? defaultValue : res;
        debug(log, "\t{}) Decoded '{}' to '{}'", index, name, res);
        return res;
    }
}
