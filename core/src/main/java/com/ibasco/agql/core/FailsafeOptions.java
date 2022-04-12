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

package com.ibasco.agql.core;

import com.ibasco.agql.core.util.Option;

/**
 * <p>FailsafeOptions interface.</p>
 *
 * @author Rafael Luis Ibasco
 */
public interface FailsafeOptions {

    /** Constant <code>FAILSAFE_ENABLED</code> */
    Option<Boolean> FAILSAFE_ENABLED = Option.createOption("failsafeEnabled", true);

    /** Constant <code>FAILSAFE_RATELIMIT_ENABLED</code> */
    Option<Boolean> FAILSAFE_RATELIMIT_ENABLED = Option.createOption("failsafeRateLimitEnabled", true);

    /** Constant <code>FAILSAFE_RATELIMIT_MAX_EXEC</code> */
    Option<Long> FAILSAFE_RATELIMIT_MAX_EXEC = Option.createOption("failsafeRateLimitMaxExec", 500L);

    /** Constant <code>FAILSAFE_RATELIMIT_PERIOD</code> */
    Option<Long> FAILSAFE_RATELIMIT_PERIOD = Option.createOption("queryFailsafeRateLimitPeriod", 60000L);

    /** Constant <code>FAILSAFE_RATELIMIT_MAX_WAIT_TIME</code> */
    Option<Long> FAILSAFE_RATELIMIT_MAX_WAIT_TIME = Option.createOption("queryFailsafeRateLimitMaxWaitTime", 10000L);

    /** Constant <code>FAILSAFE_RETRY_ENABLED</code> */
    Option<Boolean> FAILSAFE_RETRY_ENABLED = Option.createOption("queryFailsafeRetryEnabled", true);

    /** Constant <code>FAILSAFE_RETRY_BACKOFF_ENABLED</code> */
    Option<Boolean> FAILSAFE_RETRY_BACKOFF_ENABLED = Option.createOption("queryFailsafeBackoffEnabled", true);

    /** Constant <code>FAILSAFE_RETRY_BACKOFF_DELAY</code> */
    Option<Long> FAILSAFE_RETRY_BACKOFF_DELAY = Option.createOption("queryFailsafeBackoffDelay", 3000L);

    /** Constant <code>FAILSAFE_RETRY_BACKOFF_MAX_DELAY</code> */
    Option<Long> FAILSAFE_RETRY_BACKOFF_MAX_DELAY = Option.createOption("queryFailsafeBackoffMaxDelay", 5000L);

    /** Constant <code>FAILSAFE_RETRY_BACKOFF_DELAY_FACTOR</code> */
    Option<Double> FAILSAFE_RETRY_BACKOFF_DELAY_FACTOR = Option.createOption("queryFailsafeBackoffDelayFactor", 2d);

    /** Constant <code>FAILSAFE_RETRY_MAX_ATTEMPTS</code> */
    Option<Integer> FAILSAFE_RETRY_MAX_ATTEMPTS = Option.createOption("queryFailsafeMaxAttempts", 3);

    /** Constant <code>FAILSAFE_RETRY_DELAY</code> */
    Option<Long> FAILSAFE_RETRY_DELAY = Option.createOption("queryFailsafeDelay", 1000L);
}
