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

import com.ibasco.agql.core.utils.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

abstract public class AbstractPacket implements Packet {
    private byte[] header = new byte[0];
    private byte[] payload = new byte[0];
    private byte[] trailer = new byte[0];

    @Override
    public byte getSingleBytePacketHeader() {
        return getPacketHeader() != null && getPacketHeader().length >= 1 ? getPacketHeader()[0] : 0;
    }

    @Override
    public byte[] getPacketHeader() {
        return this.header;
    }

    @Override
    public byte[] getPayload() {
        return this.payload;
    }

    public ByteBuf getPayloadBuffer() {
        return Unpooled.copiedBuffer(getPayload());
    }

    @Override
    public byte[] getTrailer() {
        return this.trailer;
    }

    public void setHeader(byte header) {
        setHeader(new byte[]{header});
    }

    public void setHeader(byte[] header) {
        this.header = header;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    public void setTrailer(byte[] trailer) {
        this.trailer = trailer;
    }

    public ToStringBuilder toStringBuilder() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("class", this.getClass().getSimpleName())
                .append("header", ByteUtils.toFormattedHex(getPacketHeader()));
    }

    @Override
    public String toString() {
        return toStringBuilder().toString();
    }
}
