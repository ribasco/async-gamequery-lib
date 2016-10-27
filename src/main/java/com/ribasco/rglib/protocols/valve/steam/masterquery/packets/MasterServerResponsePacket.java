/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Luis Ibasco
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

package com.ribasco.rglib.protocols.valve.steam.masterquery.packets;

import com.ribasco.rglib.core.Decodable;
import com.ribasco.rglib.protocols.valve.steam.masterquery.MasterServerPacket;
import io.netty.buffer.ByteBuf;

import java.net.InetSocketAddress;
import java.util.Vector;

public class MasterServerResponsePacket extends MasterServerPacket implements Decodable<Vector<InetSocketAddress>> {
    private Vector<InetSocketAddress> servers;
    private StringBuffer ip;

    public MasterServerResponsePacket() {
        servers = new Vector<>();
        ip = new StringBuffer();
    }

    @Override
    public Vector<InetSocketAddress> toObject() {
        ByteBuf data = getPayloadBuffer();

        //Clear the list
        servers.clear();

        int firstOctet, secondOctet, thirdOctet, fourthOctet, portNumber;

        //Process the content containing list of source ips
        do {
            //Clear string
            ip.setLength(0);

            firstOctet = data.readByte() & 0xFF;
            secondOctet = data.readByte() & 0xFF;
            thirdOctet = data.readByte() & 0xFF;
            fourthOctet = data.readByte() & 0xFF;
            portNumber = data.readShort() & 0xFFFF;

            //Build our ip string
            ip.append(firstOctet).append(".")
                    .append(secondOctet).append(".")
                    .append(thirdOctet).append(".")
                    .append(fourthOctet);

            //Add to the list
            servers.add(new InetSocketAddress(ip.toString(), portNumber));

            //Append port number
            ip.append(":").append(portNumber);
        } while (data.readableBytes() > 0);

        return servers;
    }
}
