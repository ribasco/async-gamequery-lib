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

package com.ibasco.agql.core;

import com.ibasco.agql.core.exceptions.ChannelClosedException;
import com.ibasco.agql.core.exceptions.TransportException;
import com.ibasco.agql.core.exceptions.WriteInProgressException;
import com.ibasco.agql.core.util.Errors;
import com.ibasco.agql.core.util.GeneralOptions;
import com.ibasco.agql.core.util.Netty;
import com.ibasco.agql.core.util.Options;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.ResourceLeakDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * The netty transport driver, responsible for sending the messages to the provided {@link io.netty.channel.Channel}.
 *
 * @author Rafael Luis Ibasco
 */
public class NettyTransport implements Transport<NettyChannelContext, NettyChannelContext> {

    private static final Logger log = LoggerFactory.getLogger(NettyTransport.class);

    //<editor-fold desc="Private Members">
    private final Options options;
    //</editor-fold>

    private static final ChannelFutureListener COMPLETE_ON_WRITE = future -> {
        final Channel channel = future.channel();
        final NettyChannelContext context = NettyChannelContext.getContext(channel);
        try {
            assert context.channel().id().equals(channel.id());
            if (future.isSuccess()) {
                log.debug("{} TRANSPORT => Request has been sent and processed through the channel's pipeline", context.id());
                context.properties().endWrite();
            } else {
                log.debug("{} TRANSPORT => An error occured while sending request through the channel's pipeline", context.id(), future.cause());
                context.properties().endWrite(future.cause());
            }
            assert !context.properties().writeInProgress();
        } catch (Exception e) {
            log.error("{} TRANSPORT => Error occured during write operation", Netty.id(channel), e);
            if (context.properties().writeInProgress())
                context.properties().endWrite(e);
        }
    };

    //<editor-fold desc="Default Constructor">
    /**
     * <p>Constructor for NettyTransport.</p>
     *
     * @param options a {@link com.ibasco.agql.core.util.Options} object
     */
    public NettyTransport(final Options options) {
        this.options = Objects.requireNonNull(options, "[INIT] TRANSPORT => Missing options");
        //Set resource leak detection if debugging is enabled
        if (log.isErrorEnabled()) {
            ResourceLeakDetector.Level level = options.getOrDefault(GeneralOptions.RESOURCE_LEAK_DETECTOR_LEVEL);
            ResourceLeakDetector.setLevel(level);
            log.debug("[INIT] TRANSPORT => Set ResourceLeakDetector level to '{}'", level.name());
        }
    }
    //</editor-fold>

    /** {@inheritDoc} */
    @Override
    public final CompletableFuture<NettyChannelContext> send(final NettyChannelContext context) {
        checkContext(context);
        final CompletableFuture<NettyChannelContext> contextFuture = CompletableFuture.completedFuture(context);
        //ensure that we execute within the channel's event loop
        if (context.inEventLoop()) {
            return contextFuture
                    .thenCompose(this::writeAndNotify)
                    .handle(this::finalize);
        } else {
            return contextFuture
                    .thenComposeAsync(this::writeAndNotify, context.eventLoop())
                    .handleAsync(this::finalize, context.eventLoop());
        }
    }

    private CompletableFuture<NettyChannelContext> writeAndNotify(final NettyChannelContext context) {
        final Channel channel = context.channel();
        Objects.requireNonNull(channel, "Channel is null");
        assert context.inEventLoop();

        if (!context.isValid())
            throw new IllegalStateException("Context is no longer in a valid state", new ChannelClosedException(channel));

        if (context.properties().writeInProgress())
            throw new WriteInProgressException(String.format("A write is currently in-progress for context '%s'", context));

        //make sure we are in a valid state
        final CompletableFuture<NettyChannelContext> writePromise = context.properties().beginWrite();

        assert writePromise != null;
        assert !writePromise.isDone();

        try {
            final Envelope<AbstractRequest> envelope = context.properties().envelope();
            if (envelope == null)
                throw new IllegalStateException("Request envelope is not present in channel");
            log.debug("{} TRANSPORT => Sending request '{}' to transport", context.id(), envelope.content());
            //write to the channel pipeline
            ChannelFuture writeFuture = channel.writeAndFlush(envelope);
            if (writeFuture.isDone()) {
                COMPLETE_ON_WRITE.operationComplete(writeFuture);
            } else {
                writeFuture.addListener(COMPLETE_ON_WRITE);
            }
        } catch (Exception error) {
            log.debug("{} TRANSPORT => Error occured during writeAndNotify operation", context.id(), error);
            context.properties().endWrite(error);
        }
        return writePromise;
    }

    private NettyChannelContext finalize(NettyChannelContext context, Throwable error) {
        if (error != null) {
            log.debug("TRANSPORT => Error during write operation", error);
            throw new TransportException("Failed to send request via transport", Errors.unwrap(error));
        }
        if (context.channel() == null)
            throw new IllegalStateException("Channel is null");
        assert context.inEventLoop();
        return context;
    }

    private static void checkContext(NettyChannelContext context) {
        if (context == null)
            throw new IllegalStateException("Channel context must not be null");
        if (context.properties().envelope() == null)
            throw new IllegalStateException("No valid request attached to channel context: " + context);
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {

    }

    /** {@inheritDoc} */
    @Override
    public final Options getOptions() {
        return options;
    }
}
