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
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
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
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Netty utility functions
 *
 * @author Rafael Luis Ibasco
 */
@ApiStatus.Internal
public class Netty {

    private static final Logger log = LoggerFactory.getLogger(Netty.class);

    /** Constant <code>INBOUND</code> */
    public static final Function<LinkedList<ChannelInboundHandler>, ChannelHandler> INBOUND = LinkedList::pollFirst;

    /** Constant <code>OUTBOUND</code> */
    public static final Function<LinkedList<ChannelOutboundHandler>, ChannelHandler> OUTBOUND = LinkedList::pollLast;

    /**
     * <p>useEventLoop.</p>
     *
     * @param channelFuture
     *         a {@link java.util.concurrent.CompletableFuture} object
     * @param eventLoop
     *         a {@link io.netty.channel.EventLoop} object
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public static CompletableFuture<Channel> useEventLoop(CompletableFuture<Channel> channelFuture, EventLoop eventLoop) {
        if (eventLoop == null)
            throw new IllegalArgumentException("Event loop group is null");
        return channelFuture.thenApplyAsync(ChannelOutboundInvoker::deregister, eventLoop)
                            .thenComposeAsync(Netty::toCompletable, eventLoop)
                            .thenApplyAsync(eventLoop::register, eventLoop)
                            .thenComposeAsync(Netty::toCompletable, eventLoop);
    }

    /**
     * <p>notifyOnCompletion.</p>
     *
     * @param future
     *         a {@link io.netty.channel.ChannelFuture} object
     * @param completedValue
     *         a V object
     * @param promise
     *         a {@link java.util.concurrent.CompletableFuture} object
     * @param listener
     *         a {@link io.netty.channel.ChannelFutureListener} object
     * @param <V>
     *         a V class
     */
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

    /**
     * <p>register.</p>
     *
     * @param channel
     *         a {@link io.netty.channel.Channel} object
     * @param group
     *         a {@link io.netty.channel.EventLoop} object
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public static CompletableFuture<Channel> register(Channel channel, EventLoop group) {
        return toCompletable(group.register(channel));
    }

    /**
     * <p>deregister.</p>
     *
     * @param channel
     *         a {@link io.netty.channel.Channel} object
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public static CompletableFuture<Channel> deregister(Channel channel) {
        return toCompletable(channel.deregister());
    }

    /**
     * <p>dumpBuffer.</p>
     *
     * @param logger
     *         a {@link java.util.function.BiConsumer} object
     * @param msg
     *         a {@link java.lang.String} object
     * @param buf
     *         a {@link io.netty.buffer.ByteBuf} object
     * @param limit
     *         a {@link java.lang.Integer} object
     */
    public static void dumpBuffer(BiConsumer<String, Object[]> logger, String msg, ByteBuf buf, Integer limit) {
        if (buf == null) {
            logger.accept("{} = NULL", new Object[] {msg});
            return;
        }
        if (limit == null) {
            limit = buf.readableBytes();
        }
        byte[] tmp = new byte[Math.min(limit, buf.readableBytes())];
        try {
            buf.markReaderIndex();
            buf.readBytes(tmp);
            String postfix = limit != buf.readableBytes() ? "..." : "";
            logger.accept("{} : {}{}", new Object[] {msg, Bytes.toHexString(tmp), postfix});
        } finally {
            buf.resetReaderIndex();
        }
    }

    /**
     * <p>prettyHexDump.</p>
     *
     * @param buf
     *         an array of {@link byte} objects
     *
     * @return a {@link java.lang.String} object
     */
    public static String prettyHexDump(byte[] buf) {
        return prettyHexDump(Unpooled.copiedBuffer(buf));
    }

    /**
     * <p>prettyHexDump.</p>
     *
     * @param buf
     *         a {@link io.netty.buffer.ByteBuf} object
     *
     * @return a {@link java.lang.String} object
     */
    public static String prettyHexDump(ByteBuf buf) {
        return prettyHexDump(buf, true);
    }

    /**
     * <p>prettyHexDump.</p>
     *
     * @param buf
     *         a {@link io.netty.buffer.ByteBuf} object
     * @param dumpAll
     *         a boolean
     *
     * @return a {@link java.lang.String} object
     */
    public static String prettyHexDump(ByteBuf buf, boolean dumpAll) {
        int origIndex = -1;
        try {
            origIndex = buf.readerIndex();
            if (dumpAll)
                buf.readerIndex(0);
            return ByteBufUtil.prettyHexDump(buf);
        } finally {
            if (origIndex >= 0)
                buf.readerIndex(origIndex);
        }
    }

    /**
     * Return a byte array contents of a {@link io.netty.buffer.ByteBuf}
     *
     * @param buf
     *         The {@link io.netty.buffer.ByteBuf} to process
     *
     * @return A byte array containing the contents of the buffer
     */
    public static byte[] getBufferContents(ByteBuf buf) {
        return getBufferContents(buf, null);
    }

