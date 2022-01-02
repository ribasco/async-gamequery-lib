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

package com.ibasco.agql.protocols.valve.steam.master.packets;

import com.ibasco.agql.core.AbstractPacket;
import io.netty.buffer.ByteBuf;

import java.net.InetSocketAddress;

public class MasterServerAddressPacket extends AbstractPacket {

    public MasterServerAddressPacket(ByteBuf data) {
        super(data);
        assert data.capacity() == 6;
        assert data.readableBytes() == 6;
    }

    public final int getFirst() {
        return content().getUnsignedByte(0);
    }

    public final int getSecond() {
        return content().getUnsignedByte(1);
    }

    public final int getThird() {
        return content().getUnsignedByte(2);
    }

    public final int getFourth() {
        return content().getUnsignedByte(3);
    }

    public final int getPort() {
        return content().getUnsignedShort(4);
    }

    public final String getAddressString(boolean includePort) {
        if (includePort)
            return String.format("%d.%d.%d.%d:%d", getFirst(), getSecond(), getThird(), getFourth(), getPort());
        else
            return String.format("%d.%d.%d.%d", getFirst(), getSecond(), getThird(), getFourth());
    }

    public final InetSocketAddress getAddress() {
        return new InetSocketAddress(getAddressString(false), getPort());
    }
}
