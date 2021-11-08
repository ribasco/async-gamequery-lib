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

import com.ibasco.agql.core.Messenger;
import com.ibasco.agql.core.exceptions.AsyncGameLibCheckedException;
import com.ibasco.agql.core.exceptions.NoResponseFoundForPacket;
import com.ibasco.agql.core.transport.ChannelAttributes;
import com.ibasco.agql.core.utils.ByteUtils;
import com.ibasco.agql.protocols.valve.source.query.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * Decodes the packet and wraps it to a response object
 */
public class SourceQueryPacketDecoder extends MessageToMessageDecoder<DatagramPacket> {

    private static final Logger log = LoggerFactory.getLogger(SourceQueryPacketDecoder.class);

    private final SourcePacketBuilder builder;

    private final Messenger<SourceServerRequest, SourceServerResponse> messenger;

    public SourceQueryPacketDecoder(Messenger<SourceServerRequest, SourceServerResponse> messenger, SourcePacketBuilder builder) {
        this.builder = builder;
        this.messenger = messenger;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) {
        //Create our response packet from the datagram we received
        SourceResponsePacket packet = builder.construct(msg.content());

        if (packet != null) {
            SourceServerResponse response = null;
            Throwable error = null;
            try {
                response = SourceResponseFactory.createResponse(packet);
                if (response == null)
                    throw new NoResponseFoundForPacket("Could not find a response handler for the received datagram packet", packet);
                response.setTransactionId(ctx.channel().id().asShortText());
                response.setSender(msg.sender());
                response.setRecipient(msg.recipient());
                response.setResponsePacket(packet);
                //for debugging
                if (log.isDebugEnabled()) {
                    msg.content().markReaderIndex();
                    byte[] data = new byte[msg.content().readableBytes()];
                    msg.content().readBytes(data);
                    msg.content().resetReaderIndex();
                    log.debug("Decoded response '{}' from '{}' (Id: {}, Size: {}, Data: {})", response.getClass().getSimpleName(), msg.sender(), response.id(), data.length, ByteUtils.toFormattedHex(data));
                }

            } catch (Throwable e) {
                error = new AsyncGameLibCheckedException("Error while decoding source query response", e);
            } finally {
                //Pass the message back to the messenger
                messenger.receive(response, error);
            }
        } else {
            log.error("Response packet is null for request: {}", ctx.channel().attr(ChannelAttributes.REQUEST));
        }

    }
}