    /**
     * Return a byte array contents of a {@link io.netty.buffer.ByteBuf}
     *
     * @param buf
     *         The {@link io.netty.buffer.ByteBuf} to process
     * @param limit
     *         Limit the number of bytes to read or {@code null} to read the entire buffer
     *
     * @return A byte array containing the contents of the buffer
     */
    public static byte[] getBufferContents(ByteBuf buf, Integer limit) {
        if (limit == null)
            limit = buf.readableBytes();
        int bufferSize = Math.min(limit, buf.readableBytes());
        byte[] tmp = new byte[bufferSize];
        try {
            buf.markReaderIndex();
            buf.readBytes(tmp);
        } finally {
            buf.resetReaderIndex();
        }
        return tmp;
    }

    /**
     * <p>getBufferContentsAll.</p>
     *
     * @param buf
     *         a {@link io.netty.buffer.ByteBuf} object
     *
     * @return an array of {@link byte} objects
     */
    public static byte[] getBufferContentsAll(ByteBuf buf) {
        int origReaderIndex = buf.readerIndex();
        buf.readerIndex(0);
        int bufferSize = buf.readableBytes();
        byte[] tmp = new byte[bufferSize];
        try {
            buf.markReaderIndex();
            buf.readBytes(tmp);
        } finally {
            buf.readerIndex(origReaderIndex);
        }
        return tmp;
    }

    /**
     * <p>printChannelPipeline.</p>
     *
     * @param log
     *         a {@link org.slf4j.Logger} object
     * @param ch
     *         a {@link io.netty.channel.Channel} object
     */
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

    /**
     * <p>getThreadName.</p>
     *
     * @param channel
     *         a {@link io.netty.channel.Channel} object
     *
     * @return a {@link java.lang.String} object
     */
    public static String getThreadName(Channel channel) {
        return getThreadName(channel.eventLoop());
    }

    /**
     * <p>getThreadName.</p>
     *
     * @param eventLoop
     *         a {@link io.netty.channel.EventLoop} object
     *
     * @return a {@link java.lang.String} object
     */
    public static String getThreadName(EventLoop eventLoop) {
        if (eventLoop instanceof SingleThreadEventLoop) {
            return ((SingleThreadEventLoop) eventLoop).threadProperties().name();
        }
        return "N/A";
    }

    /**
     * Release a {@link io.netty.channel.Channel} from the pool (if applicable)
     *
     * @param ch
     *         The {@link io.netty.channel.Channel} to be released
     *
     * @return A {@link java.util.concurrent.CompletableFuture} that will be notified if the {@link io.netty.channel.Channel} has been successfully released or not
     */
    public static CompletableFuture<Void> release(Channel ch) {
        if (ch.eventLoop().isShutdown())
            return Concurrency.failedFuture(new RejectedExecutionException("Executor has shutdown"));
        if (ch instanceof PooledChannel) {
            return ((PooledChannel) ch).release();
        }
        final NettyChannelPool pool = NettyChannelPool.getPool(ch);
        if (pool == null)
            return CompletableFuture.completedFuture(null);
        return pool.release(ch);
    }

    /**
     * <p>id.</p>
     *
     * @param ctx
     *         a {@link io.netty.channel.ChannelHandlerContext} object
     *
     * @return a {@link java.lang.String} object
     */
    public static String id(ChannelHandlerContext ctx) {
        return id(ctx.channel());
    }

    /**
     * Translate {@link io.netty.channel.Channel} to it's unique id string representation
     *
     * @param ch
     *         The {@link io.netty.channel.Channel} to translate
     *
     * @return A unique id string representation of the {@link io.netty.channel.Channel}
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
     * Translates an {@link com.ibasco.agql.core.Envelope} instance to it's unique id string representation
     *
     * @param envelope
     *         The {@link com.ibasco.agql.core.Envelope} to translate
     *
     * @return A unique id string representation of the {@link com.ibasco.agql.core.Envelope}
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
     * @return A unique id string representation of the {@link com.ibasco.agql.core.Envelope}
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

    /**
     * <p>registerHandlers.</p>
     *
     * @param pipeline
     *         a {@link io.netty.channel.ChannelPipeline} object
     * @param initializer
     *         a {@link java.util.function.Consumer} object
     * @param extractStrategy
     *         a {@link java.util.function.Function} object
     * @param <H>
     *         a H class
     */
    public static <H extends ChannelHandler> void registerHandlers(ChannelPipeline pipeline, Consumer<LinkedList<H>> initializer, Function<LinkedList<H>, ChannelHandler> extractStrategy) {
        LinkedList<H> handlers = new LinkedList<>();
        initializer.accept(handlers);
        ChannelHandler handler;
        while ((handler = extractStrategy.apply(handlers)) != null) {
            pipeline.addLast(handler);
        }
    }

