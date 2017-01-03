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

package com.ibasco.agql.protocols.valve.source.query.packets.request;

import com.ibasco.agql.protocols.valve.source.query.SourceRconRequestPacket;
import com.ibasco.agql.protocols.valve.source.query.enums.SourceRconRequestType;
import com.ibasco.agql.protocols.valve.source.query.utils.SourceRconUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * This packet is sent right after each command request.
 * </p>
 *
 * @see <a href="https://developer.valvesoftware.com/wiki/Talk:Source_RCON_Protocol">How
 * to handle split packet response</a>
 */
public class SourceRconTermRequestPacket extends SourceRconRequestPacket {

    public SourceRconTermRequestPacket() {
        super(SourceRconUtil.RCON_TERMINATOR_RID);
        setSize(0);
        setType(SourceRconRequestType.RESPONSE);
        setBody(StringUtils.EMPTY);
    }
}
