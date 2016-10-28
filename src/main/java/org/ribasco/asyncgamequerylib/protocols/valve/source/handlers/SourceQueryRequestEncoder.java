/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Luis Ibasco
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **************************************************************************************************/

package org.ribasco.asyncgamequerylib.protocols.valve.source.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import org.ribasco.asyncgamequerylib.core.AbstractPacketBuilder;
import org.ribasco.asyncgamequerylib.core.transport.handlers.AbstractRequestEncoder;
import org.ribasco.asyncgamequerylib.protocols.valve.source.SourceRequestPacket;
import org.ribasco.asyncgamequerylib.protocols.valve.source.SourceServerPacket;
import org.ribasco.asyncgamequerylib.protocols.valve.source.SourceServerRequest;
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
        if (deconstructedPacket != null && deconstructedPacket.length > 0) {
            ByteBuf buffer = ctx.alloc().buffer(deconstructedPacket.length).writeBytes(deconstructedPacket);
            out.add(new DatagramPacket(buffer, request.recipient()));
        }
    }
}
