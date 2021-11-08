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

package com.ibasco.agql.protocols.valve.source.query;

import com.ibasco.agql.protocols.valve.source.query.enums.SourceGameResponse;
import com.ibasco.agql.protocols.valve.source.query.response.SourceChallengeResponse;
import com.ibasco.agql.protocols.valve.source.query.response.SourceInfoResponse;
import com.ibasco.agql.protocols.valve.source.query.response.SourcePlayerResponse;
import com.ibasco.agql.protocols.valve.source.query.response.SourceRulesResponse;

/**
 * Created by raffy on 9/19/2016.
 */
public class SourceResponseFactory {

    /**
     * Creates a new instance of {@link SourceServerResponse} from {@link SourceResponsePacket}
     *
     * @param packet A {@link SourceResponsePacket} instance
     *
     * @return A new {@link SourceServerResponse} instance
     */
    public static SourceServerResponse createResponse(SourceResponsePacket packet) {
        SourceGameResponse type = SourceGameResponse.get(packet.getSingleBytePacketHeader());
        SourceServerResponse response;
        switch (type) {
            case CHALLENGE:
                response = new SourceChallengeResponse();
                break;
            case INFO:
                response = new SourceInfoResponse();
                break;
            case PLAYER:
                response = new SourcePlayerResponse();
                break;
            case RULES:
                response = new SourceRulesResponse();
                break;
            default:
                response = null;
                break;
        }
        return response;
    }
}
