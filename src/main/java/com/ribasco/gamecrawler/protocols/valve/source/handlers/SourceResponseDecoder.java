package com.ribasco.gamecrawler.protocols.valve.source.handlers;

import com.ribasco.gamecrawler.protocols.valve.source.SourcePacketHelper;
import com.ribasco.gamecrawler.protocols.valve.source.SourceResponsePacket;
import com.ribasco.gamecrawler.protocols.valve.source.SourceResponseWrapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Responsible for handling datagram packet responses and decode
 */
public class SourceResponseDecoder extends MessageToMessageDecoder<DatagramPacket> {

    private static final Logger log = LoggerFactory.getLogger(SourceResponseDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) throws Exception {
        //Is this a valid header for the Server Info GameResponse?
        if (!SourcePacketHelper.isValidResponsePacket(msg.content())) {
            log.warn("No valid handlers found for this packet. Passing to the next channel handler in the pipeline.");
            ctx.fireChannelRead(msg.retain());
            return;
        }

        //At this point, the buffer SHOULD ALWAYS start from the response header. Not from the protocol header.
        SourceResponsePacket responsePacket = SourcePacketHelper.getResponsePacket(msg.content());

        //Wrap the object in an GameResponse class
        out.add(new SourceResponseWrapper(responsePacket, msg.sender()));
    }
}
