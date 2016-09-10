/*
 * MIT License
 *
 * Copyright (c) [year] [fullname]
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
 */

package com.ribasco.gamecrawler.protocols.valve.server.packets.response;

import com.ribasco.gamecrawler.protocols.valve.server.SourceResponsePacket;
import io.netty.buffer.ByteBuf;

import java.net.InetSocketAddress;
import java.util.Vector;

import static com.ribasco.gamecrawler.protocols.valve.server.SourceConstants.RESPONSE_MASTER_SECONDARY_HEADER;

/**
 * Created by raffy on 9/5/2016.
 */
public class SourceMasterResponsePacket extends SourceResponsePacket<Vector<InetSocketAddress>> {

    private Vector<InetSocketAddress> servers;
    private StringBuilder ip;

    public SourceMasterResponsePacket(ByteBuf buffer) {
        super(buffer);
        servers = new Vector<>();
        ip = new StringBuilder();
    }

    protected Vector<InetSocketAddress> createFromBuffer() {
        //Clear the list
        servers.clear();

        int firstOctet, secondOctet, thirdOctet, fourthOctet, portNumber;

        ByteBuf data = getBuffer();

        //Verify that the secondary header is correct
        if (RESPONSE_MASTER_SECONDARY_HEADER != data.readByte()) {
            throw new RuntimeException("Invalid Secondary Header");
        }

        //Process the content containing list of server ips
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
