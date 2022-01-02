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

import com.ibasco.agql.core.PacketEncoder;
import com.ibasco.agql.core.util.NettyUtil;
import com.ibasco.agql.protocols.valve.source.query.packets.SourceRconPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Encodes {@link SourceRconPacket} instances into {@link ByteBuf}
 */
public class SourceRconPacketEncoder extends MessageToByteEncoder<SourceRconPacket> {

    private static final Logger log = LoggerFactory.getLogger(SourceRconPacketEncoder.class);

    private final PacketEncoder<SourceRconPacket> encoder = new com.ibasco.agql.protocols.valve.source.query.packets.SourceRconPacketEncoder();

    public SourceRconPacketEncoder() {
        super(true);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, SourceRconPacket msg, ByteBuf out) throws Exception {
        ByteBuf encoded = null;
        try {
            encoded = encoder.encode(msg);
            out.writeBytes(encoded);
            log.debug("{} OUT => Encoded rcon packet '{}' (Written: {})", NettyUtil.id(ctx), msg, out.readableBytes());
        } finally {
            if (ReferenceCountUtil.release(encoded))
                log.debug("{} OUT => Released encoded message '{}'", NettyUtil.id(ctx), encoded);
        }
    }
}
