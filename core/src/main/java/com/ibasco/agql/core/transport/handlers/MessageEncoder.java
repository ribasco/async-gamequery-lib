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

package com.ibasco.agql.core.transport.handlers;

import com.ibasco.agql.core.Envelope;
import com.ibasco.agql.core.NettyChannelContext;
import com.ibasco.agql.core.util.Netty;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.ReferenceCountUtil;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Primary intercept for all outbound requests.
 *
 * @author Rafael Luis Ibasco
 */
public class MessageEncoder extends MessageToMessageEncoder<Object> {

    /** Constant <code>NAME="requestEncoder"</code> */
    public static final String NAME = "requestEncoder";

    private static final Logger log = LoggerFactory.getLogger(MessageEncoder.class);

    /** {@inheritDoc} */
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        NettyChannelContext context = NettyChannelContext.getContext(ctx.channel());
        log.debug("{} OUT => Intercepted request of type '{}' ({})", Netty.id(ctx.channel()), msg.getClass().getSimpleName(), msg);

        if (!(msg instanceof Envelope<?>)) {
            log.debug("{} OUT => Passing message '{}' to the next handler", Netty.id(ctx.channel()), msg.getClass().getSimpleName());
            out.add(msg);
            return;
        }

        Envelope<?> envelope = (Envelope<?>) msg;
        if (envelope.sender() == null) {
            log.debug("{} OUT => Updated local address of envelope to '{}'", Netty.id(ctx.channel()), ctx.channel().localAddress());
            envelope.sender(ctx.channel().localAddress());
        }

        //update channel attribute request
        /*if (envelope instanceof MessageEnvelope<?>) {
            if (context.properties().envelope() == null) {
                //noinspection unchecked
                context.properties().envelope((Envelope<AbstractRequest>) envelope);
                log.debug("{} OUT => Updated request attribute to '{}'", NettyUtil.id(ctx.channel()), envelope);
            }
        }*/

        //unwrap envelope and send it's content
        out.add(ReferenceCountUtil.retain(msg)); //call retain because this decoder will automatically release the message
    }
}
