/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Luis Ibasco
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **************************************************************************************************/

package org.ribasco.asyncgamequerylib.core.pojos;

import java.net.InetSocketAddress;

/**
 * Created by raffy on 8/28/2016.
 */
public abstract class GenericServer {

    private InetSocketAddress address;
    private String country;
    private int ping;

    public GenericServer() {
        this.address = null;
        this.country = null;
        this.ping = -1;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public String getHostAddress() {
        return address.getAddress().getHostAddress();
    }

    public int getPort() {
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
