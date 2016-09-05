package com.ribasco.gamecrawler.protocols.valve.source.handlers;

import com.ribasco.gamecrawler.protocols.GameRequest;
import com.ribasco.gamecrawler.protocols.GameRequestEnvelope;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * <p>Translate Game Queries to the underlying protocol implementation for transport</p>
 */
public class SourceRequestEncoder<T extends GameRequest> extends MessageToMessageEncoder<GameRequestEnvelope<T>> {
    private static final Logger log = LoggerFactory.getLogger(SourceRequestEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, GameRequestEnvelope<T> msg, List<Object> out) throws Exception {
        //Unpack the contents of the message
        InetSocketAddress destination = msg.getAddress();
        GameRequest query = msg.getRequest();

        //Translate the content to it's intended form
        byte[] data = query.getBytes();

        //Pack the message into a buffer (message wrapper)
        ByteBuf buffer = ctx.alloc().buffer(data.length);
        buffer.writeBytes(data);

        //Since game servers use the UDP protocol, we wrap the buffer to a DatagramPacket class
        log.debug("Sending {} to {}:{}", query.getClass().getSimpleName(), destination.getAddress().getHostAddress(), destination.getPort());

        if (log.isDebugEnabled()) {
            System.out.println("REQUEST DATA: ");
            System.out.println(ByteBufUtil.prettyHexDump(buffer));
        }

        out.add(new DatagramPacket(buffer, destination));
    }
}
