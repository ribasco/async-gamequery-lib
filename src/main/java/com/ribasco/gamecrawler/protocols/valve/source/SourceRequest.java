package com.ribasco.gamecrawler.protocols.valve.source;

import com.ribasco.gamecrawler.protocols.GameRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract Class Representing Source Server Queries
 */
public abstract class SourceRequest implements GameRequest {

    private static final Logger log = LoggerFactory.getLogger(SourceRequest.class);

    private byte header;
    private byte[] payload;

    public byte getHeader() {
        return header;
    }

    public void setHeader(byte header) {
        this.header = header;
    }

    public byte[] getPayload() {
        return this.payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    public byte[] getProtocolHeader() {
        return new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
    }

    @Override
    public byte[] getBytes() {
        byte[] payload = getPayload();
        byte[] protocolHeader = getProtocolHeader();
        int protocolHeaderLength = ((protocolHeader != null) ? protocolHeader.length : 0);
        int packetSize = protocolHeaderLength + 1 + payload.length + 1;

        ByteBuf buf = Unpooled.buffer(packetSize);

        //Include Protocol Header if available
        if (protocolHeader != null)
            buf.writeBytes(protocolHeader);

        //Include Query Header
        buf.writeByte(getHeader());

        //Include Payload (if available)
        if (payload.length > 0)
            buf.writeBytes(payload);

        //Return the backing array representation
        return buf.array();
    }
}
