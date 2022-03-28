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

package com.ibasco.agql.protocols.valve.steam.master.handlers;

import com.ibasco.agql.core.Envelope;
import com.ibasco.agql.core.transport.handlers.MessageOutboundEncoder;
import com.ibasco.agql.protocols.valve.steam.master.MasterServer;
import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerRegion;
import com.ibasco.agql.protocols.valve.steam.master.message.MasterServerRequest;
import com.ibasco.agql.protocols.valve.steam.master.packets.MasterServerQueryPacket;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public class MasterServerRequestEncoder extends MessageOutboundEncoder<MasterServerRequest> {

    @Override
    protected boolean acceptMessage(Class<MasterServerRequest> requestClass, Envelope<MasterServerRequest> envelope) throws Exception {
        return MasterServerRequest.class.equals(requestClass);
    }

    @Override
    protected void encodeMessage(ChannelHandlerContext ctx, Envelope<MasterServerRequest> msg, List<Object> out) throws Exception {
        MasterServerRequest request = msg.content();
        MasterServerQueryPacket packet = new MasterServerQueryPacket();

        if (request.getRegion() == null)
            request.setRegion(MasterServerRegion.REGION_ALL);

        String address = request.getAddress() == null ? MasterServer.INITIAL_IP : request.getAddress();
        debug("Sending MASTER REQUEST with address '{}'", address);
        packet.setType(MasterServer.SOURCE_MASTER_TYPE);
        packet.setRegion(request.getRegion().getHeader());
        packet.setAddress(address);
        packet.setFilter(request.getFilter().toString());
        out.add(packet);
    }
}
