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

import com.google.common.collect.LinkedListMultimap;
import com.ibasco.agql.protocols.valve.source.query.SourceRconResponsePacket;
import com.ibasco.agql.protocols.valve.source.query.enums.SourceRconRequestType;
import com.ibasco.agql.protocols.valve.source.query.enums.SourceRconResponseType;
import com.ibasco.agql.protocols.valve.source.query.exceptions.InvalidRconRequestIdException;
import com.ibasco.agql.protocols.valve.source.query.exceptions.SourceRconNoResponseTypeException;
import com.ibasco.agql.protocols.valve.source.query.packets.response.SourceRconAuthResponsePacket;
import com.ibasco.agql.protocols.valve.source.query.packets.response.SourceRconCmdResponsePacket;
import com.ibasco.agql.protocols.valve.source.query.utils.SourceRconUtil;
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
 * @see <a href="https://developer.valvesoftware.com/wiki/Source_RCON_Protocol#Multiple-packet_Responses">Multiple-packet_Responses</a>
 */
public class SourceRconPacketAssembler extends MessageToMessageDecoder<SourceRconResponsePacket> {

    private static final Logger log = LoggerFactory.getLogger(SourceRconPacketAssembler.class);

    private Map<Integer, SourceRconRequestType> requestTypeMap;

    private LinkedListMultimap<Integer, SourceRconResponsePacket> packetContainer = LinkedListMultimap.create();

    private Integer lastAuthRequestId = null;

    private boolean terminatingPacketsEnabled = true;

    public SourceRconPacketAssembler(Map<Integer, SourceRconRequestType> requestTypeMap, boolean terminatingPacketsEnabled) {
        if (requestTypeMap == null)
            throw new IllegalArgumentException("Request type map cannot be null");
        this.requestTypeMap = requestTypeMap;
        this.terminatingPacketsEnabled = terminatingPacketsEnabled;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, SourceRconResponsePacket msg, List<Object> out) throws Exception {
        int requestId = msg.getId();

        SourceRconResponseType responseType = getResponseType(msg);

        //Fail fast for unknown response types
        if (responseType == null)
            throw new SourceRconNoResponseTypeException("Unable to identify response type for rcon packet");

        log.debug("Received rcon packet from Request Id : {} (Type = {}, Class = {})", requestId, responseType, msg.getClass().getSimpleName());

        if (!terminatingPacketsEnabled && responseType != SourceRconResponseType.AUTH) {
            out.add(msg);
            return;
        }

        //Check if we receive an auth response type
        if (responseType == SourceRconResponseType.AUTH) {
            SourceRconAuthResponsePacket authResponsePacket = processAuthResponsePacket(msg);
            if (authResponsePacket != null)
                out.add(authResponsePacket);
        } else if (responseType == SourceRconResponseType.COMMAND) {
            //Put to the packet container
            packetContainer.put(requestId, msg);
        } else {
            //Process the first key
            int rId = packetContainer.keys().iterator().next();

            log.debug("Received a terminator packet. Re-assembling packet(s) for request id '{}'", rId);
            List<SourceRconResponsePacket> rconPackets = packetContainer.get(rId);
            SourceRconResponsePacket reassembledPacket = null;

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

    private SourceRconAuthResponsePacket processAuthResponsePacket(SourceRconResponsePacket msg) {
        int requestId = msg.getId();

        //We received a new auth cmd response, so create a new SourceRconAuthResponsePacket and merge with it
        if ((msg instanceof SourceRconCmdResponsePacket) && !this.packetContainer.containsKey(requestId)) {
            if (!SourceRconUtil.isValidRequestId(requestId))
                throw new IllegalStateException(String.format("Expecting a valid request id. Received : %d", requestId));

            //Create a new auth response container
            SourceRconAuthResponsePacket authPacket = new SourceRconAuthResponsePacket();
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
            if (lastAuthRequestId == null && msg instanceof SourceRconAuthResponsePacket) {
                //some games only respond with the AUTH packet, immediately return for processing.

                //if id is -1, look for the latest entry in the map
                if (msg.getId() == -1) {
                    Integer id = findLatestAuthId();
                    log.debug("Updating id from -1 to '{}'", id);
                    msg.setId(id != null ? id : -1);
                    ((SourceRconAuthResponsePacket) msg).setSuccess(false);
                } else {
                    ((SourceRconAuthResponsePacket) msg).setSuccess(true);
                }

                log.debug("Returning authentication packet response for processing");
                return (SourceRconAuthResponsePacket) msg;
            } else if (lastAuthRequestId != null && SourceRconUtil.isValidRequestId(lastAuthRequestId)) {
                List<SourceRconResponsePacket> authPacketContainer = this.packetContainer.get(lastAuthRequestId);
                Iterator<SourceRconResponsePacket> it = authPacketContainer.iterator();
                try {
                    //Update the auth response instance with the actual request id
                    SourceRconAuthResponsePacket authResponsePacket = (SourceRconAuthResponsePacket) it.next();
                    if (!SourceRconUtil.isValidRequestId(requestId)) {
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
                    .filter(e -> e.getValue() == SourceRconRequestType.AUTH)
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
     *         The {@link SourceRconResponsePacket} message to check
     *
     * @return A {@link SourceRconResponseType} enum
     */
    private SourceRconResponseType getResponseType(SourceRconResponsePacket response) {
        SourceRconRequestType requestType = requestTypeMap.get(response.getId());
        if (requestType == null && SourceRconUtil.isTerminator(response.getId())) {
            return SourceRconResponseType.TERMINATOR;
        } else if ((response.getId() == -1 && response instanceof SourceRconAuthResponsePacket)
                || requestType == SourceRconRequestType.AUTH) {
            return SourceRconResponseType.AUTH;
        } else if (requestType == SourceRconRequestType.COMMAND) {
            return SourceRconResponseType.COMMAND;
        }
        return null;
    }

    private SourceRconResponsePacket reassemblePackets(List<SourceRconResponsePacket> packets) {
        //We have reached the end...lets start assembling the packet
        log.debug("Received a terminator packet! Re-assembling packetContainer. Size = {}", packets.size());
        SourceRconCmdResponsePacket reassembledPacket = new SourceRconCmdResponsePacket();
        StringBuilder responseBody = new StringBuilder();

        int id = -1, type = -1, totalDeclaredPacketSize = 0, totalActualBodySize = 0, totalActualPacketSize = 0;

        int totalSplitPackets = packets.size();

        Iterator<SourceRconResponsePacket> it = packets.iterator();

        for (int i = 0; packets.size() > 0; i++) {

            SourceRconResponsePacket responsePacket = it.next();

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
