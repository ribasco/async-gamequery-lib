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

package org.ribasco.asyncgamequerylib.protocols.valve.source.packets.request;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.ribasco.asyncgamequerylib.protocols.valve.source.SourceMasterFilter;
import org.ribasco.asyncgamequerylib.protocols.valve.source.SourceRequestPacket;
import org.ribasco.asyncgamequerylib.protocols.valve.source.enums.SourceGameRequest;
import org.ribasco.asyncgamequerylib.protocols.valve.source.enums.SourceMasterServerRegion;

import java.net.InetSocketAddress;

@Deprecated
public class SourceMasterRequestPacket extends SourceRequestPacket {

    /**
     * The master source address for Source Servers
     */
    public static final InetSocketAddress SOURCE_MASTER = new InetSocketAddress("hl2master.steampowered.com", 27011);

    private byte region;

    private SourceMasterFilter filter;

    private String startIp;

    public SourceMasterRequestPacket() {

    }

    public SourceMasterRequestPacket(SourceMasterServerRegion region, SourceMasterFilter filter, InetSocketAddress startIp) {
        this(region, filter, new StringBuilder().append(startIp.getAddress().getHostAddress()).append(":").append(startIp.getPort()).toString());
    }

    public SourceMasterRequestPacket(SourceMasterServerRegion region, SourceMasterFilter filter, String startIp) {
        setHeader(SourceGameRequest.MASTER);
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

    /**
     * Override this as we have no protocol header for this request
     *
     * @return
     */
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
}
