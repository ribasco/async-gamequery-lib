/*
 * MIT License
 *
 * Copyright (c) 2018 Asynchronous Game Query Library
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ibasco.agql.core.session;

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.AbstractResponse;
import com.ibasco.agql.core.RequestDetails;
import com.ibasco.agql.core.Transport;
import io.netty.util.Timeout;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

/**
 * Contains the details of the given Session
 */
public class SessionEntry<R extends AbstractRequest, S extends AbstractResponse> {
    private static final Logger log = LoggerFactory.getLogger(SessionEntry.class);

    private Timeout timeout;
    private SessionId id;
    private RequestDetails<R, S> requestDetails;
    private final long timeRegistered = System.currentTimeMillis();
    private long index = -1;

    public SessionEntry(SessionId id, RequestDetails<R, S> requestDetails, long index) {
        this.id = id;
        this.requestDetails = requestDetails;
        this.index = index;
    }

    public SessionId getId() {
        return id;
    }

    public void setId(SessionId id) {
        this.id = id;
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

    public CompletableFuture<S> getClientPromise() {
        return this.requestDetails.getPromise();
    }

    public R getRequest() {
        return this.requestDetails.getRequest();
    }

    public Transport<R> getRequestTransport() {
        return this.requestDetails.getTransport();
    }

    public RequestDetails<R, S> getRequestDetails() {
        return requestDetails;
    }

    public void setRequestDetails(RequestDetails<R, S> requestDetails) {
        this.requestDetails = requestDetails;
    }

    public long getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        log.info("Access equals for {}", this);
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SessionEntry<?, ?> that = (SessionEntry<?, ?>) o;

        return new EqualsBuilder()
                .append(getId(), that.getId())
                .append(getIndex(), that.getIndex())
                .append(getRequest(), that.getRequest())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(421, 743)
                .append(getId())
                .append(getIndex())
                .append(getRequest())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("Id", this.getId())
                .append("Index", this.getIndex())
                .append("Request", this.getRequestDetails().getRequest().getClass().getSimpleName())
                .append("Created", this.getTimeRegistered())
                .toString();
    }
}
