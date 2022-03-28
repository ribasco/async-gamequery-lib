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

package com.ibasco.agql.protocols.valve.steam.master;

import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerType;
import com.ibasco.agql.protocols.valve.steam.master.packets.MasterServerAddressPacket;
import org.jetbrains.annotations.ApiStatus;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.EnumMap;

@ApiStatus.Internal
public final class MasterServer {

    private static final EnumMap<MasterServerType, InetSocketAddress[]> masterAddressMap = new EnumMap<>(MasterServerType.class);

    public static final InetSocketAddress MASTER_ADDR = new InetSocketAddress("hl2master.steampowered.com", 27011);

    public static final InetSocketAddress[] MASTER_ADDRS = {new InetSocketAddress("208.64.200.52", 27011), new InetSocketAddress("208.64.200.39", 27011), new InetSocketAddress("208.64.200.65", 27011)};

    public static final int SOURCE_MASTER_TYPE = 0x31;

    public static final String INITIAL_IP = "0.0.0.0:0";

    public static final int REGION_US_EAST = 0x00;

    public static final int REGION_US_WEST = 0x01;

    public static final int REGION_SOUTH_AMERICA = 0x02;

    public static final int REGION_EUROPE = 0x03;

    public static final int REGION_ASIA = 0x04;

    public static final int REGION_AUSTRALIA = 0x05;

    public static final int REGION_MIDDLE_EAST = 0x06;

    public static final int REGION_AFRICA = 0x07;

    public static final int REGION_WORLD = 0xFF;

    public static boolean isHeaderPacket(MasterServerAddressPacket packet) {
        return 255 == packet.getFirst() && 255 == packet.getSecond() && 255 == packet.getThird() && 255 == packet.getFourth() && 26122 == packet.getPort();
    }

    public static boolean isTerminatingPacket(MasterServerAddressPacket packet) {
        return (packet.getFirst() + packet.getSecond() + packet.getThird() + packet.getFourth() + packet.getPort()) == 0;
    }

    public static boolean isTerminatingAddress(InetSocketAddress address) {
        return "0.0.0.0".equals(address.getAddress().getHostAddress());
    }

    public static InetSocketAddress[] getCachedMasterAddress(MasterServerType type, boolean forceRefresh) {
        if (forceRefresh)
            masterAddressMap.clear();
        return masterAddressMap.computeIfAbsent(type, MasterServer::getAddresses);
    }

    public static InetSocketAddress[] getAddresses(MasterServerType type) {
        if (type == null)
            throw new IllegalArgumentException("MasterServerType not specified");
        final InetSocketAddress masterAddress = type.getMasterAddress();
        try {
            final InetAddress[] addr = InetAddress.getAllByName(masterAddress.getHostString());
            InetSocketAddress[] addresses = new InetSocketAddress[addr.length];
            for (int i = 0; i < addresses.length; i++)
                addresses[i] = new InetSocketAddress(addr[i].getHostAddress(), masterAddress.getPort());
            return addresses;
        } catch (UnknownHostException e) {
            throw new IllegalStateException("Failed to obtain source master addresses", e);
        }
    }
}
