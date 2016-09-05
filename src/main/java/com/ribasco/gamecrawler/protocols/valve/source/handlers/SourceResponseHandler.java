package com.ribasco.gamecrawler.protocols.valve.source.handlers;

import com.ribasco.gamecrawler.protocols.Session;
import com.ribasco.gamecrawler.protocols.valve.source.SourceResponseWrapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Responsible for notifying listeners of each response event
 */
public class SourceResponseHandler extends SimpleChannelInboundHandler<SourceResponseWrapper> {
    private static final Logger log = LoggerFactory.getLogger(SourceResponseHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SourceResponseWrapper msg) throws Exception {
        //log.debug("From SourceResponseHandler: {}", msg.getResponseObject());

        //Retrieve the request map registry
        Map<String, Promise> registry = Session.getRegistry();

        //Get the request id (should be assigned by the check handler)
        String sessionId = msg.getSessionId();

        if (sessionId == null) {
            log.warn("Session Id not found for response = {}", msg);
            return;
        }

        //Retrieve the promise associated with this request
        Promise<Object> promise = registry.get(sessionId);

        if (promise == null) {
            log.warn("No promise was assigned to request '{}' after retrieval", sessionId);
            return;
        }

        Object responseObj = msg.getResponseObject();

        //Mark as success
        promise.setSuccess(responseObj);

        //Remove the request from the registry
        registry.remove(sessionId);

        log.debug("Processed '{}' Current Size of Registry: {}", sessionId, registry.size());
    }
}