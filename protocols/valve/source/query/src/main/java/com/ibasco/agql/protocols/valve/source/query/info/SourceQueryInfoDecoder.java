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

import static com.ibasco.agql.protocols.valve.source.query.SourceQuery.A2S_INFO_EDF_GAMEID;
import static com.ibasco.agql.protocols.valve.source.query.SourceQuery.A2S_INFO_EDF_PORT;
import static com.ibasco.agql.protocols.valve.source.query.SourceQuery.A2S_INFO_EDF_SOURCETV;
import static com.ibasco.agql.protocols.valve.source.query.SourceQuery.A2S_INFO_EDF_STEAMID;
import static com.ibasco.agql.protocols.valve.source.query.SourceQuery.A2S_INFO_EDF_TAGS;
import static com.ibasco.agql.protocols.valve.source.query.SourceQuery.SOURCE_QUERY_INFO_RES;
import com.ibasco.agql.core.NettyChannelContext;
import com.ibasco.agql.core.util.Netty;
import com.ibasco.agql.protocols.valve.source.query.common.handlers.SourceQueryAuthDecoder;
import com.ibasco.agql.protocols.valve.source.query.common.packets.SourceQuerySinglePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

/**
 * <p>SourceQueryInfoDecoder class.</p>
 *
 * @author Rafael Luis Ibasco
 */
@SuppressWarnings({"DuplicatedCode", "SameParameterValue"})
public class SourceQueryInfoDecoder extends SourceQueryAuthDecoder<SourceQueryInfoRequest> {

    private static final Function<ByteBuf, String> READ_ASCII_BYTE_STR = buf -> buf.readCharSequence(1, StandardCharsets.US_ASCII).toString();

    private static final Function<ByteBuf, String> PEEK_ASCII_BYTE_STR = buf -> new String(new byte[] {buf.getByte(buf.readerIndex())}, StandardCharsets.US_ASCII);

    private static final Function<Byte, Boolean> IS_VAC = byteVal -> byteVal == 1;

    private static final Function<Byte, Boolean> IS_PRIVATE_SERVER = byteVal -> byteVal != 0;

    /**
     * <p>Constructor for SourceQueryInfoDecoder.</p>
     */
    public SourceQueryInfoDecoder() {
        super(SourceQueryInfoRequest.class, SOURCE_QUERY_INFO_RES);
    }

    /** {@inheritDoc} */
    @Override
    protected Object decodeQueryPacket(ChannelHandlerContext ctx, SourceQueryInfoRequest request, SourceQuerySinglePacket packet) throws Exception {
        NettyChannelContext context = NettyChannelContext.getContext(ctx.channel());

        ByteBuf buf = packet.content();

        final SourceServer info = new SourceServer();

        info.setAddress(context.properties().remoteAddress());

        //NOTE: Some servers return an empty response. If this is the case, we skip the decoding process and simply return SourceServer instance
        if (buf.isReadable()) {
            debug("Attempting to decode A2S_INFO response (Reader Index: {}, Readable bytes: {})", buf.readerIndex(), buf.readableBytes());

            decodeField("protocol", buf, buf::readUnsignedByte, info::setNetworkVersion, Short::byteValue);
            decodeField("name", buf, Netty::readString, info::setName);
            decodeField("mapName", buf, Netty::readString, info::setMapName);
            decodeField("gameDirectory", buf, Netty::readString, info::setGameDirectory);
            decodeField("gameDescription", buf, Netty::readString, info::setGameDescription);
            decodeField("appId", buf, buf::readShortLE, info::setAppId, Short::intValue);
            decodeField("playerCount", buf, buf::readUnsignedByte, info::setNumOfPlayers, Short::intValue);
            decodeField("maxPlayerCount", buf, buf::readUnsignedByte, info::setMaxPlayers, Short::intValue);
            decodeField("botCount", buf, buf::readUnsignedByte, info::setNumOfBots, Short::intValue);

            decodeField("isSourceTvProxy", buf, PEEK_ASCII_BYTE_STR, info::setSourceTvProxy, "p"::equalsIgnoreCase);
            decodeField("isDedicated", buf, READ_ASCII_BYTE_STR, info::setDedicated, "d"::equalsIgnoreCase); //d = dedicated, l = non-dedicated, p = source tv proxy
            decodeField("operatingSystem", buf, READ_ASCII_BYTE_STR, info::setOperatingSystem); //l = linux, w = windows, m = mac
            decodeField("isPrivateServer", buf, buf::readByte, info::setPrivateServer, IS_PRIVATE_SERVER); //0 = public, 1 = private
            decodeField("isSecure", buf, buf::readByte, info::setSecure, IS_VAC); //0 = unsecured, 1 = secured
            decodeField("gameVersion", buf, Netty::readString, info::setGameVersion);

            //do we still have more bytes to process?
            if (!buf.isReadable()) {
                debug("The server '{}' did not contain any Extra Data Flags information we could decode. Skipping this process.", context.properties().remoteAddress());
                return new SourceQueryInfoResponse(info);
            }

            int flags = buf.readUnsignedByte();
            decodeFlag("edfServerPort", buf, flags, A2S_INFO_EDF_PORT, buf::readUnsignedShortLE, info::setGamePort, Integer::intValue);
            decodeFlag("edfServerSteamId", buf, flags, A2S_INFO_EDF_STEAMID, buf::readLongLE, info::setServerId);
            decodeFlag("edfSourceTvPort", buf, flags, A2S_INFO_EDF_SOURCETV, buf::readShortLE, info::setTvPort, Short::intValue);
            decodeFlag("edfSourceTvName", buf, flags, A2S_INFO_EDF_SOURCETV, Netty::readString, info::setTvName);
            decodeFlag("edfServerTags", buf, flags, A2S_INFO_EDF_TAGS, Netty::readString, info::setServerTags);
            decodeFlag("edfAppId64", buf, flags, A2S_INFO_EDF_GAMEID, buf::readLongLE, info::setGameId);
        } else {
            debug("Received an empty INFO response");
        }
        return new SourceQueryInfoResponse(info);
    }
}
