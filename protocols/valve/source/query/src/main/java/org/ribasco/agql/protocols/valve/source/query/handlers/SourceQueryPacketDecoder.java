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

package org.ribasco.agql.protocols.valve.source.query.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.ribasco.agql.core.exceptions.AsyncGameLibCheckedException;
import org.ribasco.agql.protocols.valve.source.query.SourcePacketBuilder;
import org.ribasco.agql.protocols.valve.source.query.SourceResponseFactory;
import org.ribasco.agql.protocols.valve.source.query.SourceResponsePacket;
import org.ribasco.agql.protocols.valve.source.query.SourceServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * Decodes the packet and wraps it to a response object
 */
public class SourceQueryPacketDecoder extends MessageToMessageDecoder<DatagramPacket> {

    private static final Logger log = LoggerFactory.getLogger(SourceQueryPacketDecoder.class);

    private SourcePacketBuilder builder;
    private BiConsumer<SourceServerResponse, Throwable> responseHandler;

    public SourceQueryPacketDecoder(BiConsumer<SourceServerResponse, Throwable> responseHandler, SourcePacketBuilder builder) {
        this.builder = builder;
        this.responseHandler = responseHandler;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) throws Exception {
        //Create our response packet from the datagram we received
        final SourceResponsePacket packet = builder.construct(msg.content());

        if (packet != null) {
            try {
                SourceServerResponse response = SourceResponseFactory.createResponseFrom(packet);
                if (response != null) {
                    response.setSender(msg.sender());
                    response.setRecipient(msg.recipient());
                    response.setResponsePacket(packet);
                    log.debug("Receiving Data '{}' from '{}' using Channel Id: {}", response.getClass().getSimpleName(), ctx.channel().remoteAddress(), ctx.channel().id());
                    //Pass the message back to the messenger
                    responseHandler.accept(response, null);
                    return;
                }
            } catch (Exception e) {
                responseHandler.accept(null, new AsyncGameLibCheckedException("Error while decoding source query response", e));
            }
        }
        throw new IllegalStateException("No response packet found for the incoming datagram");
    }
}
