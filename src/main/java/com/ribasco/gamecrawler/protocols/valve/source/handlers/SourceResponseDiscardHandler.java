package com.ribasco.gamecrawler.protocols.valve.source.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Verify if response are a valid source/steam packet.
 */
public class SourceResponseDiscardHandler extends ChannelInboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(SourceResponseDiscardHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //Make sure we are only receiving an instance of DatagramPacket
        if (!(msg instanceof DatagramPacket)) {
            ReferenceCountUtil.release(msg);
            return;
        }

        //Retrived the packet instance
        DatagramPacket packet = (DatagramPacket) msg;

        if (acceptMessage(packet)) {
            //Pass the message to the succeeding handlers
            ctx.fireChannelRead(packet.retain());
        } else {
            log.warn("Not a valid message. Discarding");

            //Release the message
            ReferenceCountUtil.release(msg);
        }
    }

    /**
     * Verifies if the message is a valid source message
     *
     * @param msg
     * @return
     */
    public boolean acceptMessage(DatagramPacket msg) {
        //Check if its an instance of DatagramPacket
        ByteBuf buf = msg.content();
        //Verify Length
        if (buf.readableBytes() >= 5) {
            if (buf.readInt() == -1)
                return true;
        }

        return false;
    }
}
