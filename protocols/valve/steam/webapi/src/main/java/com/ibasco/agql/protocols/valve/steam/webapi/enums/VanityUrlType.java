/*
 * Copyright 2018-2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.protocols.valve.steam.webapi.enums;

public enum VanityUrlType {
    DEFAULT(1),
    INDIVIDUAL_PROFILE(1),
    GROUP(2),
    OFFICIAL_GAME_GROUP(3);

    private final int type;

    VanityUrlType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
