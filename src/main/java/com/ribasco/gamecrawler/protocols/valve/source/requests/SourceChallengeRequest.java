package com.ribasco.gamecrawler.protocols.valve.source.requests;

import com.ribasco.gamecrawler.protocols.valve.source.SourceConstants;
import com.ribasco.gamecrawler.protocols.valve.source.SourceRequest;

/**
 * Created by raffy on 9/5/2016.
 */
public class SourceChallengeRequest extends SourceRequest {

    public SourceChallengeRequest() {
        setHeader(SourceConstants.REQUEST_CHALLENGE_HEADER);
    }

    @Override
    public byte[] getPayload() {
        return new byte[] {};
    }
}
