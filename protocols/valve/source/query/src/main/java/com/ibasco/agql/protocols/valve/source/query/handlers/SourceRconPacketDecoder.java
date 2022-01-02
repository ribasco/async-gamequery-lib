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

package com.ibasco.agql.protocols.valve.source.query.handlers;

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.Envelope;
import com.ibasco.agql.core.PacketDecoder;
import com.ibasco.agql.core.transport.ChannelAttributes;
import com.ibasco.agql.core.transport.enums.ChannelEvent;
import com.ibasco.agql.core.util.NettyUtil;
import com.ibasco.agql.protocols.valve.source.query.SourceRcon;
import com.ibasco.agql.protocols.valve.source.query.SourceRconOptions;
import com.ibasco.agql.protocols.valve.source.query.message.SourceRconRequest;
import com.ibasco.agql.protocols.valve.source.query.packets.SourceRconPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.DefaultByteBufHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Decodes a raw {@link ByteBuf} message into a {@link SourceRconPacket} type. This decoder will read and collect decoded packets until everything has been received from the server.
 *
 * @author Rafael Luis Ibasco
 * @apiNote When the server receives an auth request, it will respond with an empty {@code SERVERDATA_RESPONSE_VALUE}, followed immediately by a {@code SERVERDATA_AUTH_RESPONSE} indicating whether authentication succeeded or failed.
 * Note that the status code is returned in the packet id field, so when pairing the response with the original auth request, you may need to look at the packet id of the preceeding {@code SERVERDATA_RESPONSE_VALUE}.
 * <p/>
 */
public class SourceRconPacketDecoder extends ByteToMessageDecoder {

    private static final Logger log = LoggerFactory.getLogger(SourceRconPacketDecoder.class);

    //minimum number of bytes for a Source Rcon Packet (Inclusive of the packet size field)
    private static final int MINIMUM_PACKET_SIZE = 14;

    private static final int PACKET_SIZE_LENGTH = 4;

    private static final int MAX_PACKET_SIZE = 4096;

    private PacketDecoder<SourceRconPacket> decoder;

    private ArrayList<SourceRconPacket> packets;

    private boolean terminatorPacketsEnabled;

