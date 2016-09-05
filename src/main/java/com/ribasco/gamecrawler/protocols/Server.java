package com.ribasco.gamecrawler.protocols;

import java.net.InetSocketAddress;

/**
 * Created by raffy on 8/27/2016.
 */
public interface Server {
    InetSocketAddress getAddress();

    void setAddress(InetSocketAddress address);

    String getName();

    void setName(String name);

    String getCountry();

    void setCountry(String country);

    int getPing();

    void setPing(int ping);
}
