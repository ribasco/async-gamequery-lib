/*
 * Copyright 2021-2022 Asynchronous Game Query Library
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

package com.ibasco.agql.core.util;

import org.apache.commons.lang3.time.DurationFormatUtils;

public class TimeUtil {

    public static String getTimeDesc(long millis) {
        return getTimeDesc(millis, false);
    }

    public static String getTimeDesc(long millis, boolean shortDesc) {
        /*Duration duration = Duration.ofMillis(millis);
        String unit;
        Supplier<Long> unitFunction;

        if (millis < 1000) {
            unit = shortDesc ? "ms" : "millisecond(s)";
            unitFunction = duration::toMillis;
        } else if (millis < 60000) {
            unit = shortDesc ? "sec(s)" : "second(s)";
            unitFunction = duration::getSeconds;
        } else if (millis < 3600000) {
            unit = shortDesc ? "min(s)" : "minute(s)";
            unitFunction = duration::toMinutes;
        } else {
            unit = shortDesc ? "hr(s)" : "hour(s)";
            unitFunction = duration::toHours;
        }*/

        return DurationFormatUtils.formatDuration(millis, "HH:mm:ss");//String.format("%03d %s", unitFunction.get(), unit);
    }
}
