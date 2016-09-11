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

package com.ribasco.gamecrawler.protocols.valve.server;

import com.ribasco.gamecrawler.protocols.GameRequestPacket;
import com.ribasco.gamecrawler.protocols.valve.server.enums.SourceRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract Class Representing Source Server Queries
 */
//TODO: Refactor this, we should move this logic to the PacketAssembler class groups
public abstract class SourceRequestPacket implements GameRequestPacket {

    private static final Logger log = LoggerFactory.getLogger(SourceRequestPacket.class);

    private byte[] header;
    private byte[] payload;

    public byte[] getHeader() {
        return header;
    }

    public void setHeader(SourceRequest request) {
        setHeader(request.getHeader());
    }

    public void setHeader(byte header) {
        this.header = new byte[]{header};
    }

    public void setHeader(byte[] header) {
        this.header = header;
    }

    public byte[] getPayload() {
        return this.payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    @Override
    public byte[] getTrailer() {
        return new byte[0];
    }

    public byte[] getProtocolHeader() {
        return new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
    }

    @Override
    public byte[] getRequestData() {
        byte[] payload = getPayload();
        byte[] protocolHeader = getProtocolHeader();
        int payloadSize = (payload != null) ? payload.length : 0;
        int protocolHeaderLength = ((protocolHeader != null) ? protocolHeader.length : 0);
        int packetSize = protocolHeaderLength + 1 + payloadSize;

        ByteBuf buf = Unpooled.buffer(packetSize);

        //Include Protocol Header if available
        if (protocolHeader != null)
            buf.writeBytes(protocolHeader);

        //Include Query Header
        buf.writeBytes(getHeader());

        //Include Payload (if available)
        if (payloadSize > 0)
            buf.writeBytes(payload);

        byte[] data = buf.array();

        log.debug("Constructing Request Packet for {} with total of {} bytes of data", this.getClass().getSimpleName(), data.length);

        //Return the backing array representation
        return data;
    }
}
