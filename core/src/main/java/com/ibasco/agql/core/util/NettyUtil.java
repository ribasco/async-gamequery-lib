/*
 * Copyright 2022 Asynchronous Game Query Library
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

package com.ibasco.agql.core.util;

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.AbstractResponse;
import com.ibasco.agql.core.Envelope;
import com.ibasco.agql.core.Message;
import com.ibasco.agql.core.transport.NettyChannelAttributes;
import com.ibasco.agql.core.transport.handlers.MessageEncoder;
import com.ibasco.agql.core.transport.handlers.WriteTimeoutHandler;
import com.ibasco.agql.core.transport.pool.NettyChannelPool;
import com.ibasco.agql.core.transport.pool.SimpleNettyChannelPool;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.pool.ChannelPool;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Netty specific utilities
 *
 * @author Rafael Luis Ibasco
 */
@ApiStatus.Internal
public class NettyUtil {

    private static final Logger log = LoggerFactory.getLogger(NettyUtil.class);

    public static final Function<LinkedList<ChannelInboundHandler>, ChannelHandler> INBOUND = LinkedList::pollFirst;

    public static final Function<LinkedList<ChannelOutboundHandler>, ChannelHandler> OUTBOUND = LinkedList::pollLast;

    public static CompletableFuture<Channel> replaceExecutor(Channel channel, EventLoop eventLoop) {
        return NettyUtil.makeCompletable(channel.deregister()).thenCompose(ch -> NettyUtil.makeCompletable(eventLoop.register(ch)));
    }

    public static void dumpBuffer(BiConsumer<String, Object[]> logger, String msg, ByteBuf buf, Integer limit) {
        if (limit == null) {
            limit = buf.readableBytes();
        }
        byte[] tmp = new byte[Math.min(limit, buf.readableBytes())];
        try {
            buf.markReaderIndex();
            buf.readBytes(tmp);
            String postfix = limit != buf.readableBytes() ? "..." : "";
            logger.accept("{} : {}{}", new Object[] {msg, ByteUtil.toHexString(tmp), postfix});
        } finally {
            buf.resetReaderIndex();
        }
    }

    public static void dumpBuffer(BiConsumer<String, Object[]> logger, String msg, ByteBuf buf) {
        dumpBuffer(logger, msg, buf, null);
    }

    public static synchronized void printChannelPipeline(Logger log, Channel ch) {
        if (!log.isDebugEnabled())
            return;
        log.debug("{} ========================================================================================================================================================", id(ch));
        log.debug("{} Initializing handlers for channel '{}' (Pooled: {})", id(ch), ch.id().asShortText(), isPooled(ch) ? "YES" : "NO");
        log.debug("{} ========================================================================================================================================================", id(ch));
        for (Map.Entry<String, ChannelHandler> handlers : ch.pipeline().toMap().entrySet()) {
            log.debug("{} {}: {} = {}", id(ch), getType(handlers.getValue()), handlers.getKey(), handlers.getValue());
        }
        log.debug("{} ========================================================================================================================================================", id(ch));
    }

    public static String getThreadName(Channel channel) {
        EventLoop el = channel.eventLoop();
        if (el instanceof SingleThreadEventLoop) {
            return ((SingleThreadEventLoop) el).threadProperties().name();
        }
        return "N/A";
    }

    public static boolean isPooled(Channel ch) {
        Objects.requireNonNull(ch, "Channel is null");
        return ch.attr(getChannelPoolKey()).get() != null;
    }

    /**
     * Returns the underlying {@link ChannelPool} this channel was created/acquired from.
     *
     * @param ch
     *         The {@link Channel} to check
     *
     * @return The {@link ChannelPool} that this channel was created/acquired from. {@code null} if the channel is not pooled.
     */
    public static NettyChannelPool getChannelPool(Channel ch) {
        return Objects.requireNonNull(ch, "Channel is null").attr(getChannelPoolKey()).get();
    }

    /**
     * Release a {@link Channel} from the pool (if applicable)
     *
     * @param ch
     *         The {@link Channel} to be released
     *
     * @return A {@link CompletableFuture} that will be notified if the {@link Channel} has been successfully released or not
     */
    public static CompletableFuture<NettyChannelPool> release(Channel ch) {
        if (ch.eventLoop().isShutdown())
            return ConcurrentUtil.failedFuture(new RejectedExecutionException("Executor has shutdown"));
        if (!isPooled(ch))
            return CompletableFuture.completedFuture(null);
        final NettyChannelPool pool = getChannelPool(ch);
        return pool.release(ch).handle((unused, error) -> {
            if (error != null)
                throw new CompletionException("Failed to release channel from pool: " + pool, error);
            return pool;
        });
    }

