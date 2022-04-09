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

package com.ibasco.agql.protocols.valve.source.query.packets;

import com.ibasco.agql.core.util.Bytes;
import com.ibasco.agql.protocols.valve.source.query.SourceQuery;
import io.netty.buffer.ByteBuf;

import java.nio.ByteOrder;

/**
 * A single-type {@link SourceQueryPacket}
 *
 * @author Rafael Luis Ibasco
 */
public class SourceQuerySinglePacket extends SourceQueryPacket {

    private int header;

    public SourceQuerySinglePacket(ByteBuf payload) {
        super(SourceQuery.SOURCE_PACKET_TYPE_SINGLE, payload);
    }

    public final int getHeader() {
        return header;
    }

    public final void setHeader(int header) {
        this.header = header;
    }

    @Override
    public String toString() {
        return String.format("'%s' Header: %s (%d)", getClass().getSimpleName(), Bytes.toHexString(header, ByteOrder.LITTLE_ENDIAN), header);
    }
}
