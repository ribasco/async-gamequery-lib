/*
 * Copyright (c) 2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.protocols.valve.source.query.rcon.packets;

import com.ibasco.agql.core.PacketDecoder;
import com.ibasco.agql.core.exceptions.MalformedPacketException;
import com.ibasco.agql.core.exceptions.PacketDecodeException;
import com.ibasco.agql.core.util.Bytes;
import com.ibasco.agql.core.util.Netty;
import com.ibasco.agql.protocols.valve.source.query.rcon.SourceRcon;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteOrder;

/**
 * <p>SourceRconPacketDecoder class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SourceRconPacketDecoder implements PacketDecoder<SourceRconPacket> {

    private static final String LINE_SEPARATOR = StringUtils.repeat('=', 155);

    private static final Logger log = LoggerFactory.getLogger(SourceRconPacketDecoder.class);

    private final static int PAD_SIZE = 56;

    //Since handler is guaranteed to be called on
    // the same thread, we do not need this to be atomic
    private int index;

    private final ChannelHandlerContext ctx;

    private final boolean strict;

    /**
     * <p>Constructor for SourceRconPacketDecoder.</p>
     *
     * @param ctx a {@link io.netty.channel.ChannelHandlerContext} object
     * @param strict a boolean
     */
    public SourceRconPacketDecoder(ChannelHandlerContext ctx, boolean strict) {
        this.ctx = ctx;
        this.strict = strict;
    }

    /** {@inheritDoc} */
    @Override
    public SourceRconPacket decode(ByteBuf in) throws PacketDecodeException {
        try {
            debug(ctx, LINE_SEPARATOR);
            debug(ctx, " ({}) DECODING INCOMING DATA : Readable Bytes = {} {}", ++index, in.readableBytes(), index > 1 ? "[Continuation]" : "");
            debug(ctx, LINE_SEPARATOR);

            //Read and Verify size
            //Make sure that we have minimum number of bytes required to process an rcon packet
            String desc = StringUtils.rightPad("Minimum bytes required?", PAD_SIZE);
            int packetSize = in.readIntLE();
            if (in.readableBytes() < packetSize) {
                debug(ctx, " [ ] {} = NO  (Readable Bytes: {}, Packet Size: {})", desc, in.readableBytes(), packetSize);
                throw new PacketDecodeException(String.format("Not enough bytes to process packet (Available: %d bytes, Minimum Required: %d bytes)", in.readableBytes(), packetSize));
            } else if (packetSize <= 0) {
                //no more data to process, just return null
                return null;
            }
            debug(ctx, " [x] {} = YES (Readable Bytes: {}, Packet Size: {})", desc, in.readableBytes(), packetSize);

            //Read and verify request id
            desc = StringUtils.rightPad("Request Id within the valid range?", PAD_SIZE);
            int packetId = in.readIntLE();
            //skip terminator packets with request id = -1, that should be considered as valid
            if (packetId > 0 && !SourceRcon.isTerminatorId(packetId) && !SourceRcon.isValidRequestId(packetId)) {
                debug(ctx, " [ ] {} = NO (Actual: {})", desc, packetId);
                throw new PacketDecodeException("Invalid request id: " + packetId);
            }
            debug(ctx, " [x] {} = YES (Actual: {})", desc, packetId);

            //Read and verify request type
            desc = StringUtils.rightPad("Valid packet type?", PAD_SIZE);
            int packetType = in.readIntLE();
            if (packetType != SourceRcon.RCON_TYPE_RESPONSE_AUTH && packetType != SourceRcon.RCON_TYPE_RESPONSE_VALUE) {
                debug(ctx, " [ ] {} = NO (Actual: {})", desc, packetType);
                throw new PacketDecodeException("Invalid packet type: " + packetType);
            }
            if (log.isDebugEnabled())
                debug(ctx, " [x] {} = YES (Actual: '{}', {}, {})", desc, SourceRcon.getPacketTypeName(packetType), packetType, Bytes.toHexString(packetType, ByteOrder.LITTLE_ENDIAN));

            //Read and verify payload (body)
            desc = StringUtils.rightPad("Contains Body?", PAD_SIZE);
            int bodyLength = in.bytesBefore((byte) 0); //packetSize - 9;
            if (bodyLength < 0) {
                if (strict) {
                    throw new PacketDecodeException(String.format("Malformed packet. Did not find a NULL terminating byte following the packet body (Remaining bytes: %d, Body Length: %d, Packet Size: %d, Packet Id: %d)", in.readableBytes(), bodyLength, packetSize, packetId));
                } else {
                    bodyLength = packetSize - 9;
                    log.debug("Updating body length to: {} (Remaining bytes: {}, Packet Size: {}, Packet Type: {}, Packet Id: {})", bodyLength, in.readableBytes(), packetSize, packetType, packetId);
                }
            }
            ByteBuf packetPayload = in.readRetainedSlice(bodyLength + 1); //in.readBytes(bodyLength + 1) //add plus 1 to include reading the last NULL byte
            //assert packetPayload.readableBytes() == bodyLength + 1;
            if (bodyLength == 0) {
                debug(ctx, " [ ] {} = NO", desc);
            } else {
                if (log.isDebugEnabled()) {
                    byte[] body = new byte[bodyLength];
                    packetPayload.getBytes(packetPayload.readerIndex(), body, 0, bodyLength);
                    debug(ctx, " [x] {} = YES (Length: {}, Body String: {}, Data: {})", desc, bodyLength, RegExUtils.replaceAll(StringUtils.truncate(new String(body), 30), "\n", "\\\\n"), Bytes.toHexString(body));
                } else {
                    debug(ctx, " [x] {} = YES (Length: {})", desc, bodyLength);
                }
            }

            //assert that we have more bytes to process
            assert in.readableBytes() >= 1;

            //note: the terminating packet is always followed by another packet whose packet terminator is equals to 0x01
            //Should we require two succeeding NULL bytes to ensure packet is well-formed?
            if (in.readableBytes() < 1)
                throw new MalformedPacketException(String.format("Expected one more byte for the packet terminator but we have %d byte(s) left in the buffer", in.readableBytes()));

            //Read the next byte and verify that they are a valid rcon packet terminator
            int packetTerminator = in.readUnsignedByte();
            desc = StringUtils.rightPad("Valid packet terminator?", PAD_SIZE);
            if (!SourceRcon.isValidTerminator(packetTerminator)) {
                //malformed packet, throw an exception
                throw new MalformedPacketException("Expected either a packet terminating byte or a NULL byte but was: " + Bytes.toHexString(packetTerminator));
            }
            if (log.isDebugEnabled())
                debug(ctx, " [x] {} = YES (Packet Terminator = {} ({}))", desc, packetTerminator, Bytes.toHexString((byte) packetTerminator));

            //At this point, we can now construct a packet
            desc = StringUtils.rightPad("Decode Result", PAD_SIZE);
            if (log.isDebugEnabled())
                debug(ctx, " [x] {} = Size = {}, Id = {}, Type = {}, Remaining Bytes = {}, Payload Size = {}, Packet Terminator: {}", desc, packetSize, packetId, packetType, in.readableBytes(), bodyLength, Bytes.toHexString(packetTerminator));

            SourceRconPacket packet = SourceRconPacketFactory.createPacket(packetSize, packetId, packetType, packetTerminator, packetPayload);
            desc = StringUtils.rightPad("Decoded Packet", PAD_SIZE);
            debug(ctx, " [x] {} = {}", desc, packet);

            //Reset the index
            index = 0;
            return packet;
        } finally {
            debug(ctx, LINE_SEPARATOR);
        }
    }

    private static void debug(ChannelHandlerContext ctx, String msg, Object... args) {
        if (log.isDebugEnabled())
            log.debug(String.format("%s %s", Netty.id(ctx), msg), args);
    }
}
