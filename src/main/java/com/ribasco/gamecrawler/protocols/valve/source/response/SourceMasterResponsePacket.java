package com.ribasco.gamecrawler.protocols.valve.source.response;

import com.ribasco.gamecrawler.protocols.GameRequest;
import com.ribasco.gamecrawler.protocols.valve.source.SourceResponsePacket;
import com.ribasco.gamecrawler.protocols.valve.source.requests.SourceMasterRequest;
import io.netty.buffer.ByteBuf;

import java.net.InetSocketAddress;
import java.util.Vector;

import static com.ribasco.gamecrawler.protocols.valve.source.SourceConstants.RESPONSE_MASTER_SECONDARY_HEADER;

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
        if (RESPONSE_MASTER_SECONDARY_HEADER != data.readByte())
            throw new RuntimeException("Invalid Secondary Header");

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

    @Override
    public Class<? extends GameRequest> getRequestClass() {
        return SourceMasterRequest.class;
    }
}
