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

package com.ibasco.agql.protocols.valve.steam.master;

import com.ibasco.agql.core.enums.RateLimitType;
import com.ibasco.agql.core.util.*;
import dev.failsafe.Failsafe;

/**
 * Options for Master Server Query
 *
 * @author Rafael Luis Ibasco
 * @see Options
 * @see OptionBuilder
 */
public final class MasterServerOptions extends AbstractOptions {

    /**
     * Enable {@link Failsafe} integration for Master Query module
     *
     * @see <a href="https://failsafe.dev/">Failsafe</a>
     */
    public static Option<Boolean> FAILSAFE_ENABLED = Option.createOption(FailsafeOptions.FAILSAFE_ENABLED, true);

    /**
     * Enable Rate Limiter (Failsafe)
     *
     * @see #FAILSAFE_RATELIMIT_MAX_EXEC
     * @see #FAILSAFE_RATELIMIT_PERIOD
     * @see #FAILSAFE_RATELIMIT_MAX_WAIT_TIME
     * @see <a href="https://failsafe.dev/rate-limiter/">Failsafe's Rate Limiter</a>
     */
    public static Option<Boolean> FAILSAFE_RATELIMIT_ENABLED = Option.createOption(FailsafeOptions.FAILSAFE_RATELIMIT_ENABLED, true);

    /**
     * Maximum number of executions within the specified period (Default is 10 executions per minute)
     *
     * @see #FAILSAFE_RATELIMIT_PERIOD
     * @see <a href="https://failsafe.dev/rate-limiter/">Failsafe's Rate Limiter</a>
     */
    public static Option<Long> FAILSAFE_RATELIMIT_MAX_EXEC = Option.createOption(FailsafeOptions.FAILSAFE_RATELIMIT_MAX_EXEC, 15L);

    /**
     * Specifies the rate limiting method to use (Default is Smooth)
     *
     * @see RateLimitType
     * @see <a href="https://failsafe.dev/rate-limiter/#rate-limiter">Failsafe's Rate Limiter</a>
     * @see <a href="https://failsafe.dev/rate-limiter/">Failsafe's Rate Limiter</a>
     */
    public static Option<RateLimitType> FAILSAFE_RATELIMIT_TYPE = Option.createOption(FailsafeOptions.FAILSAFE_RATELIMIT_TYPE, RateLimitType.SMOOTH);

    /**
     * The period after which permitted executions are reset to the max executions. (Default is 60000 ms or 1 minute)
     *
     * @see #FAILSAFE_RATELIMIT_MAX_EXEC
     * @see <a href="https://failsafe.dev/rate-limiter/">Failsafe's Rate Limiter</a>
     */
    public static Option<Long> FAILSAFE_RATELIMIT_PERIOD = Option.createOption(FailsafeOptions.FAILSAFE_RATELIMIT_PERIOD, 60000L);

    /**
     * Maximum waiting time for permits to be available (Default is 3000 ms)
     *
     * @see <a href="https://failsafe.dev/rate-limiter/#waiting">Failsafe's Rate Limiter (Waiting)</a>
     */
    public static Option<Long> FAILSAFE_RATELIMIT_MAX_WAIT_TIME = Option.createOption(FailsafeOptions.FAILSAFE_RATELIMIT_MAX_WAIT_TIME, 3000L);

    /**
     * Enable retry policy
     *
     * @see #FAILSAFE_RETRY_BACKOFF_ENABLED
     * @see #FAILSAFE_RETRY_BACKOFF_DELAY
     * @see #FAILSAFE_RETRY_BACKOFF_MAX_DELAY
     * @see #FAILSAFE_RETRY_BACKOFF_DELAY_FACTOR
     * @see #FAILSAFE_RETRY_MAX_ATTEMPTS
     * @see <a href="https://failsafe.dev/retry/">Failsafe's Retry Policy</a>
     */
    public static Option<Boolean> FAILSAFE_RETRY_ENABLED = Option.createOption(FailsafeOptions.FAILSAFE_RETRY_ENABLED, true);

    /**
     * Delay between retries (In milliseconds. Use -1 to disable)
     *
     * @see <a href="https://failsafe.dev/retry">Failsafe's Retry Policy</a>
     */
    public static Option<Long> FAILSAFE_RETRY_DELAY = Option.createOption(FailsafeOptions.FAILSAFE_RETRY_DELAY, -1L);

    /**
     * Sets the max number of execution attempts to perform. -1 indicates no limit (Default is 3 attempts)
     *
     * @see <a href="https://failsafe.dev/retry">Failsafe's Retry Policy</a>
     */
    public static Option<Integer> FAILSAFE_RETRY_MAX_ATTEMPTS = Option.createOption(FailsafeOptions.FAILSAFE_RETRY_MAX_ATTEMPTS, 3);

    /**
     * Enable Failsafe's Retry Backoff Feature
     *
     * @see #FAILSAFE_RETRY_BACKOFF_DELAY
     * @see #FAILSAFE_RETRY_BACKOFF_MAX_DELAY
     * @see #FAILSAFE_RETRY_BACKOFF_DELAY_FACTOR
     * @see <a href="https://failsafe.dev/retry/#delays">Failsafe's Retry Policy (Backoff)</a>
     */
    public static Option<Boolean> FAILSAFE_RETRY_BACKOFF_ENABLED = Option.createOption(FailsafeOptions.FAILSAFE_RETRY_BACKOFF_ENABLED, true);

    /**
     * Sets the delay between retries (milliseconds), exponentially backing off to the maxDelay and multiplying successive delays by the delayFactor. Replaces any previously configured fixed or random delays.
     *
     * @see #FAILSAFE_RETRY_BACKOFF_MAX_DELAY
     * @see #FAILSAFE_RETRY_BACKOFF_DELAY_FACTOR
     * @see <a href="https://failsafe.dev/retry/#delays">Failsafe's Retry Policy (Backoff)</a>
     */
    public static Option<Long> FAILSAFE_RETRY_BACKOFF_DELAY = Option.createOption(FailsafeOptions.FAILSAFE_RETRY_BACKOFF_DELAY, 5000L);

    /**
     * Sets the delay between retries (milliseconds), exponentially backing off to the maxDelay and multiplying successive delays by the delayFactor. Replaces any previously configured fixed or random delays. (Default is 60000 ms or 1 minute)
     *
     * @see #FAILSAFE_RETRY_BACKOFF_ENABLED
     * @see #FAILSAFE_RETRY_BACKOFF_DELAY
     * @see #FAILSAFE_RETRY_BACKOFF_DELAY_FACTOR
     * @see <a href="https://failsafe.dev/retry/#delays">Failsafe's Retry Policy (Backoff)</a>
     */
    public static Option<Long> FAILSAFE_RETRY_BACKOFF_MAX_DELAY = Option.createOption(FailsafeOptions.FAILSAFE_RETRY_BACKOFF_MAX_DELAY, 60000L);

    /**
     * Sets the delay between retries, exponentially backing off to the maxDelay and multiplying successive delays by the delayFactor. Replaces any previously configured fixed or random delays. (Default is 5.0)
     *
     * @see #FAILSAFE_RETRY_BACKOFF_ENABLED
     * @see <a href="https://failsafe.dev/retry/#delays">Failsafe's Retry Policy (Backoff)</a>
     */
    public static Option<Double> FAILSAFE_RETRY_BACKOFF_DELAY_FACTOR = Option.createOption(FailsafeOptions.FAILSAFE_RETRY_BACKOFF_DELAY_FACTOR, 5d);

}
