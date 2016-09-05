package com.ribasco.gamecrawler.protocols.valve.source;

import com.ribasco.gamecrawler.protocols.ResponseWrapper;
import com.ribasco.gamecrawler.protocols.Session;

import java.net.InetSocketAddress;

/**
 * Created by raffy on 9/5/2016.
 */
public class SourceResponseWrapper implements ResponseWrapper {
    private SourceResponsePacket responsePacket;
    private InetSocketAddress sender;

    public SourceResponseWrapper(SourceResponsePacket responsePacket, InetSocketAddress sender) {
        this.responsePacket = responsePacket;
        this.sender = sender;
    }

    @Override
    public InetSocketAddress getResponseSender() {
        return this.sender;
    }

    @Override
    public String getSessionId() {
        return Session.getSessionId(this.sender, this.responsePacket);
    }

    @Override
    public Object getResponseObject() {
        return this.responsePacket.toObject();
    }
}
