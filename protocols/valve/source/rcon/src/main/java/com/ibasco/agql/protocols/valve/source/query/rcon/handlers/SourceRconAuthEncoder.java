/*
 * Copyright (c) 2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.protocols.valve.source.query.rcon.handlers;

import com.ibasco.agql.core.Envelope;
import com.ibasco.agql.core.transport.handlers.MessageOutboundEncoder;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconAuthRequest;
import com.ibasco.agql.protocols.valve.source.query.rcon.packets.SourceRconPacketFactory;
import io.netty.channel.ChannelHandlerContext;
import java.util.List;

/**
 * <p>SourceRconAuthEncoder class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SourceRconAuthEncoder extends MessageOutboundEncoder<SourceRconAuthRequest> {

    /** {@inheritDoc} */
    @Override
    protected void encodeMessage(ChannelHandlerContext ctx, Envelope<SourceRconAuthRequest> msg, List<Object> out) throws Exception {
        final SourceRconAuthRequest request = msg.content();
        debug("Encoding rcon AUTH request: {}", request);
        out.add(SourceRconPacketFactory.createAuth(request.getRequestId(), request.getPassword()));
    }

    /** {@inheritDoc} */
    @Override
    protected boolean acceptMessage(Class<SourceRconAuthRequest> requestClass, Envelope<SourceRconAuthRequest> envelope) throws Exception {
        return SourceRconAuthRequest.class.equals(requestClass);
    }
}
