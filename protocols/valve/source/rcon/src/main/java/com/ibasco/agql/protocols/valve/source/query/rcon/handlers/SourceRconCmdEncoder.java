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
import com.ibasco.agql.protocols.valve.source.query.rcon.SourceRcon;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconCmdRequest;
import com.ibasco.agql.protocols.valve.source.query.rcon.packets.SourceRconPacketFactory;
import io.netty.channel.ChannelHandlerContext;
import java.util.List;

/**
 * <p>SourceRconCmdEncoder class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SourceRconCmdEncoder extends MessageOutboundEncoder<SourceRconCmdRequest> {

    /** {@inheritDoc} */
    @Override
    protected void encodeMessage(ChannelHandlerContext ctx, Envelope<SourceRconCmdRequest> msg, List<Object> out) throws Exception {
        boolean useTerminatorPacket = SourceRcon.terminatorPacketEnabled(ctx); //ctx.channel().attr(SourceRconOptions.USE_TERMINATOR_PACKET.toAttributeKey()).getConnectionStats();
        SourceRconCmdRequest request = msg.content();
        debug("Encoding rcon COMMAND request (Id: {}, Command: {})", request.getRequestId(), request.getCommand());
        out.add(SourceRconPacketFactory.createCommand(request.getRequestId(), request.getCommand()));
        if (useTerminatorPacket) {
            debug("Terminator packet included on request");
            out.add(SourceRconPacketFactory.createTerminator());
        } else {
            debug("Terminator packet excluded from request (Reason: Explicitly disabled from configutation)");
        }
    }
}
