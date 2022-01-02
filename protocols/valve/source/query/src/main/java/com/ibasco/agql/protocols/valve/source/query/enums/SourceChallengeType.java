/*
 * Copyright (c) 2018-2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.protocols.valve.source.query.enums;

/**
 * Created by raffy on 9/11/2016.
 */
public enum SourceChallengeType {
    INFO((byte) 0x54),
    PLAYER((byte) 0x55),
    RULES((byte) 0x56),
    CHALLENGE((byte) 0x57),
    ANY((byte) 0x56);

    private final byte header;

    SourceChallengeType(byte header) {
        this.header = header;
    }

    public byte getHeader() {
        return header;
    }
}
