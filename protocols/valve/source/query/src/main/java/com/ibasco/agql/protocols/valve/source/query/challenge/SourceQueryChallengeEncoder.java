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

package com.ibasco.agql.protocols.valve.source.query.challenge;

import com.ibasco.agql.core.Envelope;
import com.ibasco.agql.protocols.valve.source.query.SourceQuery;
import com.ibasco.agql.protocols.valve.source.query.common.handlers.SourceQueryEncoder;
import com.ibasco.agql.protocols.valve.source.query.common.message.SourceQueryRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import java.util.List;

/**
 * <p>SourceQueryChallengeEncoder class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SourceQueryChallengeEncoder extends SourceQueryEncoder<SourceQueryChallengeRequest> {

    /** {@inheritDoc} */
    @Override
    protected void encodeQueryRequest(ChannelHandlerContext ctx, Envelope<SourceQueryChallengeRequest> msg, List<Object> out) throws Exception {
        SourceQueryChallengeRequest request = msg.content();
        ByteBuf buf = ctx.alloc().directBuffer(5);
        buf.writeIntLE(SourceQuery.SOURCE_PACKET_TYPE_SINGLE);
        buf.writeByte(SourceQuery.SOURCE_QUERY_CHALLENGE_REQ);
        out.add(new DatagramPacket(buf, msg.recipient()));
    }

    /** {@inheritDoc} */
    @Override
    protected boolean acceptQueryRequest(Class<? extends SourceQueryRequest> cls, Envelope<SourceQueryRequest> envelope) {
        return SourceQueryChallengeRequest.class.equals(cls);
    }
}
