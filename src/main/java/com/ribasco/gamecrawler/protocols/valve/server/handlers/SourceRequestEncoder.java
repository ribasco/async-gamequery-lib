/*
 * MIT License
 *
 * Copyright (c) [year] [fullname]
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
 */

package com.ribasco.gamecrawler.protocols.valve.server.handlers;

import com.ribasco.gamecrawler.protocols.GameRequestEnvelope;
import com.ribasco.gamecrawler.protocols.GameRequestPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * <p>Translate Game Queries to the underlying protocol implementation for transport</p>
 */
public class SourceRequestEncoder<T extends GameRequestPacket> extends MessageToMessageEncoder<GameRequestEnvelope<T>> {
    private static final Logger log = LoggerFactory.getLogger(SourceRequestEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, GameRequestEnvelope<T> requestEnvelope, List<Object> out) throws Exception {
        //Unpack the contents of the message
        InetSocketAddress destination = requestEnvelope.getAddress();
        GameRequestPacket request = requestEnvelope.getRequest();

        //Translate the content to it's intended form
        byte[] data = request.getRequestData();

        //Pack the message into a buffer (message wrapper)
        ByteBuf buffer = ctx.alloc().buffer(data.length);
        buffer.writeBytes(data);

        out.add(new DatagramPacket(buffer, destination));

        log.debug("Request {} Successfully Sent to {}:{}", request.getClass().getSimpleName(), destination.getAddress().getHostAddress(), destination.getPort());
        if (log.isDebugEnabled()) {
            System.out.println(ByteBufUtil.prettyHexDump(buffer));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
