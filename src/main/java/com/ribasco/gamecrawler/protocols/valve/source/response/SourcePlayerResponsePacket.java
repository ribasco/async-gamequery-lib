package com.ribasco.gamecrawler.protocols.valve.source.response;

import com.ribasco.gamecrawler.protocols.valve.source.SourcePlayer;
import com.ribasco.gamecrawler.protocols.GameRequest;
import com.ribasco.gamecrawler.protocols.valve.source.SourceResponsePacket;
import io.netty.buffer.ByteBuf;

/**
 * Created by raffy on 9/6/2016.
 */
public class SourcePlayerResponsePacket extends SourceResponsePacket<SourcePlayer> {
    
    public SourcePlayerResponsePacket(ByteBuf buffer) {
        super(buffer);
    }

    @Override
    protected SourcePlayer createFromBuffer() {
        return null;
    }

    @Override
    public Class<? extends GameRequest> getRequestClass() {
        return null;
    }
}
