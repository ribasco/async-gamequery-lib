/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Ibasco
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **************************************************************************************************/

package com.ribasco.rglib.core.transport.pool;

import com.ribasco.rglib.core.AbstractMessage;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelPool;
import io.netty.channel.pool.ChannelPoolMap;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

/**
 * A {@link ChannelPoolMap} implementation that use {@link AbstractMessage} as it's key for the underlying map container
 *
 * @param <M> An {@link AbstractMessage} that will be used as a reference for our key lookup
 * @param <K> The key that will be used for our underlying map.
 *            The type of this key should be the same type returned by our key resolver.
 */
public class MessageChannelPoolMap<M extends AbstractMessage, K>
        implements ChannelPoolMap<M, ChannelPool>, Iterable<Map.Entry<K, ChannelPool>>, Closeable {

    private Function<M, K> keyResolver;
    private Function<K, ChannelPool> poolFactory;

    private AbstractChannelPoolMap<K, ChannelPool> internalPoolMap = new AbstractChannelPoolMap<K, ChannelPool>() {
        @Override
        protected ChannelPool newPool(K key) {
            return poolFactory.apply(key);
        }
    };

    public MessageChannelPoolMap(Function<M, K> keyResolver, Function<K, ChannelPool> poolFactory) {
        this.keyResolver = keyResolver;
        this.poolFactory = poolFactory;
    }

    @Override
    public ChannelPool get(M message) {
        return internalPoolMap.get(keyResolver.apply(message));
    }

    @Override
    public boolean contains(M message) {
        return internalPoolMap.contains(keyResolver.apply(message));
    }

    @Override
    public void close() throws IOException {
        internalPoolMap.close();
    }

    @Override
    public Iterator<Map.Entry<K, ChannelPool>> iterator() {
        return internalPoolMap.iterator();
    }
}