    public SourceRconPacketDecoder() {
        setSingleDecode(false);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Envelope<AbstractRequest> envelope = ctx.channel().attr(ChannelAttributes.REQUEST).get();
        if (envelope == null)
            throw new IllegalStateException("Missing request envelope");
        if (!(envelope.content() instanceof SourceRconRequest))
            throw new IllegalStateException("Expected an instance of SourceRconRequest");

        boolean readMoreBytes = false;
        final SourceRconRequest request = (SourceRconRequest) envelope.content();
        //Initialize packet container
        if (packets == null)
            packets = new ArrayList<>();

        if (in.readableBytes() < PACKET_SIZE_LENGTH) {
            debug(ctx, "Readable bytes less than the packet size length. Skipping.");
            return;
        }

        //Peek the packet size value, do not increase the reader index yet
        //since our decoder will require the packet size
        int packetSize = in.getIntLE(in.readerIndex());

        //Apply some heuristics (if terminator packets are not enabled).
        //This is a dirty workaround for determining if we still have more bytes to read (only applies when terminator packets are disabled)
        if (!terminatorPacketsEnabled) {
            int pSize = packetSize + 4;
            if (packetSize >= MAX_PACKET_SIZE) {
                readMoreBytes = true;
            } else if ((pSize >= in.readableBytes()) && (pSize >= 4000)) {
                readMoreBytes = true;
                log.debug("Packet Size: {}", pSize);
            }
        }

        //Do we have more packets to process?
        if (packetSize == 0) {
            debug(ctx, "Packet size is 0. (Remaining Bytes: {})", in.readableBytes());

            //skip 4 bytes (packet size field)
            in.skipBytes(PACKET_SIZE_LENGTH);

            debug(ctx, "Skipped 4 bytes for packet size field. (Remaining bytes: {})", in.readableBytes());
            //Do we have more packets to process?
            if (in.readableBytes() >= MINIMUM_PACKET_SIZE) {
                debug(ctx, "There are more packets to be processed. Continuing. (Remaining Bytes: {}, Collected Packets: {})", in.readableBytes(), packets.size());
                return;
            }

            //Do we have packets to flush?
            if (!packets.isEmpty()) {
                debug(ctx, "Flushing {} packet(s) to the out buffer (Packet Size: {}, Minimum Required Packet Size: {}, Remaining Bytes: {})", packets.size(), packetSize, MINIMUM_PACKET_SIZE, in.readableBytes());
                //start flushing
                flush(ctx, out);
            } else {
                debug(ctx, "No packets to flush");
            }
            return;
        }

        debug(ctx, "Packet size is {} (Remaining bytes: {})", packetSize, in.readableBytes());

        //Do we have the required number of bytes to process one complete packet? (packet size included)
        if (in.readableBytes() < (packetSize + PACKET_SIZE_LENGTH)) {
            debug(ctx, "Not enough readable bytes available to process this packet (Readable Bytes: {}, Expected packet size: {}, Collected packets: {})", in.readableBytes(), packetSize, packets.size());
            //log.info("Not enough readable bytes available to process this packet (Readable Bytes: {}, Expected packet size: {}, Collected packets: {})", in.readableBytes(), packetSize + PACKET_SIZE_LENGTH, packets.size());
            return;
        }

        //log.info("Got enough readable bytes. Start Decoding. (Readable Bytes: {}, Packet Size: {})", in.readableBytes(), packetSize);
        debug(ctx, "Got enough readable bytes. Start Decoding. (Readable Bytes: {}, Packet Size: {})", in.readableBytes(), packetSize);

        try {
            //Start decoding
            SourceRconPacket decoded = decoder.decode(in);
            if (decoded == null) {
                debug(ctx, "Decoder returned null");
                return;
            }

            //make sure we don't have a mismatch
            checkValidPacket(request, decoded);

            if (decoded.getId() == 0) {
                out.add(decoded);
                return;
            }

            //Ok, no idea what packet id 0 supposed to mean, but we need to flush this and move on to the next.
            /*if (packets.size() == 1 && packets.stream().anyMatch(p -> p.getId() == 0)) {
                debug(ctx, "Found packet with id = 0. Flushing");
                flush(ctx, out);
                return;
            }*/

            //Decoded packet is in a valid state
            debug(ctx, "Collecting decoded packet '{}' (Size: {})", decoded, packets.size());

            if (terminatorPacketsEnabled) {
                //Ignore secondary terminator packets
                if (!SourceRcon.isSecondaryTerminatorPacket(decoded)) {
                    //Decoded packet is in a valid state
                    debug(ctx, "Collecting decoded packet '{}' (Size: {})", decoded, packets.size());
                    packets.add(decoded);

                    //Do we have more bytes to read?
                    if (readMoreBytes || in.readableBytes() > 0) {
                        debug(ctx, "Continue reading. Found {} more bytes to read from the channel.", in.readableBytes());
                        return;
                    }

                    debug(ctx, "Done. No more bytes to read ({}). Collected a total of {} packet(s) from the server. Passing to the next handler", in.readableBytes(), packets.size());
                    flush(ctx, out);
                } else {
                    debug(ctx, "Discarding secondary terminator packets");
                    if (decoded.release()) {
                        debug(ctx, "Successfully released second terminator packet");
                    }
                }
            } else {
                flushExcept(decoded, out);

                packets.add(decoded);
                //Do we have more bytes to read?
                if (readMoreBytes || in.readableBytes() > 0) {
                    debug(ctx, "Continue reading. Found {} more bytes to read from the channel.", in.readableBytes());
                    return;
                }
                debug(ctx, "Done. No more bytes to read ({}). Collected a total of {} packet(s) from the server. Passing to the next handler", in.readableBytes(), packets.size());
                flush(ctx, out);
            }
        } catch (Exception ex) {
            log.error("Error occured while decoding packet", ex);
            reset(ctx, true);
            in.discardReadBytes();
            throw ex;
        }
    }

