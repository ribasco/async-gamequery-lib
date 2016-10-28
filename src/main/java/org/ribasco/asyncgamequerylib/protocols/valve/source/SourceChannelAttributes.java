/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Luis Ibasco
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

package org.ribasco.asyncgamequerylib.protocols.valve.source;

import io.netty.channel.pool.ChannelPool;
import io.netty.util.AttributeKey;
import org.ribasco.asyncgamequerylib.core.AbstractMessenger;
import org.ribasco.asyncgamequerylib.core.AbstractPacketBuilder;
import org.ribasco.asyncgamequerylib.core.SplitPacketContainer;
import org.ribasco.asyncgamequerylib.protocols.valve.source.handlers.SourceQueryPacketAssembler;

import java.util.Map;

/**
 * Created by raffy on 9/26/2016.
 */
public class SourceChannelAttributes {
    public static final AttributeKey<? extends AbstractPacketBuilder> PACKET_BUILDER = AttributeKey.valueOf("packetBuilder");
    public static final AttributeKey<? extends AbstractMessenger> MESSENGER = AttributeKey.valueOf("messenger");
    public static final AttributeKey<? extends Map<SourceQueryPacketAssembler.SplitPacketKey, SplitPacketContainer>> SPLIT_PACKET_CONTAINER = AttributeKey.valueOf("splitPacketContainer");
    public static final AttributeKey<ChannelPool> CHANNEL_POOL = AttributeKey.valueOf("channelPool");
}
