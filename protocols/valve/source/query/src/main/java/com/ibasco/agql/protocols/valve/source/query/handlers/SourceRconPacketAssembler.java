/*
 * MIT License
 *
 * Copyright (c) 2016 Asynchronous Game Query Library
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

package com.ibasco.agql.protocols.valve.source.query.handlers;

import com.ibasco.agql.protocols.valve.source.query.SourceRconResponsePacket;
import com.ibasco.agql.protocols.valve.source.query.enums.SourceRconRequestType;
import com.ibasco.agql.protocols.valve.source.query.packets.request.SourceRconTermRequestPacket;
import com.ibasco.agql.protocols.valve.source.query.packets.response.SourceRconAuthResponsePacket;
import com.ibasco.agql.protocols.valve.source.query.packets.response.SourceRconCmdResponsePacket;
import com.ibasco.agql.protocols.valve.source.query.packets.response.SourceRconTermResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Class responsible for re-assembling split packet instances into one complete packet.
 *
 * @see <a href="https://developer.valvesoftware.com/wiki/Source_RCON_Protocol#Multiple-packet_Responses">Multiple-packet_Responses</a>
 */
public class SourceRconPacketAssembler extends MessageToMessageDecoder<SourceRconResponsePacket> {

    private static final Logger log = LoggerFactory.getLogger(SourceRconPacketAssembler.class);

    private Map<Integer, SourceRconRequestType> requestTypeMap;

    private LinkedList<SourceRconResponsePacket> packetContainer = new LinkedList<>();

    public SourceRconPacketAssembler(Map<Integer, SourceRconRequestType> requestTypeMap) {
        if (requestTypeMap == null)
            throw new IllegalArgumentException("Request type map cannot be null");
        this.requestTypeMap = requestTypeMap;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, SourceRconResponsePacket msg, List<Object> out) throws Exception {

        int requestId = msg.getId();

        log.debug("{} received rcon packet with request id : {}", this.getClass().getSimpleName(), requestId);

        SourceRconRequestType type = requestTypeMap.get(requestId);

        log.debug("Got Request Type {} for Id {}", type, requestId);

        switch (type) {
            case AUTH:
                log.debug("Received an AUTH type packet. Packet Class = {}", msg.getClass().getSimpleName());
                break;
            case RESPONSE:
                break;
            default:
                break;
        }

        if (msg instanceof SourceRconAuthResponsePacket) {
            //automatically forward all auth responses to the next handler
            log.debug("Forwarding authentication response to next handler");
            out.add(msg);
        } else {
            //Should be a type of response (either a terminator or a valid response packet)
            if (msg instanceof SourceRconTermResponsePacket) {
                SourceRconResponsePacket reassembledPacket = null;
                if (packetContainer.size() == 1) {
                    reassembledPacket = packetContainer.poll();
                } else if (packetContainer.size() > 1) {
                    log.debug("Found multiple packets in the queue. Re-assembling");
                    reassembledPacket = reassemblePackets();
                }
                if (reassembledPacket != null) {
                    log.debug("Sending Re-assembled packet to the next handler");
                    //Send to the next handler
                    out.add(reassembledPacket);
                }
            } else {
                //Ignore empty responses
                if (StringUtils.isBlank(msg.getBody()) && msg.getId() == SourceRconTermRequestPacket.TERMINATOR_REQUEST_ID) {
                    log.debug("Ignoring empty response packet : {}", msg);
                    return;
                }
                log.debug("Adding response packet to the queue");
                packetContainer.add(msg);
            }
        }
    }

    private SourceRconResponsePacket reassemblePackets() {
        //We have reached the end...lets start assembling the packet
        log.debug("Received a terminator packet! Re-assembling packets. Size = {}", packetContainer.size());
        SourceRconCmdResponsePacket reassembledPacket = new SourceRconCmdResponsePacket();
        StringBuilder responseBody = new StringBuilder();

        int id = -1, type = -1, totalDeclaredPacketSize = 0, totalActualBodySize = 0, totalActualPacketSize = 0;

        int totalSplitPackets = packetContainer.size();

        for (int i = 0; packetContainer.size() > 0; i++) {

            SourceRconResponsePacket responsePacket = packetContainer.poll();

            if (responsePacket == null)
                continue;

            int packetBodySize = responsePacket.getBody().length();

            //Initialize Variables
            if (id == -1)
                id = responsePacket.getId();
            if (type == -1)
                type = responsePacket.getType();

            //Add the declared size for verification purposes
            totalDeclaredPacketSize += responsePacket.getSize();

            //Compute total body size
            totalActualBodySize += packetBodySize;

            //Compute the total actual packet size for integrity check
            totalActualPacketSize += (10 + packetBodySize); //4 bytes (Id) + 4 bytes (Type) + 2 null-terminator bytes (Size field is excluded)

            log.debug(" ({}) Re-assembling Packet: {}", i + 1, responsePacket);
            responseBody.append(responsePacket.getBody());
        }

        //Merge the details
        reassembledPacket.setSize(totalActualPacketSize);
        reassembledPacket.setId(id);
        reassembledPacket.setType(type);
        reassembledPacket.setBody(responseBody.toString());

        if (log.isDebugEnabled()) {
            String integrityStatus = (totalActualPacketSize == totalDeclaredPacketSize) ? "PASS" : "FAIL";
            log.debug("========================================================");
            log.debug(" Report Summary");
            log.debug("========================================================");
            log.debug(" # Total Split-Packets processed: {}", totalSplitPackets);
            log.debug(" # Total Declared Packet Size: {}", totalDeclaredPacketSize);
            log.debug(" # Total Actual Packet Size: {}", totalActualPacketSize);
            log.debug(" # Total Actual Body Size: {}", totalActualBodySize);
            log.debug(" # Integrity Check Status: {}", integrityStatus);
            log.debug(" # Size: {}", reassembledPacket.getSize());
            log.debug(" # Request Id: {}", reassembledPacket.getId());
            log.debug(" # Type: {}", reassembledPacket.getType());
            log.debug(" # Body Size: {}", reassembledPacket.getBody().length());
            log.debug("========================================================");
        }

        if (totalActualPacketSize != totalDeclaredPacketSize)
            log.warn("Failed packet re-assembly integrity check. Size Mismatch. Expected a total of '{}' byte(s) but got '{}' byte(s)", totalDeclaredPacketSize, totalActualPacketSize);

        return reassembledPacket;
    }
}
