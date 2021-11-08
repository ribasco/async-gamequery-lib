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

package com.ibasco.agql.protocols.valve.source.query.packets.request;

import com.ibasco.agql.core.utils.ByteUtils;
import com.ibasco.agql.protocols.valve.source.query.SourceRequestPacket;
import com.ibasco.agql.protocols.valve.source.query.enums.SourceGameRequest;

/**
 * Created by raffy on 9/1/2016.
 */
public class SourceInfoRequestPacket extends SourceRequestPacket {

    private static final byte[] INFO_PAYLOAD = "Source Engine Query\0".getBytes();

    /**
     * Creates a new source info request packet (no challenge)
     */
    public SourceInfoRequestPacket() {
        this(null);
    }

    public SourceInfoRequestPacket(Integer challenge) {
        this(challenge, false);
    }

    public SourceInfoRequestPacket(Integer challenge, boolean override) {
        setHeader(SourceGameRequest.INFO);
        byte[] payload;
        if (override) {
            payload = new byte[1395];
            System.arraycopy(INFO_PAYLOAD, 0, payload, 0, INFO_PAYLOAD.length);
            if (challenge != null) {
                byte[] challengeBytes = ByteUtils.toByteArrayLE(challenge);
                System.arraycopy(challengeBytes, 0, payload, INFO_PAYLOAD.length, challengeBytes.length);
            }
        } else {
            if (challenge != null) {
                payload = new byte[24];
                System.arraycopy(INFO_PAYLOAD, 0, payload, 0, INFO_PAYLOAD.length);
                byte[] challengeBytes = ByteUtils.toByteArrayLE(challenge);
                System.arraycopy(challengeBytes, 0, payload, INFO_PAYLOAD.length, challengeBytes.length);
            } else {
                payload = new byte[20];
                System.arraycopy(INFO_PAYLOAD, 0, payload, 0, INFO_PAYLOAD.length);
            }
        }
        setPayload(payload);
    }

    @Override
    public String toString() {
        return "SourceInfoRequestPacket{}";
    }
}
