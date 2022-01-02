/*
 * Copyright 2018-2022 Asynchronous Game Query Library
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

package com.ibasco.agql.core.enums;

import io.netty.channel.Channel;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ChannelType {
    NIO_TCP(NioSocketChannel.class),
    NIO_UDP(NioDatagramChannel.class);

    private static final Logger log = LoggerFactory.getLogger(ChannelType.class);

    Class<? extends Channel> channelClass;

    ChannelType(Class<? extends Channel> channelType) {
        this.channelClass = channelType;
    }

    //TODO: Consider merging this and createEventLoopGroup() into one factory class.
    public Class<? extends Channel> getChannelClass() {
        if (Epoll.isAvailable()) {
            if (NioSocketChannel.class.equals(channelClass)) {
                log.debug("Using EpollSocketChannel");
                return EpollSocketChannel.class;
            } else if (NioDatagramChannel.class.equals(channelClass)) {
                log.debug("Using EpollDatagramChannel");
                return EpollDatagramChannel.class;
            }
        }
        log.debug("Epoll not available. Falling back to {}", channelClass);
        return channelClass;
    }
}
