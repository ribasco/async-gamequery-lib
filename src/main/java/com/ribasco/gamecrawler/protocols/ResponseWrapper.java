package com.ribasco.gamecrawler.protocols;

import java.net.InetSocketAddress;

/**
 * Created by raffy on 9/5/2016.
 */
public interface ResponseWrapper {
    InetSocketAddress getResponseSender();
    String getSessionId();
    Object getResponseObject();
}
