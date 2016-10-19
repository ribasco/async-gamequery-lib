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

package com.ribasco.rglib.core.messenger;

import com.ribasco.rglib.core.AbstractGameServerRequest;
import com.ribasco.rglib.core.AbstractGameServerResponse;
import com.ribasco.rglib.core.AbstractMessenger;
import com.ribasco.rglib.core.enums.ProcessingMode;
import com.ribasco.rglib.core.session.AbstractSessionIdFactory;
import com.ribasco.rglib.core.session.SessionManager;
import com.ribasco.rglib.core.transport.NettyTransport;

/**
 * Messenger using the UDP Transport protocol
 */
public abstract class GameServerMessenger<A extends AbstractGameServerRequest,
        B extends AbstractGameServerResponse, T extends NettyTransport<A>>
        extends AbstractMessenger<A, B, T> {

    public GameServerMessenger(T transport) {
        super(transport);
    }

    public GameServerMessenger(T transport, ProcessingMode processingMode) {
        super(transport, processingMode);
    }

    public GameServerMessenger(T transport, AbstractSessionIdFactory keyFactory, ProcessingMode processingMode) {
        super(transport, keyFactory, processingMode);
    }

    public GameServerMessenger(T transport, SessionManager sessionManager, ProcessingMode processingMode) {
        super(transport, sessionManager, processingMode);
    }
}
