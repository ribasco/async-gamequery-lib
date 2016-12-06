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

import com.google.common.collect.LinkedListMultimap;
import com.ibasco.agql.protocols.mojang.minecraft.query.McRconResponsePacket;
import com.ibasco.agql.protocols.mojang.minecraft.query.enums.McRconRequestType;
import com.ibasco.agql.protocols.mojang.minecraft.query.enums.McRconResponseType;
import com.ibasco.agql.protocols.mojang.minecraft.query.exceptions.InvalidRconRequestIdException;
import com.ibasco.agql.protocols.mojang.minecraft.query.exceptions.McRconNoResponseTypeException;
import com.ibasco.agql.protocols.mojang.minecraft.query.packets.response.McRconAuthResponsePacket;
import com.ibasco.agql.protocols.mojang.minecraft.query.packets.response.McRconCmdResponsePacket;
import com.ibasco.agql.protocols.mojang.minecraft.query.utils.McRconUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Class responsible for re-assembling split packet instances into one complete packet.
 *
 * @see <a href="https://developer.valvesoftware.com/wiki/Mc_RCON_Protocol#Multiple-packet_Responses">Multiple-packet_Responses</a>
 */
public class McRconPacketAssembler extends MessageToMessageDecoder<McRconResponsePacket> {

    private static final Logger log = LoggerFactory.getLogger(McRconPacketAssembler.class);

    private Map<Integer, McRconRequestType> requestTypeMap;

    private LinkedListMultimap<Integer, McRconResponsePacket> packetContainer = LinkedListMultimap.create();

    private Integer lastAuthRequestId = null;

    public McRconPacketAssembler(Map<Integer, McRconRequestType> requestTypeMap) {
        if (requestTypeMap == null)
            throw new IllegalArgumentException("Request type map cannot be null");
        this.requestTypeMap = requestTypeMap;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, McRconResponsePacket msg, List<Object> out) throws Exception {
        int requestId = msg.getId();

        McRconResponseType responseType = getResponseType(msg);

        //Fail fast for unknown response types
        if (responseType == null)
            throw new McRconNoResponseTypeException("Unable to identify response type for rcon packet");

        log.debug("Received rcon packet from Request Id : {} (Type = {}, Class = {})", requestId, responseType, msg.getClass().getSimpleName());

        //Check if we receive an auth response type
        if (responseType == McRconResponseType.AUTH) {
            McRconAuthResponsePacket authResponsePacket = processAuthResponsePacket(msg);
            if (authResponsePacket != null)
                out.add(authResponsePacket);
        } else if (responseType == McRconResponseType.COMMAND) {
            //Put to the packet container
            packetContainer.put(requestId, msg);
        } else {
            //Process the first key
            int rId = packetContainer.keys().iterator().next();

            log.debug("Received a terminator packet. Re-assembling packet(s) for request id '{}'", rId);
            List<McRconResponsePacket> rconPackets = packetContainer.get(rId);
            McRconResponsePacket reassembledPacket = null;

            if (rconPackets.size() == 1) {
                reassembledPacket = rconPackets.remove(0);
            } else if (rconPackets.size() > 1) {
                log.debug("Found multiple packetContainer in the queue. Re-assembling");
                reassembledPacket = reassemblePackets(rconPackets);
            }
            if (reassembledPacket != null) {
                log.debug("Sending Re-assembled packet to the next handler");
                //Send to the next handler
                out.add(reassembledPacket);
            }
        }
    }

