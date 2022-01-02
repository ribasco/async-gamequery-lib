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

package com.ibasco.agql.core.handlers;

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.AbstractResponse;
import com.ibasco.agql.core.Envelope;
import com.ibasco.agql.core.transport.ChannelAttributes;
import com.ibasco.agql.core.util.MessageEnvelopeBuilder;
import com.ibasco.agql.core.util.NettyUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.AddressedEnvelope;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.Attribute;
import io.netty.util.ReferenceCountUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

/**
 * The initial global intercept for all raw data recived from a remote address. This handler creates a response {@link Envelope} for the
 * message received, unwraps the message's content then passes it to the next handlers in the pipeline for further processing.
 *
 * @author Rafael Luis Ibasco
 */
public class MessageDecoder extends ChannelInboundHandlerAdapter {

    public static final String NAME = "responseDecoder";

    private static final Logger log = LoggerFactory.getLogger(MessageDecoder.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, @NotNull Object msg) {
        final Channel channel = ctx.channel();
        final String id = NettyUtil.id(channel);

        //Make sure we have a request associated, otherwise do not propagate
        if (hasInvalidRequest(channel)) {
            log.debug("{} INB => Received incoming data but No VALID request found. It has either been cleared or has been marked as completed. Not propagating (Msg: {}, Request: {})", id, msg, channel.attr(ChannelAttributes.REQUEST).get());
            if (msg instanceof ByteBuf && log.isDebugEnabled()) {
                ByteBuf b = (ByteBuf) msg;
                NettyUtil.dumpBuffer(log::debug, String.format("%s INB => Discarded packet (Size: %d)", NettyUtil.id(ctx), b.readableBytes()), b, 32);
            }
            ReferenceCountUtil.release(msg);
            return;
        }

        try {
            log.debug("{} INB => Received incoming data from server of type: {} (Length: {} bytes)", id, String.format("%s (%d)", msg.getClass().getSimpleName(), msg.hashCode()), getResponseLength(msg));
            Attribute<Envelope<AbstractResponse>> responseAttr = channel.attr(ChannelAttributes.RESPONSE);

            //if already initialized, dont overwrite, just skip
            if (responseAttr.get() != null) {
                Envelope<AbstractRequest> request = ctx.channel().attr(ChannelAttributes.REQUEST).get();
                Envelope<AbstractResponse> response = responseAttr.get();
                assert request != null;
                assert response != null;

                //TODO: Dirty workaround, re-evaluate
                boolean updateResponse = response.promise() != request.promise();
                if (!updateResponse) {
                    log.debug("{} INB => Response already initialized. Skipping. (Request: {}, Response: {})", id, ctx.channel().attr(ChannelAttributes.REQUEST).get(), ctx.channel().attr(ChannelAttributes.RESPONSE).get());
                    return;
                } else {
                    log.debug("{} INB => Response neeeds to be updated (Reason: Promise does not match, Request: {}, Response: {})", id, request, response);
                }
            }

            //update with new response
            responseAttr.set(newResponse(channel, msg));
            log.debug("{} INB => Initialized channel with response '{}'", id, responseAttr.get());
        } catch (Throwable ex) {
            log.error(String.format("%s INB => Error occured during initialization", id), ex);
            ctx.fireExceptionCaught(ex);
        } finally {
            Object decoded;
            if (msg instanceof ByteBufHolder) {
                decoded = ((ByteBufHolder) msg).content();
                log.debug("{} INB => Passing decoded message ({}) to the next handler(s)", id, decoded.getClass().getSimpleName());
            } else {
                decoded = msg;
                log.debug("{} INB => Passing message ({}) to the next handler(s)", id, decoded.getClass().getSimpleName());
            }
            ctx.fireChannelRead(decoded);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        final Channel channel = ctx.channel();
        final String id = NettyUtil.id(channel);

        //Make sure we have a request associated, otherwise do not propagate
        if (hasInvalidRequest(channel)) {
            log.debug("{} INB => No VALID request found (Error: {})", NettyUtil.id(ctx.channel()), cause.getClass().getSimpleName());
            cause.printStackTrace();
            return;
        }

        Attribute<Envelope<AbstractResponse>> responseAttr = channel.attr(ChannelAttributes.RESPONSE);
        //make sure we still have a response instance in case we encounter any errors during read operation
        if (responseAttr.getAndSet(newResponse(channel)) == null) {
            log.debug("{} INB => Exception caught and response envelope is missing. Creating response '{}'", id, responseAttr.get());
        }

        //forward exception to the next handler
        ctx.fireExceptionCaught(cause);
    }

    private boolean hasInvalidRequest(Channel channel) {
        return !channel.hasAttr(ChannelAttributes.REQUEST) || channel.attr(ChannelAttributes.REQUEST).get() == null || channel.attr(ChannelAttributes.REQUEST).get().isCompleted();
    }

    private Envelope<AbstractResponse> newResponse(Channel channel) {
        return newResponse(channel, null);
    }

    private Envelope<AbstractResponse> newResponse(Channel channel, Object msg) {
        assert channel != null;
        assert channel.hasAttr(ChannelAttributes.REQUEST);

        SocketAddress sender, recipient;
        Envelope<AbstractRequest> request = channel.attr(ChannelAttributes.REQUEST).get();
        log.debug("{} INB => Initializing response envelope for request '{}'", NettyUtil.id(channel), request);
        assert request != null;

        if (msg != null) {
            if (channel instanceof DatagramChannel) {
                if (!(msg instanceof AddressedEnvelope))
                    throw new IllegalStateException(msg.getClass().getSimpleName());
                AddressedEnvelope dpMsg = (AddressedEnvelope) msg;
                recipient = dpMsg.recipient();
                sender = dpMsg.sender();
            } else if (channel instanceof SocketChannel) {
                recipient = channel.localAddress();
                sender = channel.remoteAddress();
            } else {
                throw new IllegalStateException("Unsupported channel type");
            }
        } else {
            sender = request.recipient();
            recipient = request.sender();
        }

        assert sender != null;
        assert recipient != null;
        return MessageEnvelopeBuilder.createNew()
                                     .sender(sender)
                                     .recipient(recipient)
                                     .promise(request.promise())
                                     .messenger(request.messenger())
                                     .build();
    }

    private static int getResponseLength(Object msg) {
        if (msg instanceof DatagramPacket) {
            return ((DatagramPacket) msg).content().readableBytes();
        } else if (msg instanceof ByteBuf) {
            return ((ByteBuf) msg).readableBytes();
        } else {
            return -1;
        }
    }
}
