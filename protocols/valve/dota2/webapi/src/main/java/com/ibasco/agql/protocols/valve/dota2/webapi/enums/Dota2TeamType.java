/*
 * Copyright (c) 2022 Asynchronous Game Query Library
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

package com.ibasco.agql.protocols.valve.dota2.webapi.enums;

/**
 * <p>Dota2TeamType class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public enum Dota2TeamType {
    RADIANT(0),
    DIRE(1),
    BROADCASTER(2),
    UNASSIGNED(4);

    private final int typeCode;

    Dota2TeamType(int typeCode) {
        this.typeCode = typeCode;
    }

    /**
     * <p>Getter for the field <code>typeCode</code>.</p>
     *
     * @return a int
     */
    public int getTypeCode() {
        return typeCode;
    }
}
