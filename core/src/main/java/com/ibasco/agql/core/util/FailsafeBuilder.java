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

import com.ibasco.agql.core.enums.RateLimitType;
import dev.failsafe.*;
import dev.failsafe.event.CircuitBreakerStateChangedEvent;
import dev.failsafe.event.EventListener;
import dev.failsafe.event.ExecutionCompletedEvent;

import java.time.Duration;
import java.util.function.BiPredicate;

/**
 * A convenience class that assists in building {@link dev.failsafe.Failsafe} policies and executors using pre-defined failsafe {@link com.ibasco.agql.core.util.Options}
 *
 * @author Rafael Luis Ibasco
 */
public class FailsafeBuilder {

    private static final BiPredicate<Option<?>, Object> FAILSAFE_OPTIONS_ONLY = (o, v) -> o.getKey().contains("failsafe");

    private FailsafeBuilder() {}

    /**
     * <p>buildRateLimiter.</p>
     *
     * @param options
     *         a {@link com.ibasco.agql.core.util.Options} object
     * @param <T>
     *         a T class
     *
     * @return a {@link dev.failsafe.RateLimiterBuilder} object
     */
    public static <T> RateLimiterBuilder<T> buildRateLimiter(Class<? extends Options> context, Options options) {
        Boolean failsafeEnabled = options.getOrDefault(FailsafeOptions.FAILSAFE_ENABLED, context);
        if (failsafeEnabled != null && !failsafeEnabled)
            throw new IllegalStateException("Failsafe is not enabled");

        Long maxExecutions = options.getOrDefault(FailsafeOptions.FAILSAFE_RATELIMIT_MAX_EXEC, context);
        Long periodMs = options.getOrDefault(FailsafeOptions.FAILSAFE_RATELIMIT_PERIOD, context);
        Long maxWaitTimeMs = options.getOrDefault(FailsafeOptions.FAILSAFE_RATELIMIT_MAX_WAIT_TIME, context);
        RateLimitType rateLimitType = options.getOrDefault(FailsafeOptions.FAILSAFE_RATELIMIT_TYPE, context);
        //noinspection unchecked
        RateLimiterBuilder<T> builder = (RateLimiterBuilder<T>) rateLimitType.getBuilder().apply(maxExecutions, Duration.ofMillis(periodMs));
        if (maxWaitTimeMs != null)
            builder.withMaxWaitTime(Duration.ofMillis(maxWaitTimeMs));
        if (Properties.isVerbose()) {
            Console.println("Building 'RATE LIMIT POLICY' from '%s'", options.getClass().getSimpleName());
            Console.println(">> Max Executions: %d", maxExecutions);
            Console.println(">> Period: %d", periodMs);
            Console.println(">> Max Wait Time: %s", maxWaitTimeMs);
            Console.println(">> Rate Limit Type: %s", rateLimitType);
        }
        //attachGlobalListeners(builder);
        return builder;
    }

    /**
     * <p>buildRetryPolicy.</p>
     *
     * @param options
     *         a {@link com.ibasco.agql.core.util.Options} object
     * @param <T>
     *         a T class
     *
     * @return a {@link dev.failsafe.RetryPolicyBuilder} object
     */
    public static <T> RetryPolicyBuilder<T> buildRetryPolicy(Class<? extends Options> context, Options options) {
        if (options == null)
            throw new IllegalStateException("Options must not be null");
        RetryPolicyBuilder<T> builder = RetryPolicy.builder();

        Boolean failsafeEnabled = options.getOrDefault(FailsafeOptions.FAILSAFE_ENABLED, context);

        if (failsafeEnabled != null && !failsafeEnabled)
            throw new IllegalStateException("Failsafe is not enabled");
        Long retryDelay = options.getOrDefault(FailsafeOptions.FAILSAFE_RETRY_DELAY, context);
        Integer maxAttempts = options.getOrDefault(FailsafeOptions.FAILSAFE_RETRY_MAX_ATTEMPTS, context);
        Boolean backOffEnabled = options.getOrDefault(FailsafeOptions.FAILSAFE_RETRY_BACKOFF_ENABLED, context);
        Long backoffDelay = options.getOrDefault(FailsafeOptions.FAILSAFE_RETRY_BACKOFF_DELAY, context);
        Long backoffMaxDelay = options.getOrDefault(FailsafeOptions.FAILSAFE_RETRY_BACKOFF_MAX_DELAY, context);

        if (retryDelay != null && retryDelay > 0)
            builder.withDelay(Duration.ofMillis(retryDelay));
        if (maxAttempts != null)
            builder.withMaxAttempts(maxAttempts);
        if (backOffEnabled != null && backOffEnabled) {
            Double backoffDelayFactor = options.getOrDefault(FailsafeOptions.FAILSAFE_RETRY_BACKOFF_DELAY_FACTOR, context);
            builder.withBackoff(Duration.ofMillis(backoffDelay), Duration.ofMillis(backoffMaxDelay), backoffDelayFactor);
        }
        if (Properties.isVerbose()) {
            Console.println("Building 'RETRY POLICY' from '%s'", options.getClass().getSimpleName());
            Console.println(">> Retry delay: %d", retryDelay);
            Console.println(">> Max attempts: %d", maxAttempts);
            Console.println(">> Backoff Enabled: %s", backOffEnabled);
            Console.println(">> Backoff Delay: %d", backoffDelay);
            Console.println(">> Backoff Max Delay: %d", backoffMaxDelay);
        }
        //attachGlobalListeners(builder);
        return builder;
    }

