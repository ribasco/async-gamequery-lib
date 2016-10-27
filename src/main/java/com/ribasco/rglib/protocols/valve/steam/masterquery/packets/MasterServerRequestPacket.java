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

package com.ribasco.rglib.protocols.valve.steam.masterquery.packets;

import com.ribasco.rglib.protocols.valve.steam.masterquery.MasterServerFilter;
import com.ribasco.rglib.protocols.valve.steam.masterquery.MasterServerPacket;
import com.ribasco.rglib.protocols.valve.steam.masterquery.enums.MasterServerRegion;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

import java.net.InetSocketAddress;

public class MasterServerRequestPacket extends MasterServerPacket {

    /**
     * The master source address for Source Servers
     */
    public static final InetSocketAddress SOURCE_MASTER = new InetSocketAddress("hl2master.steampowered.com", 27011);

    public static final InetSocketAddress GOLDSRC_MASTER = new InetSocketAddress("hl1master.steampowered.com", 27010);

    private static final byte MASTER_SERVER_REQUEST_HEADER = 0x31;

    private byte region;

    private MasterServerFilter filter;

    private String startIp;

    public MasterServerRequestPacket() {

    }

    public MasterServerRequestPacket(MasterServerRegion region, MasterServerFilter filter, InetSocketAddress startIp) {
        this(region, filter, new StringBuilder().append(startIp.getAddress().getHostAddress()).append(":").append(startIp.getPort()).toString());
    }

    public MasterServerRequestPacket(MasterServerRegion region, MasterServerFilter filter, String startIp) {
        setHeader(MASTER_SERVER_REQUEST_HEADER);
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

    public MasterServerFilter getFilter() {
        return filter;
    }

    public void setFilter(MasterServerFilter filter) {
        this.filter = filter;
    }

    public String getStartIp() {
        return startIp;
    }

    public void setStartIp(String startIp) {
        this.startIp = startIp;
    }

    @Override
    public byte[] getPayload() {
        String filterString = this.filter.toString();
        int payloadSize = (3 + filterString.length() + (this.startIp.length()));
        final ByteBuf payload = PooledByteBufAllocator.DEFAULT.buffer(payloadSize);
        try {
            payload.writeByte(getRegion());
            payload.writeBytes(getStartIp().getBytes());
            payload.writeByte(0); //terminating byte
            payload.writeBytes(filterString.getBytes());
            byte[] payloadBytes = new byte[payload.readableBytes()];
            payload.readBytes(payloadBytes);
            return payloadBytes;
        } finally {
            payload.release();
        }
    }
}
