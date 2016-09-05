package com.ribasco.gamecrawler.protocols;

/**
 * Created by raffy on 8/27/2016.
 */
public abstract class GameServer extends GenericServer {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new StringBuilder().append(getHostAddress()).append(":").append(getPort()).toString();
    }
}
