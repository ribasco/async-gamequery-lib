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
 * <p>Dota2IconType class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public enum Dota2IconType {
    NORMAL(0),
    LARGE(1),
    INGAME(2);

    private final int type;

    Dota2IconType(int type) {
        this.type = type;
    }

    /**
     * <p>Getter for the field <code>type</code>.</p>
     *
     * @return a int
     */
    public int getType() {
        return type;
    }
}

