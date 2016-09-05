package com.ribasco.gamecrawler.protocols.valve.source.response;

import com.ribasco.gamecrawler.protocols.valve.source.SourceRules;
import com.ribasco.gamecrawler.protocols.GameRequest;
import com.ribasco.gamecrawler.protocols.valve.source.SourceResponsePacket;
import io.netty.buffer.ByteBuf;

/**
 * Created by raffy on 9/6/2016.
 */
public class SourceRulesResponsePacket extends SourceResponsePacket<SourceRules> {
    public SourceRulesResponsePacket(ByteBuf buffer) {
        super(buffer);
    }

    @Override
    protected SourceRules createFromBuffer() {
        return null;
    }

    @Override
    public Class<? extends GameRequest> getRequestClass() {
        return null;
    }
}
