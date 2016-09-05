package com.ribasco.gamecrawler.protocols;

import io.netty.util.concurrent.Promise;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by raffy on 9/4/2016.
 */
public class Session {

    private final ConcurrentHashMap<String, Promise> registry;

    private Session() {
        registry = new ConcurrentHashMap<>();
    }

    private static class SessionHolder {
        private static final Session INSTANCE = new Session();
    }

    public static ConcurrentHashMap<String, Promise> getRegistry() {
        return SessionHolder.INSTANCE.registry;
    }

    public static String getSessionId(InetSocketAddress address, GameResponse responsePacket) {
        return getSessionId(address, responsePacket.getRequestClass());
    }

    public static String getSessionId(InetSocketAddress address, Class<? extends GameRequest> requestClass) {
        if (address == null || requestClass == null)
            return null;
        return String.format("%s:%d:%s", address.getAddress().getHostAddress(), address.getPort(), requestClass.getSimpleName());
    }
}
