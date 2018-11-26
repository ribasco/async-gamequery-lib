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

package com.ibasco.agql.core;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import org.apache.commons.lang3.builder.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

abstract public class AbstractMessage<T> implements Message<T>, Comparable<AbstractMessage<T>> {
    private InetSocketAddress sender;
    private InetSocketAddress recipient;

    private static final Logger log = LoggerFactory.getLogger(AbstractMessage.class);

    public AbstractMessage(InetSocketAddress sender, InetSocketAddress recipient) {
        this.sender = sender;
        this.recipient = recipient;
    }

    @Override
    public void setRecipient(InetSocketAddress recipient) {
        this.recipient = recipient;
    }

    @Override
    public void setSender(InetSocketAddress sender) {
        this.sender = sender;
    }

    @Override
    public InetSocketAddress sender() {
        return this.sender;
    }

    @Override
    public InetSocketAddress recipient() {
        return this.recipient;
    }

    @Override
    public abstract T getMessage();

    protected final EqualsBuilder equalsBuilder(Object rhs) {
        final EqualsBuilder builder = new EqualsBuilder();
        AbstractMessage r = (AbstractMessage) rhs;
        builder.append(recipient(), r.recipient());
        builder.append(sender(), r.sender());
        builder.append(defaultIfNull(getMessage(), "").getClass(), defaultIfNull(r.getMessage(), 1).getClass());
        return builder;
    }

    protected final HashCodeBuilder hashCodeBuilder(int primeX, int primeY) {
        final HashCodeBuilder builder = new HashCodeBuilder(primeX, primeY);
        builder.append(sender());
        builder.append(recipient());
        builder.append(getMessage());
        return builder;
    }

    protected final CompareToBuilder compareToBuilder(AbstractMessage<T> rhs) {
        final CompareToBuilder builder = new CompareToBuilder();
        InetSocketAddress lhsSender = defaultIfNull(sender(), new InetSocketAddress(0));
        InetSocketAddress rhsSender = defaultIfNull(rhs.sender(), new InetSocketAddress(0));
        InetSocketAddress lhsReciepient = defaultIfNull(recipient(), new InetSocketAddress(0));
        InetSocketAddress rhsReciepient = defaultIfNull(rhs.recipient(), new InetSocketAddress(0));
        builder.append(lhsSender.getAddress().getHostAddress(), rhsSender.getAddress().getHostAddress());
        builder.append(lhsSender.getPort(), rhsSender.getPort());
        builder.append(lhsReciepient.getAddress().getHostAddress(), rhsReciepient.getAddress().getHostAddress());
        builder.append(lhsReciepient.getPort(), rhsReciepient.getPort());
        builder.append(getClass().getSimpleName(), rhs.getClass().getSimpleName());
        return builder;
    }

    protected final ToStringBuilder toStringBuilder() {
        final ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE);
        builder.append("ClassName", this.getClass().getSimpleName());
        builder.append("Recipient", recipient());
        builder.append("Sender", sender());
        return builder;
    }

    @Override
    public int compareTo(AbstractMessage<T> rhs) {
        return compareToBuilder(rhs).toComparison();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (obj.getClass() != getClass())
            return false;
        log.debug("Sender = {}, Recipient = {}", sender(), recipient());
        return equalsBuilder(obj).isEquals();
    }

    @Override
    public int hashCode() {
        return hashCodeBuilder(51, 103).hashCode();
    }

    @Override
    public String toString() {
        return toStringBuilder().toString();
    }
}
