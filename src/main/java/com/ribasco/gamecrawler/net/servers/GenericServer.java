package com.ribasco.gamecrawler.net.servers;

import java.net.InetSocketAddress;

/**
 * Created by raffy on 8/28/2016.
 */
public abstract class GenericServer implements Server {

    private InetSocketAddress serverAddress;
    private String serverName;
    private String serverCountry;

    @Override
    public InetSocketAddress getAddress() {
        return this.serverAddress;
    }

    @Override
    public String getName() {
        return this.serverName;
    }

    @Override
    public String getCountry() {
        return this.serverCountry;
    }

    @Override
    public int getPing() {
        return -1;
    }
}
