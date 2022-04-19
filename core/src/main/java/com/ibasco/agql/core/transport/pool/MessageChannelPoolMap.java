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

package com.ibasco.agql.core.transport.pool;

import io.netty.channel.pool.SimpleChannelPool;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.concurrent.Promise;
import static io.netty.util.internal.ObjectUtil.checkNotNull;
import io.netty.util.internal.ReadOnlyIterator;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A custom {@link io.netty.channel.pool.ChannelPoolMap} implementation that allows a custom pool key to be used for {@link com.ibasco.agql.core.transport.pool.NettyChannelPool} lookup.
 *
 * @author Rafael Luis Ibasco
 */
public class MessageChannelPoolMap implements NettyChannelPoolMap<Object, NettyChannelPool>, Iterable<Map.Entry<Object, NettyChannelPool>> {

    private static final Logger log = LoggerFactory.getLogger(MessageChannelPoolMap.class);

    private final ConcurrentMap<Object, NettyChannelPool> map = new ConcurrentHashMap<>();

    private final NettyPooledChannelFactory pooledChannelFactory;

    /**
     * <p>Constructor for MessageChannelPoolMap.</p>
     *
     * @param pooledChannelFactory a {@link com.ibasco.agql.core.transport.pool.NettyPooledChannelFactory} object
     */
    public MessageChannelPoolMap(final NettyPooledChannelFactory pooledChannelFactory) {
        this.pooledChannelFactory = pooledChannelFactory;
    }

    /** {@inheritDoc} */
    @Override
    public NettyChannelPool get(Object data) {
        final InetSocketAddress remoteAddress = getResolver().resolveRemoteAddress(data);
        final Object poolKey = getResolver().resolvePoolKey(data);
        NettyChannelPool pool = map.get(poolKey);
        if (pool == null) {
            pool = pooledChannelFactory.getChannelPoolFactory().create(null, remoteAddress);
            NettyChannelPool old = map.putIfAbsent(poolKey, pool);
            if (old != null) {
                // We need to destroy the newly created pool as we not use it.
                poolCloseAsyncIfSupported(pool);
                pool = old;
            }
        }
        return pool;
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(Object data) {
        return map.containsKey(getResolver().resolvePoolKey(data));
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {
        for (Object key : map.keySet()) {
            // Wait for remove to finish ensuring that resources are released before returning from close
            removeAsyncIfSupported(key).syncUninterruptibly();
        }
        getChannelPoolFactory().getChannelFactory().close();
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public Iterator<Map.Entry<Object, NettyChannelPool>> iterator() {
        return new ReadOnlyIterator<>(map.entrySet().iterator());
    }

    private Future<Boolean> removeAsyncIfSupported(Object key) {
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

    private static Future<Void> poolCloseAsyncIfSupported(NettyChannelPool pool) {
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

    /**
     * <p>getChannelPoolFactory.</p>
     *
     * @return a {@link com.ibasco.agql.core.transport.pool.NettyChannelPoolFactory} object
     */
    public NettyChannelPoolFactory getChannelPoolFactory() {
        return pooledChannelFactory.getChannelPoolFactory();
    }

    /**
     * <p>getResolver.</p>
     *
     * @return a {@link com.ibasco.agql.core.transport.pool.NettyPoolPropertyResolver} object
     */
    public NettyPoolPropertyResolver getResolver() {
        if (!(pooledChannelFactory.getResolver() instanceof NettyPoolPropertyResolver))
            throw new IllegalStateException("Property resolver must be a type of " + NettyPoolPropertyResolver.class.getSimpleName());
        return (NettyPoolPropertyResolver) pooledChannelFactory.getResolver();
    }
}
