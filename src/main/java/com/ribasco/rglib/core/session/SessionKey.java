/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Ibasco
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

package com.ribasco.rglib.core.session;

import com.ribasco.rglib.core.AbstractRequest;
import com.ribasco.rglib.core.AbstractResponse;
import io.netty.util.Timeout;

import java.io.Serializable;
import java.net.InetSocketAddress;

/**
 * Created by raffy on 9/20/2016.
 */
public abstract class SessionKey<Req extends AbstractRequest, Res extends AbstractResponse> implements Serializable {

    protected Class<? extends Res> responseClass;
    protected InetSocketAddress responseAddress;
    protected Timeout timeout;
    protected long timeRegistered;

    /**
     * Copy constructor
     *
     * @param key
     */
    public SessionKey(SessionKey key) {
        this.responseClass = key.getResponseClass();
        this.responseAddress = key.getResponseAddress();
        this.timeout = key.getTimeout();
        this.timeRegistered = key.getTimeRegistered();
    }

    public SessionKey(Res response) {
        this(response, null);
    }

    public SessionKey(Res response, Timeout timeout) {
        this((Class<Res>) response.getClass(), response.sender(), timeout);
    }

    public SessionKey(Class<? extends Res> responseClass, InetSocketAddress responseAddress) {
        this(responseClass, responseAddress, null);
    }

    public SessionKey(Class<? extends Res> responseClass, InetSocketAddress messageAddress, Timeout timeout) {
        this.responseClass = responseClass;
        this.responseAddress = messageAddress;
        this.timeout = timeout;
        this.timeRegistered = System.currentTimeMillis();
    }

    public Class<? extends Res> getResponseClass() {
        return responseClass;
    }

    public void setResponseClass(Class<Res> responseClass) {
        this.responseClass = responseClass;
    }

    public InetSocketAddress getResponseAddress() {
        return responseAddress;
    }

    public void setResponseAddress(InetSocketAddress responseAddress) {
        this.responseAddress = responseAddress;
    }

    public Timeout getTimeout() {
        return timeout;
    }

    public void setTimeout(Timeout timeout) {
        this.timeout = timeout;
    }

    public long getTimeRegistered() {
        return timeRegistered;
    }

    public void setTimeRegistered(long timeRegistered) {
        this.timeRegistered = timeRegistered;
    }

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();

    @Override
    public String toString() {
        return "SessionKey{" +
                "messageClass=" + responseClass.getSimpleName() +
                ", messageAddress=" + responseAddress.getAddress() + ":" + responseAddress.getPort() + '}';
    }
}
