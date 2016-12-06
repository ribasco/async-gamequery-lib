/*
 *   MIT License
 *
 *   Copyright (c) 2016 Asynchronous Game Query Library
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in all
 *   copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   SOFTWARE.
 */

package com.ibasco.agql.protocols.mojang.minecraft.query.handlers;

import com.ibasco.agql.protocols.mojang.minecraft.query.McRconPacketBuilder;
import com.ibasco.agql.protocols.mojang.minecraft.query.McRconResponsePacket;
import com.ibasco.agql.protocols.mojang.minecraft.query.enums.McRconResponseType;
import com.ibasco.agql.protocols.mojang.minecraft.query.packets.response.McRconTermResponsePacket;
import com.ibasco.agql.protocols.mojang.minecraft.query.utils.McRconUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>A Class that process incoming UDP datagrams and decodes each frame into {@link McRconResponsePacket}
 * instances</p>
 *
 * <p>
 * Rcon Packet Structure:
 * <pre>
 * ----------------------------------------------------------------------------
 * Field           Type                                    Value
 * ----------------------------------------------------------------------------
 * Size            32-bit little-endian Signed Integer     Varies, see below.
 * ID              32-bit little-endian Signed Integer     Varies, see below.
 * Type            32-bit little-endian Signed Integer     Varies, see below.
 * Body            Null-terminated ASCII String            Varies, see below.
 * Empty String    Null-terminated ASCII String            0x00
 * ----------------------------------------------------------------------------
 * </pre>
 *
 * @see <a href="https://developer.valvesoftware.com/wiki/Source_RCON_Protocol">Source RCON Protocol</a>
 */
public class McRconPacketDecoder extends ByteToMessageDecoder {
    private static final Logger log = LoggerFactory.getLogger(McRconPacketDecoder.class);

    private AtomicInteger index = new AtomicInteger();

    private final static int PAD_SIZE = 56;

    private McRconPacketBuilder builder;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("Initializing McRconPacketBuilder");
        builder = new McRconPacketBuilder(ctx.channel().alloc());
    }

    //TODO: NPath complexity of 16800 as reported by PMD. Consider refactoring this and break it down to smaller bits
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        final String separator = "=================================================================================================";

        //TODO: Move all code logic below to McRconPacketBuilder

        log.debug(separator);
        log.debug(" ({}) DECODING INCOMING DATA : Bytes Received = {} {}", index.incrementAndGet(), in.readableBytes(), index.get() > 1 ? "[Continuation]" : "");
        log.debug(separator);

        String desc = StringUtils.rightPad("Minimum allowable size?", PAD_SIZE);
        //Verify we have the minimum allowable size
        if (in.readableBytes() < 14) {
            log.debug(" [ ] {} = NO (Actual Readable Bytes: {})", desc, in.readableBytes());
            return;
        }
        log.debug(" [x] {} = YES (Actual Readable Bytes: {})", desc, in.readableBytes());

        //Reset if this happens to be not a valid source rcon packet
        in.markReaderIndex();

        //Read and Verify size
        desc = StringUtils.rightPad("Bytes received at least => than the \"declared\" size?", PAD_SIZE);
        int size = in.readIntLE();
        int readableBytes = in.readableBytes();
        if (readableBytes < size) {
            log.debug(" [ ] {} = NO (Declared Size: {}, Actual Bytes Read: {})", desc, readableBytes, size);
            in.resetReaderIndex();
            return;
        }
        log.debug(" [x] {} = YES (Declared Size: {}, Actual Bytes Read: {})", desc, readableBytes, size);

        //Read and verify request id
        desc = StringUtils.rightPad("Request Id within the valid range?", PAD_SIZE);
        int id = in.readIntLE();
        if (!(id == -1 || id == McRconUtil.RCON_TERMINATOR_RID || McRconUtil.isValidRequestId(id))) {
            log.debug(" [ ] {} = NO (Actual: {})", desc, id);
            in.resetReaderIndex();
            return;
        }
        log.debug(" [x] {} = YES (Actual: {})", desc, id);

        //Read and verify request type
        desc = StringUtils.rightPad("Valid response type?", PAD_SIZE);
        int type = in.readIntLE();
        if (McRconResponseType.get(type) == null) {
            log.debug(" [ ] {} = NO (Actual: {})", desc, type);
            in.resetReaderIndex();
            return;
        }
        log.debug(" [x] {} = YES (Actual: {} = {})", desc, type, McRconResponseType.get(type));

        //Read and verify body
        desc = StringUtils.rightPad("Contains Body?", PAD_SIZE);
        int bodyLength = in.bytesBefore((byte) 0);
        String body = StringUtils.EMPTY;
        if (bodyLength <= 0)
            log.debug(" [ ] {} = NO", desc);
        else {
            body = in.readCharSequence(bodyLength, StandardCharsets.UTF_8).toString();
            log.debug(" [x] {} = YES (Length: {}, Body: {})", desc, bodyLength, StringUtils.replaceAll(StringUtils.truncate(body, 30), "\n", "\\\\n"));
        }

        //Peek at the last two bytes and verify that they are null-bytes
        byte bodyTerminator = in.getByte(in.readerIndex());
        byte packetTerminator = in.getByte(in.readerIndex() + 1);

        desc = StringUtils.rightPad("Contains TWO null-terminating bytes at the end?", PAD_SIZE);

        //Make sure the last two bytes are NULL bytes (request id: 999 is reserved for split packet responses)
        if ((bodyTerminator != 0 || packetTerminator != 0) && (id == McRconUtil.RCON_TERMINATOR_RID)) {
            log.debug("Found a malformed terminator packet. Skipping the remaining {} bytes", in.readableBytes());
            log.debug("Malformed Packet: \n{}", ByteBufUtil.prettyHexDump(in));
            in.skipBytes(in.readableBytes());
            return;
        } else if (bodyTerminator != 0 || packetTerminator != 0) {
            log.debug(" [ ] {} = NO (Actual: Body Terminator = {}, Packet Terminator = {})", desc, bodyTerminator, packetTerminator);
            in.resetReaderIndex();
            return;
        } else {
            log.debug(" [x] {} = YES (Actual: Body Terminator = {}, Packet Terminator = {})", desc, bodyTerminator, packetTerminator);
            //All is good, skip the last two bytes
            if (in.readableBytes() >= 2)
                in.skipBytes(2);
        }

        //At this point, we can now construct a packet
        log.debug(" [x] Status: PASS (Size = {}, Id = {}, Type = {}, Remaining Bytes = {}, Body Size = {})", size, id, type, in.readableBytes(), bodyLength);
        log.debug(separator);

        //Reset the index
        index.set(0);

        //Construct the response packet and send to the next handlers
        McRconResponsePacket responsePacket;

        //Did we receive a terminator packet?
        if (id == McRconUtil.RCON_TERMINATOR_RID && StringUtils.isBlank(body)) {
            responsePacket = new McRconTermResponsePacket();
        } else {
            responsePacket = McRconPacketBuilder.getResponsePacket(type);
        }

        if (responsePacket != null) {
            responsePacket.setSize(size);
            responsePacket.setId(id);
            responsePacket.setType(type);
            responsePacket.setBody(body);
            log.debug("Decode Complete. Passing response for request id : '{}' to the next handler", id);
            out.add(responsePacket);
        }
    }
}
