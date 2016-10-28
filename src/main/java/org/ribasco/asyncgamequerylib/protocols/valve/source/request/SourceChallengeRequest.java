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

package org.ribasco.asyncgamequerylib.protocols.valve.source.request;

import org.ribasco.asyncgamequerylib.protocols.valve.source.SourceServerRequest;
import org.ribasco.asyncgamequerylib.protocols.valve.source.enums.SourceChallengeType;
import org.ribasco.asyncgamequerylib.protocols.valve.source.packets.request.SourceChallengeRequestPacket;

import java.net.InetSocketAddress;

/**
 * Created by raffy on 9/15/2016.
 */
public class SourceChallengeRequest extends SourceServerRequest<SourceChallengeRequestPacket> {

    private SourceChallengeType type;

    public SourceChallengeRequest(SourceChallengeType type, InetSocketAddress recipient) {
        super(recipient);
        this.type = type;
    }

    @Override
    public SourceChallengeRequestPacket getMessage() {
        return new SourceChallengeRequestPacket(type);
    }
}
