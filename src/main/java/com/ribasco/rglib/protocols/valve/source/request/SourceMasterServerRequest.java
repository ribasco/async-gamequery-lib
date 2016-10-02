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

package com.ribasco.rglib.protocols.valve.source.request;

import com.ribasco.rglib.protocols.valve.source.SourceMasterFilter;
import com.ribasco.rglib.protocols.valve.source.SourceServerRequest;
import com.ribasco.rglib.protocols.valve.source.enums.SourceMasterServerRegion;
import com.ribasco.rglib.protocols.valve.source.packets.request.SourceMasterRequestPacket;

import java.net.InetSocketAddress;

/**
 * Created by raffy on 9/15/2016.
 */
public class SourceMasterServerRequest<T> extends SourceServerRequest<SourceMasterRequestPacket> {

    private SourceMasterServerRegion region;
    private SourceMasterFilter filter;
    private InetSocketAddress startIp;

    public SourceMasterServerRequest(InetSocketAddress recipient, SourceMasterServerRegion region, SourceMasterFilter filter, InetSocketAddress startIp) {
        super(recipient);
        this.region = region;
        this.filter = filter;
        this.startIp = startIp;
    }

    @Override
    public SourceMasterRequestPacket getMessage() {
        return new SourceMasterRequestPacket(region, filter, startIp);
    }
}