    private void flushExcept(SourceRconPacket packet, List<Object> out) {
        List<Integer> ids = packets.stream().map(SourceRconPacket::getId).filter(id -> id != -1 && id != packet.getId()).distinct().collect(Collectors.toList());
        if (ids.isEmpty())
            return;
        Iterator<SourceRconPacket> it = packets.iterator();
        while (it.hasNext()) {
            SourceRconPacket pending = it.next();
            if (pending.getId() != packet.getId()) {
                log.debug("Flushing '{}' except '{}'", pending, packet);
                out.add(pending);
                it.remove();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        try {
            reset(ctx, true);
        } finally {
            ctx.fireExceptionCaught(cause);
        }
    }

    private void reset(ChannelHandlerContext ctx, boolean release) {
        if (packets == null)
            return;
        if (!packets.isEmpty()) {
            debug(ctx, "Resetting/Releasing decoded packet(s) (Size: {})", packets.size());
            if (release)
                packets.forEach(DefaultByteBufHolder::release);
        }
        packets = null;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        ChannelEvent cEvent = (ChannelEvent) evt;
        if (cEvent == ChannelEvent.RELEASED) {
            if (packets == null)
                return;
            debug(ctx, "Channel was released. Resetting packet container (Container size: {})", packets.size());
            reset(ctx, true);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        debug(ctx, "DECODER: START");
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        debug(ctx, "DECODER: END");
        super.channelReadComplete(ctx);
    }

    @Override
    public void channelActive(@NotNull ChannelHandlerContext ctx) throws Exception {
        this.decoder = new com.ibasco.agql.protocols.valve.source.query.packets.SourceRconPacketDecoder(ctx, SourceRconOptions.STRICT_MODE.attr(ctx));
        this.terminatorPacketsEnabled = SourceRcon.terminatorPacketEnabled(ctx);
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        reset(ctx, true);
        decoder = null;
        super.channelInactive(ctx);
    }

    private boolean allMatch(SourceRconPacket packet) {
        for (int i = 0; i < packets.size(); i++) {
            SourceRconPacket p = packets.get(i);
            if (p.getId() != packet.getId())
                return false;
        }
        return true;
    }

    private void checkValidPacket(SourceRconRequest request, SourceRconPacket packet) {
        if (packets.isEmpty())
            return;
        if (SourceRcon.isTerminatorPacket(packet))
            return;
        /*if (packets.stream().allMatch(p -> p.getId() == packet.getId()))
            return;*/
        if (allMatch(packet))
            return;
        if (packet.getId() == 0)
            return;
        if (request.getRequestId() != packet.getId())
            throw new IllegalStateException(String.format("Packet id mismatch. Expected packet id of '%d' but got '%d' (Request: %s)", request.getRequestId(), packet.getId(), request));
        List<Integer> ids = packets.stream().map(SourceRconPacket::getId).filter(id -> id != -1).distinct().collect(Collectors.toList());
        int distinctIds = ids.size();
        if (distinctIds > 1) {
            throw new IllegalStateException(String.format("Packet id mismatch. Expected only 1 distinct packet id in the container but have %d (Ids: [%s], Packet Id: %s, Request: %s)", distinctIds, ids.stream().map(String::valueOf).collect(Collectors.joining(", ")), packet.getId(), request));
        }
    }

    private void flush(ChannelHandlerContext ctx, List<Object> out) {
        if (packets.isEmpty()) {
            debug(ctx, "Nothing to flush");
            return;
        }
        out.addAll(packets);
        reset(ctx, false);
    }

    private static void debug(ChannelHandlerContext ctx, String msg, Object... args) {
        log.debug(String.format("%s INB => %s", NettyUtil.id(ctx), msg), args);
    }
}
