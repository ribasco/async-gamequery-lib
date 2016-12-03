package com.ibasco.agql.protocols.valve.source.query.handlers;

import com.ibasco.agql.protocols.valve.source.query.SourceRconResponse;
import com.ibasco.agql.protocols.valve.source.query.SourceRconResponsePacket;
import com.ibasco.agql.protocols.valve.source.query.exceptions.SourceRconExecutionException;
import com.ibasco.agql.protocols.valve.source.query.packets.response.SourceRconAuthResponsePacket;
import com.ibasco.agql.protocols.valve.source.query.packets.response.SourceRconCmdResponsePacket;
import com.ibasco.agql.protocols.valve.source.query.response.SourceRconAuthResponse;
import com.ibasco.agql.protocols.valve.source.query.response.SourceRconCmdResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.function.BiConsumer;

public class SourceRconResponseRouter extends SimpleChannelInboundHandler<SourceRconResponsePacket> {

    private static final Logger log = LoggerFactory.getLogger(SourceRconResponseRouter.class);

    private BiConsumer<SourceRconResponse, Throwable> responseCallback;

    public SourceRconResponseRouter(BiConsumer<SourceRconResponse, Throwable> responseCallback) {
        this.responseCallback = responseCallback;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SourceRconResponsePacket packet) throws Exception {
        SourceRconResponse response = null;

        //Create a response object compliant to the messenger
        if (packet instanceof SourceRconAuthResponsePacket) {
            response = new SourceRconAuthResponse();
        } else if (packet instanceof SourceRconCmdResponsePacket) {
            response = new SourceRconCmdResponse();
        }

        if (response != null) {
            response.setSender((InetSocketAddress) ctx.channel().remoteAddress());
            response.setRecipient((InetSocketAddress) ctx.channel().localAddress());
            response.setRequestId(packet.getId());
            response.setResponsePacket(packet);
            log.debug("Response Processed. Sending back to the messenger : '{}={}'", response.getClass().getSimpleName(), response.sender());
            responseCallback.accept(response, null);
        } else
            responseCallback.accept(null, new SourceRconExecutionException("Invalid response received"));
    }
}
