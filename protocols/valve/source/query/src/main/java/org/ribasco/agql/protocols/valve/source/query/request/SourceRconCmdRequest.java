/*
 * MIT License
 *
 * Copyright (c) 2016 Asynchronous Game Query Library
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

package org.ribasco.agql.protocols.valve.source.query.request;

import org.ribasco.agql.protocols.valve.source.query.SourceRconRequest;
import org.ribasco.agql.protocols.valve.source.query.packets.request.SourceRconCmdRequestPacket;

import java.net.InetSocketAddress;

/**
 * Created by raffy on 9/24/2016.
 */
public class SourceRconCmdRequest extends SourceRconRequest<SourceRconCmdRequestPacket> {
    private String command;

    public SourceRconCmdRequest(InetSocketAddress recipient, int requestId, String command) {
        super(recipient, requestId);
        this.command = command;
    }

    @Override
    public SourceRconCmdRequestPacket getMessage() {
        return new SourceRconCmdRequestPacket(getRequestId(), this.command);
    }
}
