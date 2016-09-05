package com.ribasco.gamecrawler.protocols;

import java.net.InetSocketAddress;

/**
 * Created by raffy on 8/28/2016.
 */
public abstract class GenericServer implements Server {

    private InetSocketAddress address;
    private String country;
    private int ping;

    public GenericServer()
    {
        this.address = null;
        this.country = null;
        this.ping = -1;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public String getHostAddress()
    {
        return address.getAddress().getHostAddress();
    }

    public int getPort()
    {
        return address.getPort();
    }

    public void setAddress(InetSocketAddress address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getPing() {
        return ping;
    }

    public void setPing(int ping) {
        this.ping = ping;
    }

    @Override
    public String toString() {
        return String.format("IP: %s, PORT: %d", getAddress().getAddress().getHostAddress(), getAddress().getPort());
    }
}
