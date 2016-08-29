package com.ribasco.gamecrawler.net.servers;

import java.net.InetSocketAddress;

/**
 * Created by raffy on 8/27/2016.
 */
public interface Server {
    InetSocketAddress getAddress();
    String getName();
    String getCountry();
    int getPing();
}
