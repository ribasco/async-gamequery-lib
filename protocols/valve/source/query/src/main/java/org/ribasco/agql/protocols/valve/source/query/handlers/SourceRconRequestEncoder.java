/*
 * MIT License
 *
 * Copyright (c) 2016 Asynchronous Game Query Library
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

package org.ribasco.agql.protocols.valve.source.query.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.ribasco.agql.core.transport.handlers.AbstractRequestEncoder;
import org.ribasco.agql.protocols.valve.source.query.SourceRconPacket;
import org.ribasco.agql.protocols.valve.source.query.SourceRconPacketBuilder;
import org.ribasco.agql.protocols.valve.source.query.SourceRconRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * Created by raffy on 9/24/2016.
 */
public class SourceRconRequestEncoder extends AbstractRequestEncoder<SourceRconRequest, SourceRconPacket> {
    private static final Logger log = LoggerFactory.getLogger(SourceRconRequestEncoder.class);

    public SourceRconRequestEncoder(SourceRconPacketBuilder builder) {
        super(builder);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, SourceRconRequest msg, List<Object> out) throws Exception {
        byte[] deconstructedPacket = builder.deconstruct((SourceRconPacket) msg.getMessage());
        if (deconstructedPacket != null && deconstructedPacket.length > 0) {
            ByteBuf buffer = ctx.alloc().buffer(deconstructedPacket.length).writeBytes(deconstructedPacket);
            out.add(buffer);
        }
    }
}