    public static String id(ChannelHandlerContext ctx) {
        return id(ctx.channel());
    }

    /**
     * Translate {@link Channel} to it's unique id string representation
     *
     * @param ch
     *         The {@link Channel} to translate
     *
     * @return A unique id string representation of the {@link Channel}
     */
    public static String id(Channel ch) {
        if (ch == null)
            return "[N/A]";
        if (ch.hasAttr(NettyChannelAttributes.REQUEST) && ch.attr(NettyChannelAttributes.REQUEST).get() != null) {
            Envelope<AbstractRequest> request = ch.attr(NettyChannelAttributes.REQUEST).get();
            if (request.content() != null) {
                return "[" + ch.id().asShortText() + " : " + request.content().id() + "]";//String.format("[%s : %s]", ch.id().asShortText(), request.content().id());
            }
        }
        return "[" + ch.id().asShortText() + "]";
        //return String.format("[%s]", ch.id().asShortText());
    }

    /**
     * Translates an {@link Envelope} instance to it's unique id string representation
     *
     * @param envelope
     *         The {@link Envelope} to translate
     *
     * @return A unique id string representation of the {@link Envelope}
     */
    public static String id(Envelope<?> envelope) {
        if (envelope == null)
            return "[N/A]";

        return id(envelope.content());
    }

    /**
     * Translates a message instance to it's unique id string representation
     *
     * @param message
     *         The message content to translate
     *
     * @return A unique id string representation of the {@link Envelope}
     */
    public static String id(Object message) {
        if (message == null)
            return "[N/A]";
        String prefix;
        if (message instanceof AbstractRequest)
            prefix = "REQ";
        else if (message instanceof AbstractResponse)
            prefix = "RES";
        else if (message instanceof Channel)
            return id((Channel) message);
        else
            prefix = "MSG";
        return String.format("[%s:%s]", prefix, message instanceof Message ? ((Message) message).id() : message);
    }

    // 1) Populate handlers using the initializer
    // 2) extract the results from the deque
    // 3) transfer the extracted handlers from the deque to the channel pipeline in the order depicted by the extractor
    public static <H extends ChannelHandler> void registerHandlers(ChannelPipeline pipeline, Consumer<LinkedList<H>> initializer, Function<LinkedList<H>, ChannelHandler> extractStrategy) {
        LinkedList<H> handlers = new LinkedList<>();
        initializer.accept(handlers);
        ChannelHandler handler;
        while ((handler = extractStrategy.apply(handlers)) != null) {
            pipeline.addLast(handler);
        }
    }

    public static void registerTimeoutHandlers(Channel ch) {
        //assert ch.hasAttr(ChannelAttributes.READ_TIMEOUT);
        assert ch.hasAttr(NettyChannelAttributes.WRITE_TIMEOUT);

        //read default channel attributes
        //int readTimeout = ch.attr(ChannelAttributes.READ_TIMEOUT).get();
        int writeTimeout = ch.attr(NettyChannelAttributes.WRITE_TIMEOUT).get();

        try {
            //ensure they are not existing within the current pipeline
            //ch.pipeline().remove(ReadTimeoutHandler.class);
            ch.pipeline().remove(WriteTimeoutHandler.class);
        } catch (NoSuchElementException ignored) {
        }

        //ch.pipeline().addBefore("responseDecoder", "readTimeout", new ReadTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS));
        ch.pipeline().addAfter(MessageEncoder.NAME, "writeTimeout", new WriteTimeoutHandler(writeTimeout, TimeUnit.MILLISECONDS));

        log.debug("{} TRANSPORT => Registered READ/WRITE Timeout Handlers", NettyUtil.id(ch));
    }

    public static String getType(ChannelHandler handler) {
        if (handler instanceof ChannelDuplexHandler) {
            return "BOTH    ";
        } else if (handler instanceof ChannelInboundHandler) {
            return "INBOUND ";
        } else if (handler instanceof ChannelOutboundHandler) {
            return "OUTBOUND";
        } else {
            return handler.getClass().getSimpleName();
        }
    }

    public static void clearAttribute(Channel ch, AttributeKey<?> key) {
        if (ch.hasAttr(key)) {
            Attribute<?> attr = ch.attr(key);
            if (attr.get() != null) {
                attr.set(null);
                log.debug("{} RESET => Cleared channel attribute '{}' (cleared: {})", NettyUtil.id(ch), key.name(), ch.attr(key).get() == null);
            }
        }
    }

