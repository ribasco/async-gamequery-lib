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

import com.ribasco.gamecrawler.protocols.DefaultResponseWrapper;
import com.ribasco.gamecrawler.protocols.Response;
import com.ribasco.gamecrawler.protocols.ResponseWrapper;
import com.ribasco.gamecrawler.protocols.Session;
import com.ribasco.gamecrawler.protocols.valve.server.SourcePacketHelper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Responsible for decoding datagram packets and translating them to meaningful objects.
 */
public class SourceResponseDecoder extends MessageToMessageDecoder<DatagramPacket> {

    private static final Logger log = LoggerFactory.getLogger(SourceResponseDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) throws Exception {
        InetSocketAddress senderAddress = msg.sender();
        ByteBuf data = msg.content();

        //Is this a valid header for the Server Info Response?
        if (!SourcePacketHelper.isValidResponsePacket(data)) {
            if (log.isDebugEnabled())
                System.out.println(ByteBufUtil.prettyHexDump(msg.content()));
            log.warn("No valid handlers found for this packet. Discarding Packet.");
            msg.release();
            return;
        }

        //At this point, the buffer SHOULD ALWAYS start from the response header. Not from the protocol header.
        Response responsePacket = SourcePacketHelper.getResponsePacket(data);

        //We dont have a valid response packet for this message, we need to discard it.
        if (responsePacket == null) {
            log.warn("No valid response packet handler was found for {}:{}", senderAddress.getAddress().getHostAddress(), senderAddress.getPort());
            if (log.isDebugEnabled())
                System.out.println(ByteBufUtil.prettyHexDump(msg.content()));
            return;
        }

        //Retrieve the session id for this response
        String sessionId = Session.getSessionId(msg.sender(), responsePacket);

        //Verify we have a session id
        if (StringUtils.isEmpty(sessionId)) {
            log.warn("Unable to find session id for response packet : {}", responsePacket.getClass().getSimpleName());
            return;
        }

        //Start Decoding the response
        Object responseData = responsePacket.getResponseData();

        log.debug("Found a valid response packet for {}:{} (Response Data: {})", msg.sender().getAddress().getHostAddress(), msg.sender().getPort(), responseData);

        //Wrap the details in our ResponseWrapper
        ResponseWrapper response = new DefaultResponseWrapper(sessionId, senderAddress, responseData);

        //Wrap the object in an Response class
        out.add(response);
    }
}
