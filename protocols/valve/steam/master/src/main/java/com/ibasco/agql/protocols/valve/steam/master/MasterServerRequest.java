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

import com.ibasco.agql.core.AbstractGameServerRequest;
import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerRegion;
import com.ibasco.agql.protocols.valve.steam.master.packets.MasterServerRequestPacket;

import java.net.InetSocketAddress;

/**
 * Created by raffy on 9/15/2016.
 */
public class MasterServerRequest<T> extends AbstractGameServerRequest<MasterServerRequestPacket> {

    private final MasterServerRegion region;
    private final MasterServerFilter filter;
    private final InetSocketAddress startIp;

    public MasterServerRequest(InetSocketAddress recipient, MasterServerRegion region, MasterServerFilter filter, InetSocketAddress startIp) {
        super(recipient);
        this.region = region;
        this.filter = filter;
        this.startIp = startIp;
    }

    @Override
    public MasterServerRequestPacket getMessage() {
        return new MasterServerRequestPacket(region, filter, startIp);
    }
}
