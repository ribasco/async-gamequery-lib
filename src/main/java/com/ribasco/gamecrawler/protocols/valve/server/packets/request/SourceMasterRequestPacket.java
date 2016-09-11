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

package com.ribasco.gamecrawler.protocols.valve.server.packets.request;

import com.ribasco.gamecrawler.protocols.valve.server.SourceMasterFilter;
import com.ribasco.gamecrawler.protocols.valve.server.SourceRequestPacket;
import com.ribasco.gamecrawler.protocols.valve.server.enums.SourceMasterServerRegion;
import com.ribasco.gamecrawler.protocols.valve.server.enums.SourceRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.net.InetSocketAddress;

/**
 * Created by raffy on 9/1/2016.
 */
public class SourceMasterRequestPacket extends SourceRequestPacket {

    /**
     * The master server address for Source Servers
     */
    public static final InetSocketAddress SOURCE_MASTER = new InetSocketAddress("hl2master.steampowered.com", 27011);

    private byte region;

    private SourceMasterFilter filter;

    private String startIp;

    public SourceMasterRequestPacket(SourceMasterServerRegion region, SourceMasterFilter filter, InetSocketAddress startIp) {
        this(region, filter, new StringBuilder().append(startIp.getAddress().getHostAddress()).append(":").append(startIp.getPort()).toString());
    }

    public SourceMasterRequestPacket(SourceMasterServerRegion region, SourceMasterFilter filter, String startIp) {
        setHeader(SourceRequest.MASTER);
        this.region = region.getHeader();
        this.filter = filter;
        this.startIp = startIp;
    }

    public byte getRegion() {
        return region;
    }

    public void setRegion(byte region) {
        this.region = region;
    }

    public SourceMasterFilter getFilter() {
        return filter;
    }

    public void setFilter(SourceMasterFilter filter) {
        this.filter = filter;
    }

    public String getStartIp() {
        return startIp;
    }

    public void setStartIp(String startIp) {
        this.startIp = startIp;
    }

    @Override
    public byte[] getProtocolHeader() {
        return null;
    }

    @Override
    public byte[] getPayload() {
        String filterString = this.filter.toString();
        int payloadSize = (3 + filterString.length() + (this.startIp.length()));
        ByteBuf payload = Unpooled.buffer(payloadSize);
        payload.writeByte(getRegion());
        payload.writeBytes(getStartIp().getBytes());
        payload.writeByte(0); //terminating byte
        payload.writeBytes(filterString.getBytes());
        return payload.array();
    }

    @Override
    public String toString() {
        return String.format("REGION: %d, FILTER: %s, START IP: %s", this.region, this.filter, this.startIp);
    }
}
