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

package com.ibasco.agql.protocols.valve.source.query.protocols.info;

import static com.ibasco.agql.core.util.BitUtil.isSet;
import static com.ibasco.agql.core.util.NettyUtil.readString;
import static com.ibasco.agql.protocols.valve.source.query.SourceQuery.*;
import com.ibasco.agql.protocols.valve.source.query.handlers.SourceQueryAuthDecoder;
import com.ibasco.agql.protocols.valve.source.query.packets.SourceQuerySinglePacket;
import com.ibasco.agql.protocols.valve.source.query.pojos.SourceServer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class SourceQueryInfoDecoder extends SourceQueryAuthDecoder<SourceQueryInfoRequest> {

    public SourceQueryInfoDecoder() {
        super(SourceQueryInfoRequest.class, SOURCE_QUERY_INFO_RES);
    }

    @Override
    protected Object decodeQueryPacket(ChannelHandlerContext ctx, SourceQueryInfoRequest request, SourceQuerySinglePacket packet) {
        ByteBuf buf = packet.content();

        final SourceServer info = new SourceServer();

        String name = readString(buf);
        String map = readString(buf);
        String folder = readString(buf);
        String game = readString(buf);
        int appId = buf.readShortLE();
        int playerCount = buf.readUnsignedByte();
        int maxPlayerCount = buf.readUnsignedByte();
        int botCount = buf.readUnsignedByte();
        String serverType = buf.readCharSequence(1, StandardCharsets.US_ASCII).toString();
        String environmentType = buf.readCharSequence(1, StandardCharsets.US_ASCII).toString();
        String visibility = buf.readByte() == 0 ? "public" : "private";
        int vac = buf.readByte();
        String version = readString(buf);
        int flags = buf.readUnsignedByte();
        int port = -1;
        if (isSet(flags, A2S_INFO_EDF_PORT)) {
            port = buf.readShortLE();
        }
        long steamId = -1;
        if (isSet(flags, A2S_INFO_EDF_STEAMID)) {
            steamId = buf.readLongLE();
        }
        int specPort = -1;
        String specName = null;
        if (isSet(flags, A2S_INFO_EDF_SOURCETV)) {
            specPort = buf.readShortLE();
            specName = readString(buf);
        }
        String tags = null;
        if (isSet(flags, A2S_INFO_EDF_TAGS)) {
            tags = readString(buf);
        }
        long gameId = -1;
        if (isSet(flags, A2S_INFO_EDF_GAMEID)) {
            gameId = buf.readLongLE();
        }

        assert getRequest().recipient() != null;
        assert getRequest().recipient() instanceof InetSocketAddress;

        InetSocketAddress addr = getRequest().recipient();

        info.setAddress(new InetSocketAddress(addr.getAddress(), addr.getPort()));
        info.setName(name);
        info.setMapName(map);
        info.setGameDirectory(folder);
        info.setGameDescription(game);
        info.setAppId(appId);
        info.setNumOfPlayers(playerCount);
        info.setMaxPlayers(maxPlayerCount);
        info.setNumOfBots(botCount);
        info.setDedicated("d".equalsIgnoreCase(serverType));
        info.setOperatingSystem(environmentType);
        info.setPrivateServer("private".equalsIgnoreCase(visibility));
        info.setSecure(vac == 1);
        info.setGameVersion(version);
        info.setServerId(steamId);
        info.setTvName(specName);
        info.setTvPort(specPort);
        info.setServerTags(tags);
        info.setGameId(gameId);

        return new SourceQueryInfoResponse(info);
    }
}
