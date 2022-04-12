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

package com.ibasco.agql.protocols.valve.source.query.info;

import com.ibasco.agql.protocols.valve.source.query.SourceQuery;
import com.ibasco.agql.protocols.valve.source.query.common.handlers.SourceQueryAuthEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * Encodes a {@link com.ibasco.agql.protocols.valve.source.query.info.SourceQueryInfoRequest} to it's datagram packet form
 *
 * @author Rafael Luis Ibasco
 */
public class SourceQueryInfoEncoder extends SourceQueryAuthEncoder<SourceQueryInfoRequest> {

    /**
     * <p>Constructor for SourceQueryInfoEncoder.</p>
     */
    public SourceQueryInfoEncoder() {
        super(SourceQueryInfoRequest.class, SourceQuery.SOURCE_QUERY_INFO_REQ, 1400);
    }

    /** {@inheritDoc} */
    @Override
    protected void encodeChallenge(ChannelHandlerContext ctx, SourceQueryInfoRequest request, ByteBuf payload) {
        payload.writeBytes(SourceQuery.SOURCE_QUERY_INFO_PAYLOAD.getBytes());
        //try bypass server challenge?
        if (!request.isBypassChallenge()) {
            if (request.getChallenge() != null)
                payload.writeIntLE(request.getChallenge());
        } else {
            payload.writerIndex(payload.capacity());
        }
    }
}
