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

package com.ribasco.rglib.core;

import com.ribasco.rglib.core.enums.RequestPriority;
import com.ribasco.rglib.core.enums.RequestStatus;
import com.ribasco.rglib.core.transport.NettyTransport;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Contains all the properties associated with this request
 */
public class RequestDetails<Req extends AbstractRequest, Res extends AbstractResponse> implements Comparable<RequestDetails> {
    private static final Logger log = LoggerFactory.getLogger(RequestDetails.class);
    private Req request;
    private CompletableFuture<Res> clientPromise;
    private RequestStatus status;
    private RequestPriority priority;
    private NettyTransport<Req> transport;
    private Class<Res> expectedResponseClass;
    private AtomicInteger retries = new AtomicInteger(0);
    private long timeCreated;

    public RequestDetails(Req request, CompletableFuture<Res> clientPromise, RequestPriority priority, NettyTransport<Req> transport) {
        this.status = RequestStatus.NEW;
        this.request = request;
        this.clientPromise = clientPromise;
        this.priority = priority;
        this.transport = transport;
        this.timeCreated = System.currentTimeMillis();
    }

    /**
     * Copy Constructor
     *
     * @param requestDetails An {@link RequestDetails} that will be used as reference for the copy
     */
    public RequestDetails(RequestDetails<Req, Res> requestDetails) {
        this.request = requestDetails.getRequest();
        this.clientPromise = requestDetails.getClientPromise();
        this.status = requestDetails.getStatus();
        this.priority = requestDetails.getPriority();
        this.retries = new AtomicInteger(requestDetails.getRetries());
        this.expectedResponseClass = requestDetails.getExpectedResponseClass();
    }

    public Class<Res> getExpectedResponseClass() {
        return expectedResponseClass;
    }

    public void setExpectedResponseClass(Class<Res> expectedResponseClass) {
        this.expectedResponseClass = expectedResponseClass;
    }

    public synchronized CompletableFuture<Res> getClientPromise() {
        return clientPromise;
    }

    public synchronized void setClientPromise(CompletableFuture<Res> clientPromise) {
        this.clientPromise = clientPromise;
    }

    public synchronized RequestPriority getPriority() {
        return priority;
    }

    public synchronized void setPriority(RequestPriority priority) {
        this.priority = priority;
    }

    public int getRetries() {
        return retries.get();
    }

    public synchronized void setRequest(Req request) {
        this.request = request;
    }

    public synchronized Req getRequest() {
        return request;
    }

    public synchronized RequestStatus getStatus() {
        return this.status;
    }

    public synchronized void setStatus(RequestStatus status) {
        this.status = status;
    }

    public int incrementRetry() {
        return this.retries.getAndAdd(1);
    }

    public NettyTransport<Req> getTransport() {
        return transport;
    }

    public void setTransport(NettyTransport transport) {
        this.transport = transport;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof RequestDetails))
            return false;
        final RequestDetails rhs = (RequestDetails) o;
        return new EqualsBuilder()
                .append(this.getClass(), rhs.getClass())
                .append(this.getRequest(), rhs.getRequest())
                .append(this.getTimeCreated(), rhs.getTimeCreated())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(327, 993)
                .append(this.getClass())
                .append(this.getRequest())
                .append(this.getTimeCreated())
                .toHashCode();
    }

    @Override
    public int compareTo(RequestDetails o) {
        return this.priority.compareTo(o.getPriority());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("Request", this.getRequest().getClass().getSimpleName())
                .append("Created", this.getTimeCreated())
                .append("Priority", this.getPriority())
                .append("Status", this.getStatus())
                .toString();
    }
}
