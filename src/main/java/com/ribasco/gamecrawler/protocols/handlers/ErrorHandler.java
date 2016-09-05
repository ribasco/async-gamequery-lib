package com.ribasco.gamecrawler.protocols.handlers;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.ReadTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by raffy on 9/1/2016.
 */
@ChannelHandler.Sharable
public class ErrorHandler extends ChannelDuplexHandler {

    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (!(cause instanceof ReadTimeoutException))
            log.error("Error Occured within the pipeline. Signalling all handlers shutdown will occur", cause);
        else {
            log.info("No Data Received. Shutting Down");
            ctx.close();
        }
    }
}
