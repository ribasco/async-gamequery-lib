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
import com.ribasco.gamecrawler.protocols.valve.server.SourceServer;
import io.netty.buffer.ByteBuf;

import static com.ribasco.gamecrawler.utils.ByteBufUtils.readString;

/**
 * Created by raffy on 9/5/2016.
 */
public class SourceInfoResponsePacket extends SourceResponsePacket<SourceServer> {

    private static byte EDF_GAME_ID = (byte) 0x01;
    private static byte EDF_GAME_PORT = (byte) 0x80;
    private static byte EDF_SERVER_ID = (byte) 0x10;
    private static byte EDF_SERVER_TAGS = (byte) 0x20;
    private static byte EDF_SOURCE_TV = (byte) 0x40;

    public SourceInfoResponsePacket(ByteBuf buffer) {
        super(buffer);
    }

    @Override
    protected SourceServer createFromBuffer() {

        ByteBuf data = getBuffer();

        SourceServer server = new SourceServer();

        try {
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
        } finally {
            data.release();
        }

        return server;
    }
}
