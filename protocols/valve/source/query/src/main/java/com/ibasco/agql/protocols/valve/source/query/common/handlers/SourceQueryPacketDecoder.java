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

package com.ibasco.agql.protocols.valve.source.query.common.handlers;

import com.ibasco.agql.core.util.Bytes;
import com.ibasco.agql.core.util.Netty;
import com.ibasco.agql.protocols.valve.source.query.SourceQuery;
import com.ibasco.agql.protocols.valve.source.query.common.exceptions.InvalidPacketTypeException;
import com.ibasco.agql.protocols.valve.source.query.common.packets.SourceQueryPacket;
import com.ibasco.agql.protocols.valve.source.query.common.packets.util.SourceQueryPacketDecoderProvider;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Decodes a raw source query response ({@link io.netty.buffer.ByteBuf}) into an instance of {@link com.ibasco.agql.protocols.valve.source.query.common.packets.SourceQueryPacket}.
 * The packet can be a single-type or a split-type. Split-type packets should be re-assembled back to a single-type packet by the next handlers
 * <p>
 * Simple workflow of data:
 *
 * <pre>
 *
 * INCOMING DATA ----> DECODE TO PACKET ----> ASSEMBLE/PROCESS PACKET(s) ---> CREATE RESPONSE
 *
 * </pre>
 *
 * @author Rafael Luis Ibasco
 */
public class SourceQueryPacketDecoder extends MessageToMessageDecoder<ByteBuf> {

    private static final Logger log = LoggerFactory.getLogger(SourceQueryPacketDecoder.class);

    /** {@inheritDoc} */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
        try {
            msg.markReaderIndex();
            //Read the packet type from the buffer
            int type = msg.readIntLE();
            //make sure we have a valid packet type
            if (!SourceQuery.isValidPacketType(type))
                throw new InvalidPacketTypeException(type, String.format("Invalid source query packet type: %d (%s)", type, Bytes.toHexString(type)));
            SourceQueryPacket packet = SourceQueryPacketDecoderProvider.getDecoder(type).decode(msg);
            out.add(packet.retain());
            log.debug("{} INB => DECODED '{}' into \"{}\"", Netty.id(ctx.channel()), msg.getClass().getSimpleName(), packet);
        } catch (Exception e) {
            log.debug("{} INB => Failed to decode datagram packet into a SourceQueryPacket instance. Passing message to next handler", Netty.id(ctx.channel()), e);
            out.add(msg.resetReaderIndex().retain());
        }
    }
}
