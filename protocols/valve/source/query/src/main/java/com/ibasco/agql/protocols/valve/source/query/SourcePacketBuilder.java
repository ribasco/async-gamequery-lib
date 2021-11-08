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

package com.ibasco.agql.protocols.valve.source.query;

import com.ibasco.agql.core.AbstractPacketBuilder;
import com.ibasco.agql.core.utils.ByteUtils;
import com.ibasco.agql.protocols.valve.source.query.enums.SourceGameResponse;
import com.ibasco.agql.protocols.valve.source.query.packets.response.SourceChallengeResponsePacket;
import com.ibasco.agql.protocols.valve.source.query.packets.response.SourceInfoResponsePacket;
import com.ibasco.agql.protocols.valve.source.query.packets.response.SourcePlayerResponsePacket;
import com.ibasco.agql.protocols.valve.source.query.packets.response.SourceRulesResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Responsible for encoding and decoding of source packets</p>
 *
 * Created by raffy on 9/16/2016.
 */
public class SourcePacketBuilder extends AbstractPacketBuilder<SourceServerPacket> {

    private static final Logger log = LoggerFactory.getLogger(SourcePacketBuilder.class);

    public SourcePacketBuilder(ByteBufAllocator alloc) {
        super(alloc);
    }

    public static <T extends SourceServerPacket> SourceServerPacket createResponsePacketFromHeader(byte header) {
        SourceGameResponse res = SourceGameResponse.get(header);
        if (res == null) {
            log.debug("Did not find a matching response class for header : {}", header);
            return null;
        }
        switch (res) {
            case CHALLENGE:
                return new SourceChallengeResponsePacket();
            case INFO:
                return new SourceInfoResponsePacket();
            case PLAYER:
                return new SourcePlayerResponsePacket();
            case RULES:
                return new SourceRulesResponsePacket();
            default:
                break;
        }
        return null;
    }

    @Override
    public <T extends SourceServerPacket> T construct(ByteBuf data) {
        //Mark Index
        data.markReaderIndex();
        try {
            //Reset the index
            data.readerIndex(0);

            //Verify size
            if (data.readableBytes() < 5)
                throw new IllegalStateException("Cannot continue processing buffer with less than or equal to 4 bytes");

            //Read protocol header
            int protocolHeader = data.readIntLE();

            //Check if this is a split packet
            if (protocolHeader == 0xFFFFFFFE)
                throw new IllegalStateException("Cannot construct a response from a partial/split packet.");

            //Verify that we have a valid header
            if (protocolHeader != 0xFFFFFFFF)
                throw new IllegalStateException("Protocol header not supported.");

            //Read packet header
            byte packetHeader = data.readByte();

            //Read payload
            byte[] payload = new byte[data.readableBytes()];
            data.readBytes(payload);

            //Verify if packet header is valid
            SourceServerPacket packet = createResponsePacketFromHeader(packetHeader);

            //If packet is empty, means the supplied packet header is not supported
            if (packet == null)
                return null;

            packet.setProtocolHeader(ByteUtils.toByteArrayLE(protocolHeader));
            packet.setHeader(packetHeader);
            packet.setPayload(payload);

            //noinspection unchecked
            return (T) packet;
        } finally {
            data.resetReaderIndex();
        }
    }

    /**
     * Convert a source packet instance to it's byte representation
     *
     * @param packet The {@link SourceServerPacket} to convert
     *
     * @return Returns the deconstructed packet in byte array form
     */
    @Override
    public byte[] deconstruct(SourceServerPacket packet) {
        if (packet == null)
            throw new IllegalArgumentException("Invalid packet specified in the arguments.");

        byte[] payload = packet.getPayload();
        byte[] protocolHeader = packet.getProtocolHeader();
        byte[] packetHeader = packet.getPacketHeader();

        int payloadSize = payload != null ? payload.length : 0;
        int protocolHeaderSize = protocolHeader != null ? protocolHeader.length : 0;
        int packetHeaderSize = packetHeader != null ? packetHeader.length : 0;
        int packetSize = protocolHeaderSize + packetHeaderSize + payloadSize;

        //Allocate our buffer
        ByteBuf buf = createBuffer(packetSize);
        byte[] data;

        try {
            //Include Protocol Header if available
            if (protocolHeaderSize > 0)
                buf.writeBytes(protocolHeader);

            //Include Packet Header
            if (packetHeaderSize > 0)
                buf.writeBytes(packetHeader);

            //Include Payload (if available)
            if (payloadSize > 0)
                buf.writeBytes(payload);

            //Store the buffer into a byte array
            data = new byte[buf.readableBytes()];
            if (data.length > 0) {
                buf.readBytes(data);
            }
        } finally {
            buf.release();
        }

        log.debug("Constructing '{}' with total of {} bytes of data", packet.getClass().getSimpleName(), data.length);

        //Return the backing array representation
        return data;
    }
}