    /**
     * <p>registerTimeoutHandlers.</p>
     *
     * @param ch
     *         a {@link io.netty.channel.Channel} object
     */
    public static void registerTimeoutHandlers(Channel ch) {
        Integer writeTimeout = GeneralOptions.WRITE_TIMEOUT.attr(ch);
        assert writeTimeout != null;
        //read default channel attributes
        try {
            //ensure they are not existing within the current pipeline
            ch.pipeline().remove(WriteTimeoutHandler.class);
        } catch (NoSuchElementException ignored) {
        }
        ch.pipeline().addAfter(MessageEncoder.NAME, "writeTimeout", new WriteTimeoutHandler(writeTimeout, TimeUnit.MILLISECONDS));
        log.debug("{} TRANSPORT => Registered READ/WRITE Timeout Handlers", Netty.id(ch));
    }

    /**
     * <p>Returns the type name of a {@link ChannelHandler}</p>
     *
     * @param handler
     *         a {@link io.netty.channel.ChannelHandler} object
     *
     * @return a {@link java.lang.String} object
     */
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

    /**
     * <p>Clears the value of a netty {@link Attribute}</p>
     *
     * @param ch
     *         a {@link io.netty.channel.Channel} object
     * @param key
     *         a {@link io.netty.util.AttributeKey} object
     */
    public static void clearAttribute(Channel ch, AttributeKey<?> key) {
        if (!ch.hasAttr(key))
            return;
        Attribute<?> attr = ch.attr(key);
        if (attr.get() != null) {
            attr.set(null);
            log.debug("{} RESET => Cleared channel attribute '{}' (cleared: {})", Netty.id(ch), key.name(), ch.attr(key).get() == null);
        }
    }

    /**
     * <p>Converts a netty based {@link Future} to a {@link CompletableFuture}.</p>
     *
     * @param future
     *         a {@link io.netty.util.concurrent.Future} object
     * @param <V>
     *         a V class
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public static <V> CompletableFuture<V> toCompletable(Future<V> future) {
        if (future.isDone()) {
            if (future.isSuccess()) {
                return CompletableFuture.completedFuture(future.getNow());
            } else {
                return Concurrency.failedFuture(future.cause());
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
     * Converts a netty {@link io.netty.channel.ChannelFuture} to a {@link java.util.concurrent.CompletableFuture}
     *
     * @param channelFuture
     *         The {@link io.netty.channel.ChannelFuture} to convert
     *
     * @return The converted {@link java.util.concurrent.CompletableFuture}
     */
    public static CompletableFuture<Channel> toCompletable(ChannelFuture channelFuture) {
        if (channelFuture.isDone()) {
            if (channelFuture.isSuccess()) {
                assert channelFuture.channel() != null;
                return CompletableFuture.supplyAsync(channelFuture::channel, channelFuture.channel().eventLoop());
            } else {
                return Concurrency.failedFuture(channelFuture.cause(), channelFuture.channel() == null ? null : channelFuture.channel().eventLoop());
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

    /**
     * <p>Converts a netty based {@link Future} to a {@link CompletableFuture}.</p>
     *
     * @param future
     *         a C object
     * @param success
     *         a {@link java.util.function.Supplier} object
     * @param fail
     *         a {@link java.util.function.Supplier} object
     * @param <A>
     *         a A class
     * @param <B>
     *         a B class
     * @param <C>
     *         a C class
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
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

    /**
     * <p>Reads a null-terminated string from the provided {@link ByteBuf}</p>
     *
     * @param buffer
     *         a {@link io.netty.buffer.ByteBuf} object
     *
     * @return a {@link java.lang.String} object
     */
    public static String readString(ByteBuf buffer) {
        return readString(buffer, StandardCharsets.UTF_8);
    }

    /**
     * <p>Reads a null-terminated string from the provided {@link ByteBuf}</p>
     *
     * @param buffer
     *         a {@link io.netty.buffer.ByteBuf} object
     * @param charset
     *         a {@link java.nio.charset.Charset} object
     *
     * @return a {@link java.lang.String} object
     */
    public static String readString(ByteBuf buffer, Charset charset) {
        int readable = buffer.readableBytes();
        if (readable <= 0)
            return null;
        int length = buffer.bytesBefore((byte) 0);
        if (length < 0)
            return null;
        if (charset == null)
            charset = StandardCharsets.UTF_8;

        String data = buffer.readCharSequence(length, charset).toString();
        //advance the reader index by 1 so the next read operation
        //will not start on the previous null-byte, which might be interpreted as null/empty.
        if (buffer.isReadable())
            buffer.skipBytes(1); //skip null-terminating byte
        return data;
    }

    /**
     * <p>Closes a {@link Channel}.</p>
     *
     * @param channel
     *         a {@link io.netty.channel.Channel} object
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
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
     * <p>Increments an {@link Attribute} value whose underlying type is a {@link Number}</p>
     *
     * @param channel
     *         a {@link io.netty.channel.Channel} object
     * @param stat
     *         a {@link io.netty.util.AttributeKey} object
     * @param <V>
     *         a V class
     *
     * @return a {@link java.lang.Number} object
     */
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

}


