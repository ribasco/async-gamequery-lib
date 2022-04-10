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

package com.ibasco.agql.protocols.valve.source.query.info;

import com.ibasco.agql.core.NettyChannelContext;
import com.ibasco.agql.core.util.Netty;
import static com.ibasco.agql.protocols.valve.source.query.SourceQuery.*;
import com.ibasco.agql.protocols.valve.source.query.common.handlers.SourceQueryAuthDecoder;
import com.ibasco.agql.protocols.valve.source.query.common.packets.SourceQuerySinglePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

@SuppressWarnings({"DuplicatedCode", "SameParameterValue"})
public class SourceQueryInfoDecoder extends SourceQueryAuthDecoder<SourceQueryInfoRequest> {

    private static final Function<ByteBuf, String> READ_ASCII_BYTE_STR = buf -> buf.readCharSequence(1, StandardCharsets.US_ASCII).toString();

    private static final Function<Byte, Boolean> IS_VAC = byteVal -> byteVal == 1;

    private static final Function<Byte, Boolean> IS_PRIVATE_SERVER = byteVal -> byteVal != 0;

    public SourceQueryInfoDecoder() {
        super(SourceQueryInfoRequest.class, SOURCE_QUERY_INFO_RES);
    }

    @Override
    protected Object decodeQueryPacket(ChannelHandlerContext ctx, SourceQueryInfoRequest request, SourceQuerySinglePacket packet) throws Exception {
        NettyChannelContext context  = NettyChannelContext.getContext(ctx.channel());

        ByteBuf buf = packet.content();

        final SourceServer info = new SourceServer();
        info.setAddress(context.properties().remoteAddress());

        //NOTE: Some servers return an empty response. If this is the case, we skip the decoding process and simply return SourceServer instance
        if (buf.isReadable()) {
            debug("Attempting to decode A2S_INFO response (Reader Index: {}, Readable bytes: {})", buf.readerIndex(), buf.readableBytes());

            //I dont know what this byte (0x11) is for, there is no mention of this in the docs
            int undocumentedByte = buf.readUnsignedByte();

            if (isDebugEnabled())
                debug("INFO Dump ({})\n{}", undocumentedByte, Netty.prettyHexDump(buf));

            decodeField("name", buf, Netty::readString, info::setName);
            decodeField("mapName", buf, Netty::readString, info::setMapName);
            decodeField("gameDirectory", buf, Netty::readString, info::setGameDirectory);
            decodeField("gameDescription", buf, Netty::readString, info::setGameDescription);
            decodeField("appId", buf, buf::readShortLE, info::setAppId, Short::intValue);
            decodeField("playerCount", buf, buf::readUnsignedByte, info::setNumOfPlayers, Short::intValue);
            decodeField("maxPlayerCount", buf, buf::readUnsignedByte, info::setMaxPlayers, Short::intValue);
            decodeField("botCount", buf, buf::readUnsignedByte, info::setNumOfBots, Short::intValue);
            decodeField("isDedicated", buf, READ_ASCII_BYTE_STR, info::setDedicated, "d"::equalsIgnoreCase);
            decodeField("operatingSystem", buf, READ_ASCII_BYTE_STR, info::setOperatingSystem);
            decodeField("isPrivateServer", buf, buf::readByte, info::setPrivateServer, IS_PRIVATE_SERVER);
            decodeField("isSecure", buf, buf::readByte, info::setSecure, IS_VAC);
            decodeField("gameVersion", buf, Netty::readString, info::setGameVersion);

            //do we still have more bytes to process?
            if (!buf.isReadable()) {
                debug("The server '{}' did not contain any Extra Data Flags information we could decode. Skipping this process.", context.properties().remoteAddress());
                return new SourceQueryInfoResponse(info);
            }

            int flags = buf.readUnsignedByte();
            decodeFlag("edfServerPort", buf, flags, A2S_INFO_EDF_PORT, buf::readShortLE, null);
            decodeFlag("serverSteamId", buf, flags, A2S_INFO_EDF_STEAMID, buf::readLongLE, info::setServerId);
            decodeFlag("sourceTvPort", buf, flags, A2S_INFO_EDF_SOURCETV, buf::readShortLE, info::setTvPort, Short::intValue);
            decodeFlag("sourceTvName", buf, flags, A2S_INFO_EDF_SOURCETV, Netty::readString, info::setTvName);
            decodeFlag("serverTags", buf, flags, A2S_INFO_EDF_TAGS, Netty::readString, info::setServerTags);
            decodeFlag("appId64", buf, flags, A2S_INFO_EDF_GAMEID, buf::readLongLE, info::setGameId);
        } else {
            debug("Received an empty INFO response");
        }
        return new SourceQueryInfoResponse(info);
    }
}
