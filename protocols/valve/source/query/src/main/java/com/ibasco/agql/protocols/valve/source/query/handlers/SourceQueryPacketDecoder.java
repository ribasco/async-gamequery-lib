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

package com.ibasco.agql.protocols.valve.source.query.handlers;

import com.ibasco.agql.core.util.ByteUtil;
import com.ibasco.agql.core.util.NettyUtil;
import com.ibasco.agql.protocols.valve.source.query.SourceQuery;
import com.ibasco.agql.protocols.valve.source.query.exceptions.InvalidPacketTypeException;
import com.ibasco.agql.protocols.valve.source.query.packets.SourceQueryPacket;
import com.ibasco.agql.protocols.valve.source.query.packets.util.SourceQueryPacketDecoderProvider;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Decodes a raw source query response ({@link ByteBuf}) into an instance of {@link SourceQueryPacket}.
 * The packet can be a single-type or a split-type. Split-type packets should be re-assembled back to a single-type packet by the next handlers
 *
 * @author Rafael Luis Ibasco
 */
public class SourceQueryPacketDecoder extends MessageToMessageDecoder<ByteBuf> {

    private static final Logger log = LoggerFactory.getLogger(SourceQueryPacketDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
        try {
            msg.markReaderIndex();
            //Read the packet type from the buffer
            int type = msg.readIntLE();
            //make sure we have a valid packet type
            if (!SourceQuery.isValidPacketType(type))
                throw new InvalidPacketTypeException(type, String.format("Invalid source query packet type: %d (%s)", type, ByteUtil.toHexString(type)));
            SourceQueryPacket packet = SourceQueryPacketDecoderProvider.getDecoder(type).decode(msg);
            out.add(packet.retain());
            log.debug("{} INB => DECODED '{}' into \"{}\"", NettyUtil.id(ctx.channel()), msg.getClass().getSimpleName(), packet);
        } catch (Throwable e) {
            log.error("{} INB => Failed to decode datagram packet into a SourceQueryPacket instance. Passing message to next handler", NettyUtil.id(ctx.channel()), e);
            out.add(msg.resetReaderIndex().retain());
        }
    }
}
