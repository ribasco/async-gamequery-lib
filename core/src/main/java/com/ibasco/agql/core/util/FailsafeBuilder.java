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

import dev.failsafe.CircuitBreakerBuilder;
import dev.failsafe.RateLimiterBuilder;
import dev.failsafe.RetryPolicyBuilder;

/**
 * A convenience class that assists in building {@link dev.failsafe.Failsafe} policies and executors
 *
 * @author Rafael Luis Ibasco
 */
public class FailsafeBuilder {

    private static final String RETRY_DELAY = "retryDelay";

    private static final String RETRY_MAX_ATTEMPTS = "retryMaxAttempts";

    private static final String RETRY_BACKOFF_ENABLED = "retryBackOffEnabled";

    private static final String RETRY_BACKOFF_DELAY = "retryBackoffDelay";

    private static final String RETRY_BACKOFF_MAX_DELAY = "retryBackoffMaxDelay";

    private static final String RETRY_BACKOFF_DELAY_FACTOR = "retryBackoffDelayFactor";

    private FailsafeBuilder() {}

    public static <T> RateLimiterBuilder<T> rateLimiterBuilder(Options options) {
        return null;
    }

    public static <T> CircuitBreakerBuilder<T> circuitBreakerBuilder(Options options) {
        return null;
    }

    public static <T> RetryPolicyBuilder<T> retryPolicyBuilder(Options options) {


        /*RetryPolicyBuilder<T> builder = RetryPolicy.builder();

        Long retryDelay = options.getOrDefault(SourceQueryOptions.FAILSAFE_RETRY_DELAY);
        Integer maxAttempts = options.getOrDefault(SourceQueryOptions.FAILSAFE_RETRY_MAX_ATTEMPTS);
        Boolean backOffEnabled = options.getOrDefault(SourceQueryOptions.FAILSAFE_RETRY_BACKOFF_ENABLED);

        if (retryDelay != null)
            builder.withDelay(Duration.ofMillis(retryDelay));
        if (maxAttempts != null)
            builder.withMaxAttempts(maxAttempts);
        if (backOffEnabled != null && backOffEnabled) {
            Long backoffDelay = options.getOrDefault(SourceQueryOptions.FAILSAFE_RETRY_BACKOFF_DELAY);
            Long backoffMaxDelay = options.getOrDefault(SourceQueryOptions.FAILSAFE_RETRY_BACKOFF_MAX_DELAY);
            Double backoffDelayFactor = options.getOrDefault(SourceQueryOptions.FAILSAFE_RETRY_BACKOFF_DELAY_FACTOR);
            builder.withBackoff(Duration.ofMillis(backoffDelay), Duration.ofMillis(backoffMaxDelay), backoffDelayFactor);
        }
        builder.abortOn(RejectedExecutionException.class);
        builder.onRetriesExceeded(retryExceededListener);
        return builder;*/
        return null;
    }
}
