package org.ribasco.agql.core.transport;

import io.netty.channel.pool.ChannelPool;
import io.netty.util.AttributeKey;

public class ChannelAttributes {
    public static final AttributeKey<ChannelPool> CHANNEL_POOL = AttributeKey.valueOf("channelPool");
}
