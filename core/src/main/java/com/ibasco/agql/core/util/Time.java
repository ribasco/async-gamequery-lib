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

package com.ibasco.agql.core.util;

import org.apache.commons.lang3.time.DurationFormatUtils;

import java.time.Duration;

/**
 * <p>Time class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class Time {

    /**
     * <p>getTimeDesc.</p>
     *
     * @param duration a {@link java.time.Duration} object
     * @return a {@link java.lang.String} object
     */
    public static String getTimeDesc(Duration duration) {
        return DurationFormatUtils.formatDuration(duration.toMillis(), "HH:mm:ss");
    }

    /**
     * <p>getTimeDesc.</p>
     *
     * @param millis a long
     * @return a {@link java.lang.String} object
     */
    public static String getTimeDesc(long millis) {
        return getTimeDesc(millis, false);
    }

    /**
     * <p>getTimeDesc.</p>
     *
     * @param millis a long
     * @param shortDesc a boolean
     * @return a {@link java.lang.String} object
     */
    public static String getTimeDesc(long millis, boolean shortDesc) {
        return DurationFormatUtils.formatDuration(millis, "HH:mm:ss");
    }
}
