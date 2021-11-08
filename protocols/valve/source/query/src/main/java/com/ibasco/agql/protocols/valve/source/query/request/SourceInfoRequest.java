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

package com.ibasco.agql.protocols.valve.source.query.request;

import com.ibasco.agql.protocols.valve.source.query.SourceServerRequest;
import com.ibasco.agql.protocols.valve.source.query.packets.request.SourceInfoRequestPacket;

import java.net.InetSocketAddress;

/**
 * Created by raffy on 9/15/2016.
 */
public class SourceInfoRequest extends SourceServerRequest<SourceInfoRequestPacket> {

    private final Integer challenge;

    private final boolean override;

    public SourceInfoRequest(Integer challenge, InetSocketAddress recipient) {
        this(challenge, recipient, false);
    }

    public SourceInfoRequest(InetSocketAddress recipient, boolean override) {
        this(null, recipient, override);
    }

    public SourceInfoRequest(Integer challenge, InetSocketAddress recipient, boolean override) {
        super(recipient);
        this.override = override;
        this.challenge = challenge;
    }

    public final Integer getChallenge() {
        return challenge;
    }

    @Override
    public SourceInfoRequestPacket getMessage() {
        return new SourceInfoRequestPacket(challenge, override);
    }
}
