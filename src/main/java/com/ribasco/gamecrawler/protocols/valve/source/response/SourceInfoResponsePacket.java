package com.ribasco.gamecrawler.protocols.valve.source.response;

import com.ribasco.gamecrawler.protocols.GameRequest;
import com.ribasco.gamecrawler.protocols.valve.source.SourceResponsePacket;
import com.ribasco.gamecrawler.protocols.valve.source.SourceServer;
import com.ribasco.gamecrawler.protocols.valve.source.requests.SourceInfoRequest;
import io.netty.buffer.ByteBuf;

import static com.ribasco.gamecrawler.utils.ByteBufUtils.getString;

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
            server.setName(getString(data));
            server.setMapName(getString(data));
            server.setGameDirectory(getString(data));
            server.setGameDescription(getString(data));
            server.setAppId(data.readShortLE());
            server.setNumOfPlayers(data.readByte());
            server.setMaxPlayers(data.readByte());
            server.setNumOfBots(data.readByte());
            server.setDedicated(data.readByte() == 1);
            server.setOperatingSystem((char) data.readByte());
            server.setPasswordProtected(data.readByte() == 1);
            server.setSecure(data.readByte() == 1);
            server.setGameVersion(getString(data));

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
                    server.setTvName(getString(data));
                }

                if ((extraDataFlag & EDF_SERVER_TAGS) != 0) {
                    server.setServerTags(getString(data));
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

    @Override
    public Class<? extends GameRequest> getRequestClass() {
        return SourceInfoRequest.class;
    }
}