    /**
     * <p>buildCircuitBreaker.</p>
     *
     * @param options
     *         a {@link com.ibasco.agql.core.util.Options} object
     * @param <T>
     *         a T class
     *
     * @return a {@link dev.failsafe.CircuitBreakerBuilder} object
     */
    public static <T> CircuitBreakerBuilder<T> buildCircuitBreaker(Class<? extends Options> context, Options options) {
        if (options == null)
            throw new IllegalStateException("Options must not be null");

        Boolean failsafeEnabled = options.getOrDefault(FailsafeOptions.FAILSAFE_ENABLED, context);
        if (failsafeEnabled != null && !failsafeEnabled)
            throw new IllegalStateException("Failsafe is not enabled");

        CircuitBreakerBuilder<T> builder = CircuitBreaker.builder();
        Integer delay = options.getOrDefault(FailsafeOptions.FAILSAFE_CIRCBREAKER_DELAY, context);
        Integer failureThreshold = options.getOrDefault(FailsafeOptions.FAILSAFE_CIRCBREAKER_FAILURE_THRESHOLD, context);
        Integer failureThresholdingCapacity = options.getOrDefault(FailsafeOptions.FAILSAFE_CIRCBREAKER_FAILURE_THRESHOLDING_CAP, context);
        Integer successThreshold = options.getOrDefault(FailsafeOptions.FAILSAFE_CIRCBREAKER_SUCCESS_THRESHOLD, context);
        builder.withFailureThreshold(failureThreshold, failureThresholdingCapacity);
        builder.withSuccessThreshold(successThreshold);
        builder.withDelay(Duration.ofMillis(delay));

        //debugging purposes only
        if (Properties.isVerbose()) {
            Console.println("Building 'CIRCUIT BREAKER POLICY' for '%s'", options.getClass().getSimpleName());
            Console.println(">> Delay: %d", delay);
            Console.println(">> Failure Threshold: %d", failureThreshold);
            Console.println(">> Failure Thresholding Capacity: %d", failureThresholdingCapacity);
            Console.println(">> Success Threshold: %d", successThreshold);
            builder.onOpen(new EventListener<CircuitBreakerStateChangedEvent>() {
                @Override
                public void accept(CircuitBreakerStateChangedEvent event) throws Throwable {
                    Console.println("[CIRCUIT-BREAKER] Circuit breaker is now " + Console.color(Console.RED, "OPEN") + " (Previous state: %s)", event.getPreviousState());
                }
            });
            builder.onHalfOpen(new EventListener<CircuitBreakerStateChangedEvent>() {
                @Override
                public void accept(CircuitBreakerStateChangedEvent event) throws Throwable {
                    Console.println("[CIRCUIT-BREAKER] Circuit breaker is now " + Console.color(Console.YELLOW, "HALF-OPEN") + " (Previous state: %s)", event.getPreviousState());
                }
            });
            builder.onClose(new EventListener<CircuitBreakerStateChangedEvent>() {
                @Override
                public void accept(CircuitBreakerStateChangedEvent event) throws Throwable {
                    Console.println("[CIRCUIT-BREAKER] Circuit breaker is now " + Console.color(Console.GREEN, "CLOSED") + " (Previous state: %s)", event.getPreviousState());
                }
            });
            builder.onFailure(new EventListener<ExecutionCompletedEvent<T>>() {
                @Override
                public void accept(ExecutionCompletedEvent<T> event) throws Throwable {
                    Console.println("[CIRCUIT-BREAKER] Circuit breaker is now " + Console.color(Console.RED, "FAILED STATE") + " (Attempts: %d, Error: %s)", event.getAttemptCount(), event.getException());
                }
            });
        }
        return builder;
    }
}
