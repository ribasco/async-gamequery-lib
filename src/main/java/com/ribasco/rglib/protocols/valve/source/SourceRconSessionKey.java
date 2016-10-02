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

package com.ribasco.rglib.protocols.valve.source;

import com.ribasco.rglib.core.session.SessionKey;
import io.netty.util.Timeout;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.net.InetSocketAddress;

/**
 * Created by raffy on 9/26/2016.
 */
public class SourceRconSessionKey extends SessionKey<SourceRconRequest, SourceRconResponse> {

    private Integer requestId = null;

    SourceRconSessionKey(SessionKey key) {
        super(key);
    }

    SourceRconSessionKey(SourceRconResponse response) {
        super(response);
        requestId = response.getRequestId();
    }

    SourceRconSessionKey(Class<? extends SourceRconResponse> responseClass, SourceRconRequest request) {
        super(responseClass, request.recipient());
        requestId = request.getRequestId();
    }

    public SourceRconSessionKey(SourceRconResponse response, Timeout timeout) {
        super(response, timeout);
    }

    public SourceRconSessionKey(Class<? extends SourceRconResponse> responseClass, InetSocketAddress responseAddress) {
        super(responseClass, responseAddress);
    }

    public SourceRconSessionKey(Class<SourceRconResponse> responseClass, InetSocketAddress messageAddress, Timeout timeout) {
        super(responseClass, messageAddress, timeout);
    }

    public Integer getRequestId() {
        return requestId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SourceRconSessionKey))
            return false;
        if (o == this)
            return true;

        final SourceRconSessionKey rhs = (SourceRconSessionKey) o;

        if (requestId == null || rhs.getRequestId() == null)
            return false;
        if (responseAddress == null && rhs.getResponseAddress() != null)
            return false;
        if (responseClass == null && rhs.getResponseClass() != null)
            return false;

        return new EqualsBuilder().append(responseClass.getSimpleName(), rhs.getResponseClass().getSimpleName())
                .append(responseAddress.getAddress().getHostAddress(), rhs.getResponseAddress().getAddress().getHostAddress())
                .append(responseAddress.getPort(), rhs.getResponseAddress().getPort())
                .append(requestId, rhs.getRequestId())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(73, 41).append(responseClass.getSimpleName())
                .append(responseAddress.getAddress().getHostAddress())
                .append(responseAddress.getPort())
                .append(requestId)
                .hashCode();
    }
}
