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

package com.ribasco.rglib.protocols.valve.steam.master;

import com.ribasco.rglib.core.AbstractPacketBuilder;
import com.ribasco.rglib.core.Decodable;
import com.ribasco.rglib.protocols.valve.steam.master.packets.MasterServerResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MasterServerPacketBuilder extends AbstractPacketBuilder<MasterServerPacket> {

    private static Logger log = LoggerFactory.getLogger(MasterServerPacketBuilder.class);

    public MasterServerPacketBuilder(ByteBufAllocator allocator) {
        super(allocator);
    }

    @Override
    public <T extends MasterServerPacket> T construct(ByteBuf data) {
        //Mark Index
        data.markReaderIndex();
        try {
            //Reset the index
            data.readerIndex(0);
            //Verify size
            if (data.readableBytes() <= 6)
                throw new IllegalStateException("Cannot continue processing buffer with less than or equal to 6 bytes");
            //Read header
            byte[] header = new byte[6];
            data.readBytes(header);
            //Read payload
            byte[] payload = new byte[data.readableBytes()];
            data.readBytes(payload);
            //Verify if packet header is valid
            MasterServerResponsePacket packet = new MasterServerResponsePacket();
            packet.setHeader(header);
            packet.setPayload(payload);
            return (T) packet;
        } finally {
            data.resetReaderIndex();
        }
    }

    @Override
    public byte[] deconstruct(MasterServerPacket packet) {
        if (packet == null)
            throw new IllegalArgumentException("Invalid packet specified in the arguments.");

        byte[] payload = packet.getPayload();
        byte[] packetHeader = packet.getPacketHeader();

        int payloadSize = payload != null ? payload.length : 0;
        int packetHeaderSize = packetHeader != null ? packetHeader.length : 0;
        int packetSize = packetHeaderSize + payloadSize;

        //Allocate our buffer
        final ByteBuf buf = createBuffer(packetSize);
        byte[] data;

        try {
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

    @Override
    public <B> B decodePacket(Decodable<B> packet) {
        return packet.toObject();
    }
}
