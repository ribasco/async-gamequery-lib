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

package com.ibasco.agql.protocols.supercell.coc.webapi.enums;

import org.jetbrains.annotations.ApiStatus;

/**
 * Created by raffy on 10/28/2016.
 *
 * @author Rafael Luis Ibasco
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
public enum CocWarFrequency {
    NONE(""),
    ALWAYS("always"),
    MORE_THAN_ONCE_PER_WEEK("moreThanOncePerWeek"),
    ONCE_PER_WEEK("oncePerWeek"),
    LESS_THAN_ONCE_PER_WEEK("lessThanOncePerWeek"),
    NEVER("never"),
    UNKNOWN("unknown");

    private final String code;

    CocWarFrequency(String code) {
        this.code = code;
    }

    /**
     * <p>Getter for the field <code>code</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getCode() {
        return code;
    }
}