    public static <V> CompletableFuture<V> makeCompletable(Future<V> future) {
        CompletableFuture<V> cFuture = new CompletableFuture<>();
        if (future.isDone()) {
            if (future.isSuccess()) {
                cFuture.complete(future.getNow());
            } else {
                cFuture.completeExceptionally(future.cause());
            }
        } else {
            future.addListener(future1 -> {
                if (future1.isSuccess()) {
                    cFuture.complete(future.getNow());
                } else {
                    cFuture.completeExceptionally(future1.cause());
                }
            });
        }
        return cFuture;
    }

    public static CompletableFuture<Channel> makeCompletable(ChannelFuture future) {
        return makeCompletable(future, future::channel, future::cause);
    }

    public static <A, B, C extends Future<B>> CompletableFuture<A> makeCompletable(C future, Supplier<A> success, Supplier<Throwable> fail) {
        CompletableFuture<A> cFuture = new CompletableFuture<>();
        if (future.isDone()) {
            if (future.isSuccess()) {
                cFuture.complete(success.get());
            } else {
                cFuture.completeExceptionally(fail.get());
            }
        } else {
            future.addListener(fut -> {
                if (fut.isSuccess()) {
                    cFuture.complete(success.get());
                } else {
                    cFuture.completeExceptionally(fail.get());
                }
            });
        }
        return cFuture;
    }

    public static CompletableFuture<Channel> makeCompletable(Future<Channel> future, int timeout, TimeUnit timeUnit, Consumer<Channel> initialize) {
        final CompletableFuture<Channel> cFuture = new CompletableFuture<>();
        future.addListener((Future<Channel> pFuture) -> {
            try {
                Channel channel = pFuture.get(timeout, timeUnit);
                if (initialize != null)
                    initialize.accept(channel);
                cFuture.complete(channel);
            } catch (Throwable e) {
                cFuture.completeExceptionally(e);
            }
        });
        return cFuture;
    }

    public static String readString(ByteBuf buffer) {
        return readString(buffer, StandardCharsets.UTF_8);
    }

    public static String readString(ByteBuf buffer, Charset charset) {
        int readable = buffer.readableBytes();
        if (readable <= 0)
            return null;
        if (readable > 1)
            buffer.skipBytes(1);
        int length = buffer.bytesBefore((byte) 0);
        if (length < 0)
            return null;
        if (charset == null)
            charset = StandardCharsets.UTF_8;
        return buffer.readCharSequence(length, charset).toString();
    }

    /**
     * Release or close the {@link Channel} returned by the future.
     *
     * @param channelFuture
     *         The future whose {@link Channel} will be closed once it transitions to a completed state.
     */
    public static void releaseOrClose(CompletableFuture<Channel> channelFuture) {
        if (channelFuture.isDone()) {
            releaseOrClose(channelFuture.getNow(null));
        } else {
            channelFuture.thenAccept(NettyUtil::releaseOrClose);
        }
    }

    public static CompletableFuture<Void> close(Channel channel) {
        CompletableFuture<Void> cf = new CompletableFuture<>();
        ChannelFuture future = channel.close();
        if (future.isDone()) {
            if (future.isSuccess()) {
                cf.complete(null);
            } else {
                cf.completeExceptionally(future.cause());
            }
        } else {
            future.addListener((ChannelFutureListener) future1 -> {
                if (future1.isSuccess()) {
                    cf.complete(null);
                } else {
                    cf.completeExceptionally(future1.cause());
                }
            });
        }
        return cf;
    }

    /**
     * Attempt to release (if acquired from a {@link ChannelPool}) or close a channel
     */
    public static void releaseOrClose(Channel channel) {
        if (channel == null) {
            log.debug("[N/A] ROUTER (RELEASE) => Channel has been disposed. Skipping operation");
            return;
        }
        //is the channel pooled?
        if (!isPooled(channel)) {
            log.debug("{} ROUTER (RELEASE) => This channel does not have a channel pool attached. Closing.", id(channel));
            channel.close();
            return;
        }
        final String id = id(channel);
        final NettyChannelPool pool = getChannelPool(channel);
        log.debug("{} ROUTER (RELEASE) => Releasing channel '{}' from pool '{}#{}'", id, channel, pool.getClass().getName(), pool.hashCode());
        release(channel).whenComplete((pool1, error) -> {
            if (error != null) {
                log.error(String.format("%s ROUTER (RELEASE) => Failed to release channel from pool '%s'. Requesting to close context.", id, pool1), error);
                channel.close();
            }
        });
    }

    public static <V> void runWhenComplate(ChannelFuture future, Consumer<ChannelFuture> consumer) {
        if (future.isDone()) {
            consumer.accept(future);
        } else {
            future.addListener((ChannelFutureListener) consumer::accept);
        }
    }

    private static AttributeKey<NettyChannelPool> getChannelPoolKey() {
        return SimpleNettyChannelPool.getChannelPoolKey();
    }
}
