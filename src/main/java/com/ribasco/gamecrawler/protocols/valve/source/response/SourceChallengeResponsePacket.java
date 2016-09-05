package com.ribasco.gamecrawler.protocols.valve.source.response;

import com.ribasco.gamecrawler.protocols.GameRequest;
import com.ribasco.gamecrawler.protocols.valve.source.SourceResponsePacket;
import com.ribasco.gamecrawler.protocols.valve.source.requests.SourceChallengeRequest;
import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;

/**
 * Created by raffy on 9/5/2016.
 */
public class SourceChallengeResponsePacket extends SourceResponsePacket<Integer> {

    public SourceChallengeResponsePacket(ByteBuf buffer) {
        super(buffer);
    }

    @Override
    protected Integer createFromBuffer() {
        ByteBuf data = this.getBuffer();
        Integer challengeNumber;

        try {
            //Read the 32bit challenge number
            challengeNumber = data.readIntLE();
        } finally {
            ReferenceCountUtil.release(data);
        }

        return challengeNumber;
    }

    @Override
    public Class<? extends GameRequest> getRequestClass() {
        return SourceChallengeRequest.class;
    }
}
