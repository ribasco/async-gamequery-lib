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

package com.ribasco.gamecrawler.protocols.valve.server.packets.requests;

import com.ribasco.gamecrawler.protocols.valve.server.SourceMasterFilter;
import com.ribasco.gamecrawler.protocols.valve.server.SourceRequestPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.net.InetSocketAddress;

import static com.ribasco.gamecrawler.protocols.valve.server.SourceConstants.REQUEST_MASTER_HEADER;

/**
 * Created by raffy on 9/1/2016.
 */
public class SourceMasterRequestPacket extends SourceRequestPacket {
    /**
     * Master server header
     */
    public static final byte A2M_GET_SERVERS_BATCH2_HEADER = 0x31;

    /**
     * The region code for the US east coast
     */
    public static final byte REGION_US_EAST_COAST = 0x00;

    /**
     * The region code for the US west coast
     */
    public static final byte REGION_US_WEST_COAST = 0x01;

    /**
     * The region code for South America
     */
    public static final byte REGION_SOUTH_AMERICA = 0x02;

    /**
     * The region code for Europe
     */
    public static final byte REGION_EUROPE = 0x03;

    /**
     * The region code for Asia
     */
    public static final byte REGION_ASIA = 0x04;

    /**
     * The region code for Australia
     */
    public static final byte REGION_AUSTRALIA = 0x05;

    /**
     * The region code for the Middle East
     */
    public static final byte REGION_MIDDLE_EAST = 0x06;

    /**
     * The region code for the whole world
     */
    public static final byte REGION_ALL = (byte) 0xFF;

    /**
     * The region code for Africa
     */
    public static final byte REGION_AFRICA = 0x07;

    /**
     * The master server address for Source Servers
     */
    public static final InetSocketAddress SOURCE_MASTER = new InetSocketAddress("hl2master.steampowered.com", 27011);

    private byte region;

    private SourceMasterFilter filter;

    private String startIp;

    public SourceMasterRequestPacket(byte region, SourceMasterFilter filter, InetSocketAddress startIp) {
        this(region, filter, new StringBuilder().append(startIp.getAddress().getHostAddress()).append(":").append(startIp.getPort()).toString());
    }

    public SourceMasterRequestPacket(byte region, SourceMasterFilter filter, String startIp) {
        setHeader(new byte[]{REQUEST_MASTER_HEADER});
        this.region = region;
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
