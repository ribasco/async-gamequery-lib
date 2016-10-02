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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.net.InetSocketAddress;

/**
 * Created by raffy on 9/26/2016.
 */
public class DefaultSessionKey<Req extends AbstractRequest, Res extends AbstractResponse> extends SessionKey<Req, Res> {

    public DefaultSessionKey(SessionKey key) {
        super(key);
    }

    public DefaultSessionKey(Res response) {
        super(response);
    }

    public DefaultSessionKey(Res response, Timeout timeout) {
        super(response, timeout);
    }

    public DefaultSessionKey(Class<Res> responseClass, InetSocketAddress responseAddress) {
        super(responseClass, responseAddress);
    }

    public DefaultSessionKey(Class<Res> responseClass, InetSocketAddress messageAddress, Timeout timeout) {
        super(responseClass, messageAddress, timeout);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DefaultSessionKey))
            return false;
        if (o == this)
            return true;

        final DefaultSessionKey rhs = (DefaultSessionKey) o;

        if (responseAddress == null && rhs.getResponseAddress() != null)
            return false;
        if (responseClass == null && rhs.getResponseClass() != null)
            return false;

        return new EqualsBuilder().append(responseClass.getSimpleName(), rhs.getResponseClass().getSimpleName())
                .append(responseAddress.getAddress().getHostAddress(), rhs.getResponseAddress().getAddress().getHostAddress())
                .append(responseAddress.getPort(), rhs.getResponseAddress().getPort())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(19, 33).append(responseClass.getSimpleName())
                .append(responseAddress.getAddress().getHostAddress())
                .append(responseAddress.getPort()).hashCode();
    }
}