    private McRconAuthResponsePacket processAuthResponsePacket(McRconResponsePacket msg) {
        int requestId = msg.getId();

        //We received a new auth cmd response, so create a new McRconAuthResponsePacket and merge with it
        if ((msg instanceof McRconCmdResponsePacket) && !this.packetContainer.containsKey(requestId)) {
            if (!McRconUtil.isValidRequestId(requestId))
                throw new IllegalStateException(String.format("Expecting a valid request id. Received : %d", requestId));

            //Create a new auth response container
            McRconAuthResponsePacket authPacket = new McRconAuthResponsePacket();
            authPacket.setId(requestId);
            authPacket.setBody(StringUtils.strip(msg.getBody(), "\r\n "));

            //Place it into the multimap container
            this.packetContainer.put(requestId, authPacket);

            //Store the request id to lastAuthRequestId. If the authentication fails,
            // we will only receive a request id value of -1 from the auth response packet.
            this.lastAuthRequestId = requestId;
            log.debug("Saving the auth request id : {}", lastAuthRequestId);
        } else {
            log.debug("Received AUTH response packet. Current Request Id = {}, Last Request Id = {}", requestId, lastAuthRequestId);
            if (lastAuthRequestId == null && msg instanceof McRconAuthResponsePacket) {
                //some games only respond with the AUTH packet, immediately return for processing.

                //if id is -1, look for the latest entry in the map
                if (msg.getId() == -1) {
                    Integer id = findLatestAuthId();
                    log.debug("Updating id from -1 to '{}'", id);
                    msg.setId(id != null ? id : -1);
                    ((McRconAuthResponsePacket) msg).setSuccess(false);
                } else {
                    ((McRconAuthResponsePacket) msg).setSuccess(true);
                }

                log.debug("Returning authentication packet response for processing");
                return (McRconAuthResponsePacket) msg;
            } else if (lastAuthRequestId != null && McRconUtil.isValidRequestId(lastAuthRequestId)) {
                List<McRconResponsePacket> authPacketContainer = this.packetContainer.get(lastAuthRequestId);
                Iterator<McRconResponsePacket> it = authPacketContainer.iterator();
                try {
                    //Update the auth response instance with the actual request id
                    McRconAuthResponsePacket authResponsePacket = (McRconAuthResponsePacket) it.next();
                    if (!McRconUtil.isValidRequestId(requestId)) {
                        log.debug("Updating request id to : {}", lastAuthRequestId);
                        authResponsePacket.setId(lastAuthRequestId); //update the request id
                    }
                    authResponsePacket.setSuccess(requestId != -1);
                    return authResponsePacket;
                } finally {
                    //send to the next handler
                    it.remove(); //remove from the container
                    lastAuthRequestId = null; //reset id
                }
            } else
                throw new InvalidRconRequestIdException("Unable to retrieve the authentication request id. Expected a valid request id but received: " + lastAuthRequestId);
        }
        return null;
    }

    private Integer findLatestAuthId() {
        if (requestTypeMap != null) {
            return requestTypeMap.entrySet().stream()
                    .filter(e -> e.getValue() == McRconRequestType.AUTH)
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    /**
     * <p>Determine the response type of the message.</p>
     *
     * @param response
     *         The {@link McRconResponsePacket} message to check
     *
     * @return A {@link McRconResponseType} enum
     */
    private McRconResponseType getResponseType(McRconResponsePacket response) {
        McRconRequestType requestType = requestTypeMap.get(response.getId());
        if (requestType == null && McRconUtil.isTerminator(response.getId())) {
            return McRconResponseType.TERMINATOR;
        } else if ((response.getId() == -1 && response instanceof McRconAuthResponsePacket)
                || requestType == McRconRequestType.AUTH) {
            return McRconResponseType.AUTH;
        } else if (requestType == McRconRequestType.COMMAND) {
            return McRconResponseType.COMMAND;
        }
        return null;
    }

    private McRconResponsePacket reassemblePackets(List<McRconResponsePacket> packets) {
        //We have reached the end...lets start assembling the packet
        log.debug("Received a terminator packet! Re-assembling packetContainer. Size = {}", packets.size());
        McRconCmdResponsePacket reassembledPacket = new McRconCmdResponsePacket();
        StringBuilder responseBody = new StringBuilder();

        int id = -1, type = -1, totalDeclaredPacketSize = 0, totalActualBodySize = 0, totalActualPacketSize = 0;

        int totalSplitPackets = packets.size();

        Iterator<McRconResponsePacket> it = packets.iterator();

        for (int i = 0; packets.size() > 0; i++) {

            McRconResponsePacket responsePacket = it.next();

            try {
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
            } finally {
                it.remove();
            }
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
