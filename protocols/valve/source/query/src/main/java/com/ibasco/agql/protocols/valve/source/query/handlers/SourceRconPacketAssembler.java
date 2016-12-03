package com.ibasco.agql.protocols.valve.source.query.handlers;

import com.ibasco.agql.protocols.valve.source.query.SourceRconResponsePacket;
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

/**
 * Class responsible for re-assembling split packet instances into one complete packet.
 *
 * @see <a href="https://developer.valvesoftware.com/wiki/Source_RCON_Protocol#Multiple-packet_Responses">Multiple-packet_Responses</a>
 */
public class SourceRconPacketAssembler extends MessageToMessageDecoder<SourceRconResponsePacket> {

    private static final Logger log = LoggerFactory.getLogger(SourceRconPacketAssembler.class);

    private LinkedList<SourceRconResponsePacket> packetContainer = new LinkedList<>();

    @Override
    protected void decode(ChannelHandlerContext ctx, SourceRconResponsePacket msg, List<Object> out) throws Exception {
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
                    reassembledPacket = reassemblePackets();
                }
                if (reassembledPacket != null) {
                    log.debug("Re-assembly Complete! Sending to the next handler");
                    log.debug(" # Size: {}", reassembledPacket.getSize());
                    log.debug(" # Request Id: {}", reassembledPacket.getId());
                    log.debug(" # Type: {}", reassembledPacket.getType());
                    log.debug(" # Body Size: {}", reassembledPacket.getBody().length());
                    //Send to the next handler
                    out.add(reassembledPacket);
                }
            } else {
                //Ignore empty responses
                if (StringUtils.isBlank(msg.getBody())) {
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

        int totalPackets = packetContainer.size();

        SourceRconCmdResponsePacket reassembledPacket = new SourceRconCmdResponsePacket();

        StringBuilder responseBody = new StringBuilder();

        int bodySize = 0;
        int id = -1;
        int type = -1;

        for (int i = 0; i < totalPackets; i++) {
            SourceRconResponsePacket responsePacket = packetContainer.poll();

            if (responsePacket == null)
                continue;

            //Initialize Variables
            if (id == -1) {
                id = responsePacket.getId();
            }
            if (type == -1) {
                type = responsePacket.getType();
            }

            //Compute total body size
            bodySize += responsePacket.getBody().length();

            log.debug(" ({}) Re-assembling Packet: {}", i + 1, responsePacket);
            responseBody.append(responsePacket.getBody());
        }

        //Merge the details
        reassembledPacket.setSize(8 + bodySize + 2); //id(4) + type(4) + body + body terminator (1) + packet terminator (1)
        reassembledPacket.setId(id);
        reassembledPacket.setType(type);
        reassembledPacket.setBody(responseBody.toString());

        return reassembledPacket;
    }
}
