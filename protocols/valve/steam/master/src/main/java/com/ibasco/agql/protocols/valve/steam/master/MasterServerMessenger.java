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

package com.ibasco.agql.protocols.valve.steam.master;

import com.google.common.collect.Multimap;
import com.ibasco.agql.core.Transport;
import com.ibasco.agql.core.enums.QueueStrategy;
import com.ibasco.agql.core.messenger.GameServerMessenger;
import com.ibasco.agql.core.transport.udp.NettyPooledUdpTransport;
import io.netty.channel.ChannelOption;

import java.util.concurrent.ExecutorService;

/**
 * Created by raffy on 10/22/2016.
 */
public class MasterServerMessenger extends GameServerMessenger<MasterServerRequest, MasterServerResponse> {

    public MasterServerMessenger() {
        this(null);
    }

    public MasterServerMessenger(ExecutorService executorService) {
        super(QueueStrategy.ASYNCHRONOUS, executorService);
    }

    @Override
    protected Transport<MasterServerRequest> createTransport() {
        NettyPooledUdpTransport<MasterServerRequest> transport = new NettyPooledUdpTransport<>(getExecutorService());
        //Set our channel initializer
        transport.setChannelInitializer(new MasterServerChannelInitializer(this));
        return transport;
    }
}
