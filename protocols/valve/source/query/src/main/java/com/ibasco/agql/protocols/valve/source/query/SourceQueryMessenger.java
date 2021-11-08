/*
 * MIT License
 *
 * Copyright (c) 2018 Asynchronous Game Query Library
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ibasco.agql.protocols.valve.source.query;

import com.google.common.collect.Multimap;
import com.ibasco.agql.core.Transport;
import com.ibasco.agql.core.enums.QueueStrategy;
import com.ibasco.agql.core.messenger.GameServerMessenger;
import com.ibasco.agql.core.session.SessionManager;
import com.ibasco.agql.core.session.SessionEntry;
import com.ibasco.agql.core.transport.udp.NettyPooledUdpTransport;
import com.ibasco.agql.protocols.valve.source.query.request.SourceChallengeRequest;
import com.ibasco.agql.protocols.valve.source.query.request.SourceInfoRequest;
import com.ibasco.agql.protocols.valve.source.query.request.SourcePlayerRequest;
import com.ibasco.agql.protocols.valve.source.query.request.SourceRulesRequest;
import com.ibasco.agql.protocols.valve.source.query.response.SourceChallengeResponse;
import com.ibasco.agql.protocols.valve.source.query.response.SourceInfoResponse;
import com.ibasco.agql.protocols.valve.source.query.response.SourcePlayerResponse;
import com.ibasco.agql.protocols.valve.source.query.response.SourceRulesResponse;
import io.netty.channel.ChannelOption;
import io.netty.channel.FixedRecvByteBufAllocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

/**
 * Created by raffy on 9/14/2016.
 */
public class SourceQueryMessenger extends GameServerMessenger<SourceServerRequest, SourceServerResponse> {

    private static final Logger log = LoggerFactory.getLogger(SourceQueryMessenger.class);

    public SourceQueryMessenger() {
        //Use the default session manager
        this(QueueStrategy.ASYNCHRONOUS, null);
    }

    public SourceQueryMessenger(QueueStrategy queueStrategy, ExecutorService executorService) {
        //Use the default session manager
        super(queueStrategy, executorService);
    }

    @Override
    protected Transport<SourceServerRequest> createTransport() {
        log.debug("Creating UDP Pooled Transport for '{}'", getClass().getSimpleName());
        NettyPooledUdpTransport<SourceServerRequest> transport = new NettyPooledUdpTransport<>(getExecutorService());
        transport.setChannelInitializer(new SourceQueryChannelInitializer(this));
        transport.addChannelOption(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(1400));
        return transport;
    }
}
