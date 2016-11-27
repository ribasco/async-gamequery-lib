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

package com.ibasco.agql.protocols.valve.steam.master.handlers;

import com.ibasco.agql.protocols.valve.steam.master.MasterServerPacketBuilder;
import com.ibasco.agql.protocols.valve.steam.master.MasterServerResponse;
import com.ibasco.agql.protocols.valve.steam.master.packets.MasterServerResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiConsumer;

public class MasterServerPacketDecoder extends MessageToMessageDecoder<DatagramPacket> {

    private static final Logger log = LoggerFactory.getLogger(MasterServerPacketBuilder.class);
    private BiConsumer<MasterServerResponse, Throwable> responseCallback;
    private MasterServerPacketBuilder builder;

    public MasterServerPacketDecoder(BiConsumer<MasterServerResponse, Throwable> responseCallback, MasterServerPacketBuilder builder) {
        this.responseCallback = responseCallback;
        this.builder = builder;
    }

    //TODO: maybe make a more generic version of this class?
    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) throws Exception {
        //Create our response packet from the datagram we received
        final MasterServerResponsePacket packet = builder.construct(msg.content());
        if (packet != null) {
            final MasterServerResponse response = new MasterServerResponse();
            if (response != null) {
                response.setSender(msg.sender());
                response.setRecipient(msg.recipient());
                response.setResponsePacket(packet);
                log.debug("Receiving Data '{}' from '{}' using Channel Id: {}", response.getClass().getSimpleName(), ctx.channel().remoteAddress(), ctx.channel().id());
                //Pass the message back to the messenger
                responseCallback.accept(response, null);
                return;
            }
        }
        throw new IllegalStateException("No response packet found for the incoming datagram");
    }
}
