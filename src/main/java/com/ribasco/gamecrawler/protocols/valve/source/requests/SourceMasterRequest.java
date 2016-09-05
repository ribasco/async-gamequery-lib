package com.ribasco.gamecrawler.protocols.valve.source.requests;

import com.ribasco.gamecrawler.protocols.valve.source.SourceRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.net.InetSocketAddress;

/**
 * Created by raffy on 9/1/2016.
 */
public class SourceMasterRequest extends SourceRequest {
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

    private String filter;

    private String startIp;

    public SourceMasterRequest(byte region, String filter, InetSocketAddress startIp) {
        this(region, filter, new StringBuilder().append(startIp.getAddress().getHostAddress()).append(":").append(startIp.getPort()).toString());
    }

    public SourceMasterRequest(byte region, String filter, String startIp) {
        setHeader(A2M_GET_SERVERS_BATCH2_HEADER);
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

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
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
        int payloadSize = (3 + this.filter.length() + (this.startIp.length()));
        ByteBuf payload = Unpooled.buffer(payloadSize);
        payload.writeByte(getRegion());
        payload.writeBytes(getStartIp().getBytes());
        payload.writeByte(0); //terminating byte
        payload.writeBytes(getFilter().getBytes());
        return payload.array();
    }

    @Override
    public String toString() {
        return String.format("REGION: %d, FILTER: %s, START IP: %s", this.region, this.filter, this.startIp);
    }
}
