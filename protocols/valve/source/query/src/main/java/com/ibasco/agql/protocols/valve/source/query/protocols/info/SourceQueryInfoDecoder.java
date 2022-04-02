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

package com.ibasco.agql.protocols.valve.source.query.protocols.info;

import static com.ibasco.agql.core.util.BitUtil.isSet;
import com.ibasco.agql.core.util.ByteUtil;
import com.ibasco.agql.core.util.Functions;
import com.ibasco.agql.core.util.NettyUtil;
import static com.ibasco.agql.protocols.valve.source.query.SourceQuery.*;
import com.ibasco.agql.protocols.valve.source.query.handlers.SourceQueryAuthDecoder;
import com.ibasco.agql.protocols.valve.source.query.packets.SourceQuerySinglePacket;
import com.ibasco.agql.protocols.valve.source.query.pojos.SourceServer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings({"DuplicatedCode", "SameParameterValue"})
public class SourceQueryInfoDecoder extends SourceQueryAuthDecoder<SourceQueryInfoRequest> {

    private static final Function<ByteBuf, String> READ_ASCII_BYTE_STR = buf -> buf.readCharSequence(1, StandardCharsets.US_ASCII).toString();

    public SourceQueryInfoDecoder() {
        super(SourceQueryInfoRequest.class, SOURCE_QUERY_INFO_RES);
    }

    @Override
    protected Object decodeQueryPacket(ChannelHandlerContext ctx, SourceQueryInfoRequest request, SourceQuerySinglePacket packet) {
        ByteBuf buf = packet.content();

        final SourceServer info = new SourceServer();
        info.setAddress(getRequest().recipient());

        //NOTE: Some servers return an empty response. If this is the case, we skip the decoding process and simply return SourceServer instance
        if (buf.readableBytes() > 0) {
            try {
                debug("Attempting to decode A2S_INFO response (Reader Index: {}, Readable bytes: {})", buf.readerIndex(), buf.readableBytes());

                decodeField("name", buf, NettyUtil::readString, info::setName);
                decodeField("mapName", buf, NettyUtil::readString, info::setMapName);
                decodeField("gameDirectory", buf, NettyUtil::readString, info::setGameDirectory);
                decodeField("gameDescription", buf, NettyUtil::readString, info::setGameDescription);
                decodeField("appId", buf, buf::readShortLE, info::setAppId);
                decodeField("playerCount", buf, buf::readUnsignedByte, info::setNumOfPlayers);
                decodeField("maxPlayerCount", buf, buf::readUnsignedByte, info::setMaxPlayers);
                decodeField("botCount", buf, buf::readUnsignedByte, info::setNumOfBots);
                decodeField("isDedicated", buf, READ_ASCII_BYTE_STR, info::setDedicated, "d"::equalsIgnoreCase);
                decodeField("operatingSystem", buf, READ_ASCII_BYTE_STR, info::setOperatingSystem);
                decodeField("isPrivateServer", buf, buf::readByte, info::setPrivateServer, value -> value != 0);
                decodeField("isSecure", buf, buf::readByte, info::setSecure, vac -> vac == 1);
                decodeField("gameVersion", buf, NettyUtil::readString, info::setGameVersion);

                //do we still have more bytes to process?
                if (!buf.isReadable()) {
                    warn("Extra data flags not available for server. Skipping decode.");
                    return new SourceQueryInfoResponse(info);
                }

                int flags = buf.readUnsignedByte();
                decodeFlag("edfServerPort", buf, flags, A2S_INFO_EDF_PORT, buf::readShortLE, null);
                decodeFlag("serverSteamId", buf, flags, A2S_INFO_EDF_STEAMID, buf::readLongLE, info::setServerId);
                decodeFlag("sourceTvPort", buf, flags, A2S_INFO_EDF_SOURCETV, buf::readShortLE, info::setTvPort);
                decodeFlag("sourceTvName", buf, flags, A2S_INFO_EDF_SOURCETV, NettyUtil::readString, info::setTvName);
                decodeFlag("serverTags", buf, flags, A2S_INFO_EDF_TAGS, NettyUtil::readString, info::setServerTags);
                decodeFlag("appId64", buf, flags, A2S_INFO_EDF_GAMEID, buf::readLongLE, info::setGameId);
            } catch (Throwable e) {
                debug("An error occured while attempting to decode packet: '{}'", packet, e);
                if (isDebugEnabled()) {
                    byte[] dataDump = NettyUtil.getBufferContentsAll(buf);
                    debug("A2S_INFO Packet Dump: {}", ByteUtil.toHexString(dataDump));
                }
                throw e;
            }
        } else {
            debug("Received an empty INFO response");
        }
        return new SourceQueryInfoResponse(info);
    }

