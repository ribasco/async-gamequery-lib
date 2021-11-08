/*
 * MIT License
 *
 * Copyright (c) 2018 Asynchronous Game Query Library
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ibasco.agql.protocols.valve.source.query.handlers;

import com.ibasco.agql.core.AbstractPacketBuilder;
import com.ibasco.agql.core.transport.handlers.AbstractRequestEncoder;
import com.ibasco.agql.core.utils.ByteUtils;
import com.ibasco.agql.protocols.valve.source.query.SourceRequestPacket;
import com.ibasco.agql.protocols.valve.source.query.SourceServerPacket;
import com.ibasco.agql.protocols.valve.source.query.SourceServerRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by raffy on 9/17/2016.
 */
public class SourceQueryRequestEncoder extends AbstractRequestEncoder<SourceServerRequest, SourceServerPacket> {
    private static final Logger log = LoggerFactory.getLogger(SourceQueryRequestEncoder.class);

    public SourceQueryRequestEncoder(AbstractPacketBuilder<SourceServerPacket> builder) {
        super(builder);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, SourceServerRequest request, List<Object> out) throws Exception {
        SourceRequestPacket packet = request.getMessage();
        byte[] deconstructedPacket = builder.deconstruct(packet);
        log.debug("Sending request packet (Type: {}, Size: {}): {}", packet.getClass().getSimpleName(), deconstructedPacket.length, ByteUtils.toFormattedHex(deconstructedPacket));
        if (deconstructedPacket.length > 0) {
            ByteBuf buffer = ctx.alloc().buffer(deconstructedPacket.length).writeBytes(deconstructedPacket);
            out.add(new DatagramPacket(buffer, request.recipient()));
        }
    }
}
