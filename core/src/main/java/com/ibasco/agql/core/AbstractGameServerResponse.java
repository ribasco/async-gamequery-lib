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

import java.net.InetSocketAddress;

abstract public class AbstractGameServerResponse<T extends Decodable<U>, U> extends AbstractResponse<U> {
    private T responsePacket;

    public AbstractGameServerResponse(InetSocketAddress sender, T packet) {
        super(sender);
        this.responsePacket = packet;
    }

    public T getResponsePacket() {
        return responsePacket;
    }

    public void setResponsePacket(T responsePacket) {
        this.responsePacket = responsePacket;
    }

    @Override
    public U getMessage() {
        return responsePacket.toObject();
    }

    @Override
    public String toString() {
        return toStringBuilder()
                .append("type", this.getClass().getSimpleName())
                .append("sender", sender())
                .append("responsePacket", responsePacket)
                .toString();
    }
}
