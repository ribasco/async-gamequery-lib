package com.ribasco.gamecrawler.protocols;

import io.netty.util.concurrent.Promise;

import java.net.InetSocketAddress;

/**
 * Created by raffy on 9/1/2016.
 */
public interface GameRequest {
    byte[] getBytes();
}
