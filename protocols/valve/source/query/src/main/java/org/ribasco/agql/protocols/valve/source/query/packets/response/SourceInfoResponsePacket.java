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

package org.ribasco.agql.protocols.valve.source.query.packets.response;

import io.netty.buffer.ByteBuf;
import org.ribasco.agql.protocols.valve.source.query.SourceResponsePacket;
import org.ribasco.agql.protocols.valve.source.query.pojos.SourceServer;

import static org.ribasco.agql.core.utils.ByteBufUtils.readString;

/**
 * Created by raffy on 9/5/2016.
 */
public class SourceInfoResponsePacket extends SourceResponsePacket<SourceServer> {

    /**
     * This is for internal use only and no need to expose to the public API
     */
    private static byte EDF_GAME_ID = (byte) 0x01;
    private static byte EDF_GAME_PORT = (byte) 0x80;
    private static byte EDF_SERVER_ID = (byte) 0x10;
    private static byte EDF_SERVER_TAGS = (byte) 0x20;
    private static byte EDF_SOURCE_TV = (byte) 0x40;

    @Override
    public SourceServer toObject() {
        ByteBuf data = getPayloadBuffer();

        SourceServer server = new SourceServer();

        //Start Decoding
        server.setNetworkVersion(data.readByte());
        server.setName(readString(data));
        server.setMapName(readString(data));
        server.setGameDirectory(readString(data));
        server.setGameDescription(readString(data));
        server.setAppId(data.readShortLE());
        server.setNumOfPlayers(data.readByte());
        server.setMaxPlayers(data.readByte());
        server.setNumOfBots(data.readByte());
        server.setDedicated(data.readByte() == 1);
        server.setOperatingSystem((char) data.readByte());
        server.setPasswordProtected(data.readByte() == 1);
        server.setSecure(data.readByte() == 1);
        server.setGameVersion(readString(data));

        if (data.readableBytes() > 0) {
            byte extraDataFlag = data.readByte();

            if ((extraDataFlag & EDF_GAME_PORT) != 0) {
                data.readShortLE(); //discard, we already know which port based on the sender address
            }

            if ((extraDataFlag & EDF_SERVER_ID) != 0) {
                //this.info.put("serverId", Long.reverseBytes((this.contentData.getInt() << 32) | this.contentData.getInt()));
                server.setServerId(data.readLongLE());
            }

            if ((extraDataFlag & EDF_SOURCE_TV) != 0) {
                server.setTvPort(data.readShortLE());
                server.setTvName(readString(data));
            }

            if ((extraDataFlag & EDF_SERVER_TAGS) != 0) {
                server.setServerTags(readString(data));
            }

            if ((extraDataFlag & EDF_GAME_ID) != 0) {
                server.setGameId(data.readLongLE());
                //this.info.put("gameId", Long.reverseBytes((this.contentData.getInt() << 32) | this.contentData.getInt()));
            }
        }

        return server;
    }
}
