package com.ibasco.agql.protocols.valve.source.query.handlers;

import com.ibasco.agql.protocols.valve.source.query.SourceRconPacketBuilder;
import com.ibasco.agql.protocols.valve.source.query.SourceRconResponsePacket;
import com.ibasco.agql.protocols.valve.source.query.packets.response.SourceRconTermResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.ibasco.agql.protocols.valve.source.query.enums.SourceRconResponseType.get;

/**
 * <p>Decodes that decodes Source Rcon Packets</p>
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
public class SourceRconPacketDecoder extends ByteToMessageDecoder {
    private static final Logger log = LoggerFactory.getLogger(SourceRconPacketDecoder.class);

    private AtomicInteger index = new AtomicInteger();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        log.debug("=============================================================");
        log.debug(" #{}) DECODING INCOMING DATA : Size = {} {}", index.incrementAndGet(), in.readableBytes(), index.get() > 1 ? "[Continuation]" : "");
        log.debug("=============================================================");

        //log.debug("Raw Data : \n{}", ByteBufUtil.prettyHexDump(in));

        String desc = StringUtils.rightPad("Minimum allowable size?", 56);
        //Verify we have the minimum allowable size
        if (in.readableBytes() < 14) {
            log.debug(" # {} = NO (Actual Readable Bytes: {})", desc, in.readableBytes());
            return;
        }
        log.debug(" # {} = YES (Actual Readable Bytes: {})", desc, in.readableBytes());

        //Reset if this happens to be not a valid source rcon packet
        in.markReaderIndex();

        //Read and Verify size
        desc = StringUtils.rightPad("Header size is at least = or > than the actual size?", 56);
        int size = in.readIntLE();
        if (in.readableBytes() < size) {
            log.debug(" # {} = NO (Declared Size: {}, Actual Size: {})", desc, in.readableBytes(), size);
            in.resetReaderIndex();
            return;
        }
        log.debug(" # {} = YES (Declared Size: {}, Actual Size: {})", desc, in.readableBytes(), size);

        //Read and verify request id
        desc = StringUtils.rightPad("Request Id within the valid range?", 56);
        int id = in.readIntLE();
        if (!(id == -1 || id == 999 || (id >= 100000000 && id <= 999999999))) {
            log.debug(" # {} = NO (Actual: {})", desc, id);
            in.resetReaderIndex();
            return;
        }
        log.debug(" # {} = YES (Actual: {})", desc, id);

        //Read and verify request type
        desc = StringUtils.rightPad("Valid response type?", 56);
        int type = in.readIntLE();
        if (get(type) == null) {
            log.debug(" # {} = NO (Actual: {})", desc, type);
            in.resetReaderIndex();
            return;
        }
        log.debug(" # {} = YES (Actual: {})", desc, type);

        //Read and verify body
        desc = StringUtils.rightPad("Contains Body?", 56);
        int bodyLength = in.bytesBefore((byte) 0);
        String body = StringUtils.EMPTY;
        if (bodyLength <= 0)
            log.debug(" # {} = NO", desc);
        else {
            body = in.readCharSequence(bodyLength, StandardCharsets.UTF_8).toString();
            log.debug(" # {} = YES (Length: {}, Body: {})", desc, bodyLength, StringUtils.replaceAll(StringUtils.truncate(body, 30), "\n", "\\\\n"));
        }

        //Peek at the last two bytes and verify that they are null-bytes
        byte bodyTerminator = in.getByte(in.readerIndex());
        byte packetTerminator = in.getByte(in.readerIndex() + 1);

        desc = StringUtils.rightPad("Contains TWO null-terminating bytes at the end?", 56);

        //Make sure the last two bytes are NULL bytes
        if ((bodyTerminator != 0 || packetTerminator != 0) && (id == 999)) {
            log.debug("Found a malformed terminator packet. Ignoring");
            in.skipBytes(in.readableBytes());
            return;
        } else if (bodyTerminator != 0 || packetTerminator != 0) {
            log.debug(" # {} = NO (Actual: Body Terminator = {}, Packet Terminator = {})", desc, bodyTerminator, packetTerminator);
            in.resetReaderIndex();
            return;
        } else {
            log.debug(" # {} = YES (Actual: Body Terminator = {}, Packet Terminator = {})", desc, bodyTerminator, packetTerminator);
            //All is good, skip the last two bytes
            if (in.readableBytes() >= 2)
                in.skipBytes(2);
        }

        //At this point, we can now construct a packet
        log.debug(" # Status: PASS (Size = {}, Id = {}, Type = {}, Remaining Bytes = {}, Body Size = {})", size, id, type, in.readableBytes(), bodyLength);

        index.set(0);

        //Construct the response packet and send to the next handlers
        SourceRconResponsePacket responsePacket;

        //Do we receive a terminator packet?
        if (id == 999 && StringUtils.isBlank(body)) {
            responsePacket = new SourceRconTermResponsePacket();
        } else {
            responsePacket = SourceRconPacketBuilder.getResponsePacket(type);
        }

        if (responsePacket != null) {
            responsePacket.setSize(size);
            responsePacket.setId(id);
            responsePacket.setType(type);
            responsePacket.setBody(body);
            out.add(responsePacket);
        }
    }
}
