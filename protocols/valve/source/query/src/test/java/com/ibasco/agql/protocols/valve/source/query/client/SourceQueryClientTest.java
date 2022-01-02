/*
 * Copyright 2021-2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.protocols.valve.source.query.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

class SourceQueryClientTest {

    private static final Logger log = LoggerFactory.getLogger(SourceQueryClientTest.class);

    @Test
    public void testSourceQuery() throws Exception {
        log.debug("Running test source query");
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup loopGroup = new NioEventLoopGroup();
        bootstrap.group(loopGroup)
                 .channel(NioDatagramChannel.class)
                 .handler(new ChannelInitializer<Channel>() {
                     @Override
                     protected void initChannel(Channel ch) throws Exception {
                         ChannelPipeline pipeline = ch.pipeline();
                         pipeline.addLast(new MessageToMessageDecoder<DatagramPacket>() {
                             @Override
                             protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List out) throws Exception {
                                 ByteBuf res = msg.content();
                                 byte[] header = new byte[4];
                                 res.readBytes(header);
                                 byte identifier = res.readByte();

                                 log.info("Header: {}", toHex(header));
                                 log.info("Identifier: {}", toHex(identifier));

                                 if (identifier == 0x41) {
                                     log.info("Server responded with a challenge number");
                                 } else if (identifier == 0x49) {
                                     log.info("Server responded with an A2S_INFO packet");
                                 }

                                 log.info("Closing channel");
                                 ctx.channel().close();
                             }
                         });
                     }
                 });

        Channel channel = bootstrap.bind(0).channel();

        ByteBuf requestPacket = Unpooled.buffer(1400);
        requestPacket.writeIntLE(0xFFFFFFFF);
        requestPacket.writeByte(0x54);
        requestPacket.writeBytes("Source Engine Query\0".getBytes());
        requestPacket.writerIndex(requestPacket.capacity() - 1);

        log.info("Sending requset packet: {} ({})", toHex(requestPacket.array()), requestPacket.readableBytes());
        ChannelFuture future = channel.writeAndFlush(new DatagramPacket(requestPacket, new InetSocketAddress("192.168.1.34", 27015)));
        log.info("Waiting for server to respond");
        future.channel().closeFuture().await();
        log.info("Exiting");
    }

    private static String toHex(byte... data) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            res.append("0x");
            res.append(String.format("%02x", data[i]).toUpperCase());
            if (i < data.length - 1)
                res.append(" ");
        }
        return res.toString();
    }
}