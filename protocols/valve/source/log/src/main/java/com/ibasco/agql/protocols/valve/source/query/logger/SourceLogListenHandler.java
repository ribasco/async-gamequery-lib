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

package com.ibasco.agql.protocols.valve.source.query.logger;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.AttributeKey;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * Default handler for parsing incoming source log messages
 * <p>
 * Created by raffy on 10/25/2016.
 *
 * @author Rafael Luis Ibasco
 */
public class SourceLogListenHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    static final AttributeKey<AtomicReference<Consumer<SourceLogEntry>>> CALLBACK = AttributeKey.newInstance("sourceLogCallback");

    /** {@inheritDoc} */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        ByteBuf data = msg.content();
        if (data.readableBytes() > 6 && data.readIntLE() == -1) {
            byte[] raw = new byte[data.readableBytes() - 2];
            data.readBytes(raw);
            data.skipBytes(2);
            //Pass to the callback
            AtomicReference<Consumer<SourceLogEntry>> callbackRef = ctx.channel().attr(CALLBACK).get();
            if (callbackRef != null && callbackRef.get() != null) {
                callbackRef.get().accept(new SourceLogEntry(new String(raw, Charsets.UTF_8), msg.sender()));
            }
        }
    }
}
