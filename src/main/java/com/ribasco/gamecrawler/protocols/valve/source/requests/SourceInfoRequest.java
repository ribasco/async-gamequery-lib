package com.ribasco.gamecrawler.protocols.valve.source.requests;

import com.ribasco.gamecrawler.protocols.valve.source.SourceRequest;

/**
 * Created by raffy on 9/1/2016.
 */
public class SourceInfoRequest extends SourceRequest {

    private static final byte HEADER = 0x54;

    public SourceInfoRequest()
    {
        setHeader(HEADER);
        setPayload("Source Engine Query".getBytes());
    }
}
