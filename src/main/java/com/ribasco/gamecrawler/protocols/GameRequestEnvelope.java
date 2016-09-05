package com.ribasco.gamecrawler.protocols;

import java.net.InetSocketAddress;

/**
 * Created by raffy on 9/5/2016.
 */
public class GameRequestEnvelope<T extends GameRequest> {

    private T request;
    private InetSocketAddress address;

    public GameRequestEnvelope(T request, InetSocketAddress address) {
        this.request = request;
        this.address = address;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public T getRequest() {
        return request;
    }
}
