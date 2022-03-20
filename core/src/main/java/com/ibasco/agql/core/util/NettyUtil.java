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

package com.ibasco.agql.core.util;

import com.ibasco.agql.core.*;
import com.ibasco.agql.core.exceptions.NoChannelContextException;
import com.ibasco.agql.core.transport.handlers.MessageEncoder;
import com.ibasco.agql.core.transport.handlers.WriteTimeoutHandler;
import com.ibasco.agql.core.transport.pool.NettyChannelPool;
import com.ibasco.agql.core.transport.pool.PooledChannel;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
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

    public static CompletableFuture<Channel> useEventLoop(CompletableFuture<Channel> channelFuture, EventLoop eventLoop) {
        if (eventLoop == null)
            throw new IllegalArgumentException("Event loop group is null");
        return channelFuture.thenApplyAsync(ChannelOutboundInvoker::deregister, eventLoop)
                            .thenComposeAsync(NettyUtil::toCompletable, eventLoop)
                            .thenApplyAsync(eventLoop::register, eventLoop)
                            .thenComposeAsync(NettyUtil::toCompletable, eventLoop);
    }

    public static <V> void notifyOnCompletion(ChannelFuture future, V completedValue, CompletableFuture<V> promise, ChannelFutureListener listener) {
        if (future.isDone()) {
            if (future.isSuccess()) {
                promise.complete(completedValue);
            } else {
                promise.completeExceptionally(future.cause());
            }
        } else {
            future.addListener(listener);
        }
    }

    public static CompletableFuture<Channel> register(Channel channel, EventLoop group) {
        return toCompletable(group.register(channel));
    }

    public static CompletableFuture<Channel> deregister(Channel channel) {
        return toCompletable(channel.deregister());
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

    public static synchronized void printChannelPipeline(Logger log, Channel ch) {
        if (!log.isDebugEnabled())
            return;
        log.debug("{} ========================================================================================================================================================", id(ch));
        log.debug("{} Initializing handlers for channel '{}' (Pooled: {})", id(ch), ch.id().asShortText(), NettyChannelPool.isPooled(ch) ? "YES" : "NO");
        log.debug("{} ========================================================================================================================================================", id(ch));
        for (Map.Entry<String, ChannelHandler> handlers : ch.pipeline().toMap().entrySet()) {
            log.debug("{} {}: {} = {}", id(ch), getType(handlers.getValue()), handlers.getKey(), handlers.getValue());
        }
        log.debug("{} ========================================================================================================================================================", id(ch));
    }

    public static String getThreadName(Channel channel) {
        return getThreadName(channel.eventLoop());
    }

    public static String getThreadName(EventLoop eventLoop) {
        if (eventLoop instanceof SingleThreadEventLoop) {
            return ((SingleThreadEventLoop) eventLoop).threadProperties().name();
        }
        return "N/A";
    }

    /**
     * Release a {@link Channel} from the pool (if applicable)
     *
     * @param ch
     *         The {@link Channel} to be released
     *
     * @return A {@link CompletableFuture} that will be notified if the {@link Channel} has been successfully released or not
     */
    public static CompletableFuture<Void> release(Channel ch) {
        if (ch.eventLoop().isShutdown())
            return ConcurrentUtil.failedFuture(new RejectedExecutionException("Executor has shutdown"));
        if (ch instanceof PooledChannel) {
            return ((PooledChannel) ch).release();
        }
        final NettyChannelPool pool = NettyChannelPool.getPool(ch);
        if (pool == null)
            return CompletableFuture.completedFuture(null);
        return pool.release(ch);
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
        try {
            NettyChannelContext context = NettyChannelContext.getContext(ch);
            if (context.properties().envelope() != null) {
                Envelope<AbstractRequest> request = context.properties().envelope();
                if (request.content() != null) {
                    return "[" + ch.id().asShortText() + " : " + request.content().id() + "]";//String.format("[%s : %s]", ch.id().asShortText(), request.content().id());
                }
            }
        } catch (NoChannelContextException ignored) {
        }
        return "[" + ch.id().asShortText() + "]";
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
        Integer writeTimeout = TransportOptions.WRITE_TIMEOUT.attr(ch);
        assert writeTimeout != null;
        //read default channel attributes
        try {
            //ensure they are not existing within the current pipeline
            ch.pipeline().remove(WriteTimeoutHandler.class);
        } catch (NoSuchElementException ignored) {
        }
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
        if (!ch.hasAttr(key))
            return;
        Attribute<?> attr = ch.attr(key);
        if (attr.get() != null) {
            attr.set(null);
            log.debug("{} RESET => Cleared channel attribute '{}' (cleared: {})", NettyUtil.id(ch), key.name(), ch.attr(key).get() == null);
        }
    }

    public static <V> CompletableFuture<V> toCompletable(Future<V> future) {
        if (future.isDone()) {
            if (future.isSuccess()) {
                return CompletableFuture.completedFuture(future.getNow());
            } else {
                return ConcurrentUtil.failedFuture(future.cause());
            }
        } else {
            CompletableFuture<V> cFuture = new CompletableFuture<>();
            future.addListener(fut -> {
                if (fut.isSuccess()) {
                    //noinspection unchecked
                    cFuture.complete((V) fut.getNow());
                } else {
                    cFuture.completeExceptionally(fut.cause());
                }
            });
            return cFuture;
        }
    }

    /**
     * Converts a netty {@link ChannelFuture} to a {@link CompletableFuture}
     *
     * @param channelFuture
     *         The {@link ChannelFuture} to convert
     *
     * @return The converted {@link CompletableFuture}
     */
    public static CompletableFuture<Channel> toCompletable(ChannelFuture channelFuture) {
        if (channelFuture.isDone()) {
            if (channelFuture.isSuccess()) {
                assert channelFuture.channel() != null;
                return CompletableFuture.supplyAsync(channelFuture::channel, channelFuture.channel().eventLoop());
            } else {
                return ConcurrentUtil.failedFuture(channelFuture.cause(), channelFuture.channel() == null ? null : channelFuture.channel().eventLoop());
            }
        } else {
            CompletableFuture<Channel> cFuture = new CompletableFuture<>();
            channelFuture.addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    cFuture.complete(future.channel());
                } else {
                    cFuture.completeExceptionally(future.cause());
                }
            });
            return cFuture;
        }
    }

    public static <A, B, C extends Future<B>> CompletableFuture<A> toCompletable(C future, Supplier<A> success, Supplier<Throwable> fail) {
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
     * Attempt to release (if acquired from a {@link NettyChannelPool}) or close a channel
     */
    public static void releaseOrClose(Channel channel) {
        if (channel == null) {
            log.debug("[N/A] ROUTER (RELEASE) => Channel is null. Skipping operation");
            return;
        }
        final NettyChannelPool pool = NettyChannelPool.getPool(channel);
        //is the channel pooled?
        if (pool == null) {
            log.debug("{} ROUTER (RELEASE) => This channel does not have a channel pool attached. Closing.", id(channel));
            channel.close();
            return;
        }
        final String id = id(channel);
        log.debug("{} ROUTER (RELEASE) => Releasing channel '{}' from pool '{}#{}'", id, channel, pool.getClass().getName(), pool.hashCode());
        release(channel).whenComplete((unused, error) -> {
            if (error != null) {
                log.error(String.format("%s ROUTER (RELEASE) => Failed to release channel from pool '%s'. Requesting to close channel.", id, unused), error);
                channel.close();
            }
        });
    }

    public static <V extends Number> Number incrementAttrNumber(Channel channel, AttributeKey<V> stat) {
        Attribute<V> attr = channel.attr(stat);
        Number oldValue = attr.get();
        Number newValue;
        if (oldValue == null) {
            newValue = 1;
            //noinspection unchecked
            attr.set((V) newValue);
        } else {
            if (oldValue instanceof Integer) {
                newValue = attr.get().intValue() + 1;
            } else if (oldValue instanceof Long) {
                newValue = attr.get().longValue() + 1;
            } else if (oldValue instanceof Double) {
                newValue = attr.get().doubleValue() + 1;
            } else if (oldValue instanceof Float) {
                newValue = attr.get().floatValue() + 1;
            } else if (oldValue instanceof Byte) {
                newValue = attr.get().byteValue() + 1;
            } else if (oldValue instanceof Short) {
                newValue = attr.get().shortValue() + 1;
            } else {
                throw new IllegalStateException("Unsupported number type: " + oldValue.getClass());
            }
            //noinspection unchecked
            attr.set((V) newValue);
        }
        return newValue;
    }

    public static <V> CompletableFuture<V> supplyAsync(Supplier<V> supplier, EventLoop eventLoop) {
        if (eventLoop.inEventLoop()) {
            return CompletableFuture.completedFuture(supplier.get());
        } else {
            return CompletableFuture.supplyAsync(supplier, eventLoop);
        }
    }

    public static <V> CompletableFuture<V> applyAsync(Channel channel, Function<Channel, V> func) {
        if (channel.eventLoop().inEventLoop())
            return CompletableFuture.completedFuture(channel).thenApply(func);
        return applyAsync(CompletableFuture.completedFuture(channel), func);
    }

    public static <V> CompletableFuture<V> applyAsync(CompletableFuture<Channel> channelFuture, Function<Channel, V> func) {
        //return channelFuture.thenCombine(CompletableFuture.completedFuture(func), Pair::new).thenCompose(pair -> CompletableFuture.completedFuture(pair.getFirst()).thenApplyAsync(pair.getSecond(), pair.getFirst().eventLoop()));
        return channelFuture.thenCompose(channel -> CompletableFuture.completedFuture(channel).thenApplyAsync(func, channel.eventLoop()));
    }

    public static <V> CompletableFuture<V> composeAsync(CompletableFuture<Channel> channelFuture, Function<Channel, CompletionStage<V>> func) {
        return channelFuture.thenCompose(channel -> CompletableFuture.completedFuture(channel).thenComposeAsync(func, channel.eventLoop()));
    }
}


