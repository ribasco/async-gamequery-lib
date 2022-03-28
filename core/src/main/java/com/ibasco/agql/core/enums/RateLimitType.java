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

package com.ibasco.agql.core.enums;

import dev.failsafe.RateLimiter;
import dev.failsafe.RateLimiterBuilder;

import java.time.Duration;
import java.util.function.BiFunction;

/**
 * Enumeration identifying the different methods of Rate Limiting
 *
 * @author Rafael Luis Ibasco
 * @see <a href="https://failsafe.dev/rate-limiter/#rate-limiter">Rate Limiter</a>
 */
public enum RateLimitType {
    /**
     * A smooth rate limiter permits a max number of executions per time period, using a leaky bucket approach to spread out executions at an even rate
     *
     * @see <a href="https://failsafe.dev/rate-limiter/#smooth-rate-limiter">Smooth Rate Limiter</a>
     */
    SMOOTH(RateLimiter::smoothBuilder),
    /**
     * A bursty rate limiter uses a fixed window approach to permit a max number of executions for individual time periods
     *
     * @see <a href="https://failsafe.dev/rate-limiter/#bursty-rate-limiter">Bursty Rate Limiter</a>
     */
    BURST(RateLimiter::burstyBuilder);

    private final BiFunction<Long, Duration, RateLimiterBuilder<?>> function;

    RateLimitType(BiFunction<Long, Duration, RateLimiterBuilder<?>> function) {
        this.function = function;
    }

    /**
     * Returns the builder function for creating the type of Rate Limiter
     *
     * @return A {@link BiFunction}
     */
    public BiFunction<Long, Duration, RateLimiterBuilder<?>> getBuilder() {
        return function;
    }
}
