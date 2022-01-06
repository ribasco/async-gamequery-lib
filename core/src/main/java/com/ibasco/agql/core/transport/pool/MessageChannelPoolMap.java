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

package com.ibasco.agql.core.transport.pool;

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.Envelope;
import com.ibasco.agql.core.util.TransportOptions;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.SimpleChannelPool;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.concurrent.Promise;
import static io.netty.util.internal.ObjectUtil.checkNotNull;
import io.netty.util.internal.ReadOnlyIterator;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A custom {@link ChannelPoolMap} implementation using {@link Envelope} as the key for obtaining a {@link NettyChannelPool} instance.
 *
 * @author Rafael Luis Ibasco
 */
public class MessageChannelPoolMap implements NettyChannelPoolMap<Envelope<? extends AbstractRequest>, NettyChannelPool>, Iterable<Map.Entry<Object, NettyChannelPool>>, Closeable {

    private static final Logger log = LoggerFactory.getLogger(MessageChannelPoolMap.class);

    private final ConcurrentMap<Object, NettyChannelPool> map = new ConcurrentHashMap<>();

    private final NettyChannelPoolFactory channelPoolFactory;

    private final NettyPoolingStrategy poolStrategy;

    public MessageChannelPoolMap(final NettyChannelPoolFactory channelPoolFactory) {
        this.channelPoolFactory = Objects.requireNonNull(channelPoolFactory, "Channel pool factory is not provided");
        this.poolStrategy = channelPoolFactory.getChannelFactory().getOptions().getOrDefault(TransportOptions.POOL_STRATEGY);
        log.debug("[INIT] POOL => Using pool strategy '{}'", poolStrategy.getName());
    }

    @Override
    public NettyChannelPool get(Envelope<? extends AbstractRequest> envelope) {
        Objects.requireNonNull(envelope, "Envelope must not be null");
        Objects.requireNonNull(envelope.sender(), "Receipient address is null");
        Objects.requireNonNull(envelope.recipient(), "Destination address is null");

        Object key = poolStrategy.extractKey(envelope);
        NettyChannelPool pool = map.get(key);
        if (pool == null) {
            pool = channelPoolFactory.create(envelope.sender(), envelope.recipient());
            NettyChannelPool old = map.putIfAbsent(key, pool);
            if (old != null) {
                // We need to destroy the newly created pool as we not use it.
                poolCloseAsyncIfSupported(pool);
                pool = old;
            }
        }
        return pool;
    }

    @Override
    public boolean contains(Envelope<? extends AbstractRequest> key) {
        return map.containsKey(poolStrategy.extractKey(key));
    }

    @Override
    public void close() throws IOException {
        for (Object key : map.keySet()) {
            // Wait for remove to finish ensuring that resources are released before returning from close
            removeAsyncIfSupported(key).syncUninterruptibly();
        }
        this.channelPoolFactory.getChannelFactory().close();
    }

    @NotNull
    @Override
    public Iterator<Map.Entry<Object, NettyChannelPool>> iterator() {
        return new ReadOnlyIterator<>(map.entrySet().iterator());
    }

    private synchronized Future<Boolean> removeAsyncIfSupported(Object key) {
        NettyChannelPool pool = map.remove(checkNotNull(key, "key"));
        if (pool != null) {
            final Promise<Boolean> removePromise = GlobalEventExecutor.INSTANCE.newPromise();
            poolCloseAsyncIfSupported(pool).addListener(future -> {
                if (future.isSuccess()) {
                    removePromise.setSuccess(Boolean.TRUE);
                } else {
                    removePromise.setFailure(future.cause());
                }
            });
            return removePromise;
        }
        return GlobalEventExecutor.INSTANCE.newSucceededFuture(Boolean.FALSE);
    }

    private synchronized static Future<Void> poolCloseAsyncIfSupported(NettyChannelPool pool) {
        if (pool instanceof SimpleChannelPool) {
            return ((SimpleChannelPool) pool).closeAsync();
        } else {
            try {
                pool.close();
                return GlobalEventExecutor.INSTANCE.newSucceededFuture(null);
            } catch (Exception e) {
                return GlobalEventExecutor.INSTANCE.newFailedFuture(e);
            }
        }
    }
}