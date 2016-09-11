/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Ibasco
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **************************************************************************************************/

package com.ribasco.gamecrawler.protocols.valve.server.handlers;

import com.ribasco.gamecrawler.protocols.DefaultResponseWrapper;
import com.ribasco.gamecrawler.protocols.Session;
import com.ribasco.gamecrawler.protocols.SessionInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Responsible for notifying listeners everytime we receive a valid and processed response
 */
public class SourceResponseHandler extends SimpleChannelInboundHandler<DefaultResponseWrapper> {
    private static final Logger log = LoggerFactory.getLogger(SourceResponseHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DefaultResponseWrapper response) throws Exception {
        //Retrieve the session id associated with this response
        String sessionId = response.getSessionId();

        //Retrieve the session information for the response
        SessionInfo sessionInfo = Session.getSessionInfo(sessionId);

        //Retrieve the promise associated with this request
        Promise<Object> promise = sessionInfo.getPromise();

        if (promise == null) {
            //Do nothing here and just return, let the task monitor cancel the request
            log.error("No promise was assigned to request '{}' after retrieval", sessionId);
            return;
        }

        //Retrieve the decoded response
        Object responseObj = response.getResponseData();

        //Mark as success
        promise.setSuccess(responseObj);

        //Remove the request from the registry
        Session.getInstance().unregister(sessionId);

        log.debug("Successfully Processed '{}' (Current Size of Registry: {})", sessionId, Session.getInstance().getTotalRequests());
    }
}