    private <A, B> void decodeFlag(String name, ByteBuf buf, int flags, int flag, Supplier<A> reader, Consumer<B> writer) {
        decodeFlag(name, buf, flags, flag, reader, writer, null);
    }

    private <A, B> void decodeFlag(String name, ByteBuf buf, int flags, int flag, Supplier<A> reader, Consumer<B> writer, Function<A, B> transformer) {
        if (!isSet(flags, flag)) {
            debug("Flag '{}' not set. Skipping (Readable bytes: {})", name, buf.readableBytes());
            return;
        }
        if (!buf.isReadable()) {
            debug("Skipped decoding flag '{}'. Buffer no longer readable (Reader Index: {}, Readable Bytes: {})", name, buf.readerIndex(), buf.readableBytes());
            return;
        }
        int startPosition = buf.readerIndex();
        try {
            debug("Decoding flag '{}' at index position '{}'", name, startPosition);
            A fromValue = reader.get();
            if (writer != null) {
                if (transformer == null)
                    transformer = Functions::cast;
                B toValue = transformer.apply(fromValue);
                writer.accept(toValue);
                debug("Saved decoded flag '{}' with value '{}'", name, toValue);
            }
        } catch (Throwable e) {
            error("Failed to decode flag '{}' at position '{}'", startPosition);
        }
    }

    private <A, B> void decodeFlag(String name, ByteBuf buf, int flags, int flag, Function<ByteBuf, A> reader, Consumer<B> writer) {
        decodeFlag(name, buf, flags, flag, reader, writer, null);
    }

    private <A, B> void decodeFlag(String name, ByteBuf buf, int flags, int flag, Function<ByteBuf, A> reader, Consumer<B> writer, Function<A, B> transformer) {
        if (!isSet(flags, flag)) {
            debug("Flag '{}' not set. Skipping (Readable bytes: {})", name, buf.readableBytes());
            return;
        }
        if (!buf.isReadable()) {
            debug("Skipped decoding flag '{}'. Buffer no longer readable (Reader Index: {}, Readable Bytes: {})", name, buf.readerIndex(), buf.readableBytes());
            return;
        }
        int startPosition = buf.readerIndex();
        try {
            debug("Decoding flag '{}' at index position '{}'", name, startPosition);
            A fromValue = reader.apply(buf);
            if (writer != null) {
                if (transformer == null)
                    transformer = Functions::cast;
                B toValue = transformer.apply(fromValue);
                writer.accept(toValue);
                debug("Saved decoded flag '{}' with value '{}'", name, toValue);
            }
        } catch (Throwable e) {
            error("Failed to decode flag '{}' at position '{}'", startPosition);
        }
    }

    private <A, B> void decodeField(String name, ByteBuf buf, Supplier<A> reader, Consumer<B> writer) {
        decodeField(name, buf, reader, writer, null);
    }

    private <A, B> void decodeField(String name, ByteBuf buf, Supplier<A> reader, Consumer<B> writer, Function<A, B> transformer) {
        if (!buf.isReadable()) {
            debug("Skipped decoding for field '{}'. Buffer no longer readable (Reader Index: {}, Readable Bytes: {})", name, buf.readerIndex(), buf.readableBytes());
            return;
        }
        int startPosition = buf.readerIndex();
        try {
            debug("Decoding field '{}' at index position '{}'", name, startPosition);
            A fromValue = reader.get();
            if (writer != null) {
                if (transformer == null)
                    transformer = Functions::cast;
                B toValue = transformer.apply(fromValue);
                writer.accept(toValue);
                debug("Saved decoded field '{}' with value '{}'", name, toValue);
            }
        } catch (Throwable e) {
            error("Failed to decode field '{}' at position '{}'", startPosition);
        }
    }

    private <A, B> void decodeField(String name, ByteBuf buf, Function<ByteBuf, A> reader, Consumer<B> writer) {
        decodeField(name, buf, reader, writer, null);
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
