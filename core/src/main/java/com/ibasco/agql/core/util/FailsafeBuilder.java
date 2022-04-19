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
import dev.failsafe.event.ExecutionAttemptedEvent;
import dev.failsafe.event.ExecutionCompletedEvent;

import java.time.Duration;
import java.util.Map;
import java.util.function.BiPredicate;

/**
 * A convenience class that assists in building {@link dev.failsafe.Failsafe} policies and executors using pre-defined failsafe {@link Options}
 *
 * @author Rafael Luis Ibasco
 */
public class FailsafeBuilder {

    private static final BiPredicate<Option<?>, Object> FAILSAFE_OPTIONS_ONLY = (o, v) -> o.getKey().contains("failsafe");

    private FailsafeBuilder() {}

    public static <T> RateLimiterBuilder<T> buildRateLimiter(Options options) {
        Map<Option<?>, Object> mergedOptions = Option.merge(options, FAILSAFE_OPTIONS_ONLY);

        Boolean failsafeEnabled = findValue(FailsafeProperties.FAILSAFE_ENABLED, mergedOptions);
        if (failsafeEnabled != null && !failsafeEnabled)
            throw new IllegalStateException("Failsafe is not enabled");

        Long maxExecutions = findValue(FailsafeProperties.FAILSAFE_RATELIMIT_MAX_EXEC, mergedOptions);
        Long periodMs = findValue(FailsafeProperties.FAILSAFE_RATELIMIT_PERIOD, mergedOptions);
        Long maxWaitTimeMs = findValue(FailsafeProperties.FAILSAFE_RATELIMIT_MAX_WAIT_TIME, mergedOptions);
        RateLimitType rateLimitType = findValue(FailsafeProperties.FAILSAFE_RATELIMIT_TYPE, mergedOptions);
        //noinspection unchecked
        RateLimiterBuilder<T> builder = (RateLimiterBuilder<T>) rateLimitType.getBuilder().apply(maxExecutions, Duration.ofMillis(periodMs));
        if (maxWaitTimeMs != null)
            builder.withMaxWaitTime(Duration.ofMillis(maxWaitTimeMs));
        Console.println("Building 'RATE LIMIT POLICY' from '%s'", options.getClass().getSimpleName());
        Console.println(">> Max Executions: %d", maxExecutions);
        Console.println(">> Period: %d", periodMs);
        Console.println(">> Max Wait Time: %s", maxWaitTimeMs);
        Console.println(">> Rate Limit Type: %s", rateLimitType);
        //attachGlobalListeners(builder);
        return builder;
    }

    public static <T> RetryPolicyBuilder<T> buildRetryPolicy(Options options) {
        if (options == null)
            throw new IllegalStateException("Options must not be null");
        Map<Option<?>, Object> mergedOptions = Option.merge(options, FAILSAFE_OPTIONS_ONLY);
        RetryPolicyBuilder<T> builder = RetryPolicy.builder();

        Boolean failsafeEnabled = findValue(FailsafeProperties.FAILSAFE_ENABLED, mergedOptions);
        if (failsafeEnabled != null && !failsafeEnabled)
            throw new IllegalStateException("Failsafe is not enabled");

        Long retryDelay = findValue(FailsafeProperties.FAILSAFE_RETRY_DELAY, mergedOptions);
        Integer maxAttempts = findValue(FailsafeProperties.FAILSAFE_RETRY_MAX_ATTEMPTS, mergedOptions);
        Boolean backOffEnabled = findValue(FailsafeProperties.FAILSAFE_RETRY_BACKOFF_ENABLED, mergedOptions);
        Long backoffDelay = findValue(FailsafeProperties.FAILSAFE_RETRY_BACKOFF_DELAY, mergedOptions);
        Long backoffMaxDelay = findValue(FailsafeProperties.FAILSAFE_RETRY_BACKOFF_MAX_DELAY, mergedOptions);

        if (retryDelay != null && retryDelay > 0)
            builder.withDelay(Duration.ofMillis(retryDelay));
        if (maxAttempts != null)
            builder.withMaxAttempts(maxAttempts);
        if (backOffEnabled != null && backOffEnabled) {
            Double backoffDelayFactor = findValue(FailsafeProperties.FAILSAFE_RETRY_BACKOFF_DELAY_FACTOR, mergedOptions);
            builder.withBackoff(Duration.ofMillis(backoffDelay), Duration.ofMillis(backoffMaxDelay), backoffDelayFactor);
        }

        Console.println("Building 'RETRY POLICY' from '%s'", options.getClass().getSimpleName());
        Console.println(">> Retry delay: %d", retryDelay);
        Console.println(">> Max attempts: %d", maxAttempts);
        Console.println(">> Backoff Enabled: %s", backOffEnabled);
        Console.println(">> Backoff Delay: %d", backoffDelay);
        Console.println(">> Backoff Max Delay: %d", backoffMaxDelay);

        //attachGlobalListeners(builder);
        return builder;
    }

    public static <T> CircuitBreakerBuilder<T> buildCircuitBreaker(Options options) {
        if (options == null)
            throw new IllegalStateException("Options must not be null");
        Map<Option<?>, Object> mergedOptions = Option.merge(options, FAILSAFE_OPTIONS_ONLY);

        Boolean failsafeEnabled = findValue(FailsafeProperties.FAILSAFE_ENABLED, mergedOptions);
        if (failsafeEnabled != null && !failsafeEnabled)
            throw new IllegalStateException("Failsafe is not enabled");

        CircuitBreakerBuilder<T> builder = CircuitBreaker.builder();
        Integer delay = findValue(FailsafeProperties.FAILSAFE_CIRCBREAKER_DELAY, mergedOptions);
        Integer failureThreshold = findValue(FailsafeProperties.FAILSAFE_CIRCBREAKER_FAILURE_THRESHOLD, mergedOptions);
        Integer failureThresholdingCapacity = findValue(FailsafeProperties.FAILSAFE_CIRCBREAKER_FAILURE_THRESHOLDING_CAP, mergedOptions);
        Integer successThreshold = findValue(FailsafeProperties.FAILSAFE_CIRCBREAKER_SUCCESS_THRESHOLD, mergedOptions);

        Console.println("Building 'CIRCUIT BREAKER POLICY' for '%s'", options.getClass().getSimpleName());
        Console.println(">> Delay: %d", delay);
        Console.println(">> Failure Threshold: %d", failureThreshold);
        Console.println(">> Failure Thresholding Capacity: %d", failureThresholdingCapacity);
        Console.println(">> Success Threshold: %d", successThreshold);

        builder.withFailureThreshold(failureThreshold, failureThresholdingCapacity);
        builder.withSuccessThreshold(successThreshold);
        builder.withDelay(Duration.ofMillis(delay));
        if (Properties.isVerbose()) {
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

    private static <S, R, C extends PolicyConfig<R>, T extends PolicyBuilder<S, C, R>> void attachGlobalListeners(T builder) {
        builder.onSuccess(new EventListener<ExecutionCompletedEvent<R>>() {
            @Override
            public void accept(ExecutionCompletedEvent<R> event) throws Throwable {
                Console.println("[GLOBAL LISTENER] Successful execution: %s", event.getResult());
            }
        });
        builder.onFailure(new EventListener<ExecutionCompletedEvent<R>>() {
            @Override
            public void accept(ExecutionCompletedEvent<R> event) throws Throwable {
                Console.error("[GLOBAL LISTENER] Failed execution encountered  (Attempts: %d, Error: %s)", event.getAttemptCount(), event.getException());
            }
        });
        if (builder instanceof RetryPolicyBuilder<?>) {
            //noinspection unchecked
            RetryPolicyBuilder<R> rBuilder = (RetryPolicyBuilder<R>) builder;
            rBuilder.onRetry(new EventListener<ExecutionAttemptedEvent<R>>() {
                @Override
                public void accept(ExecutionAttemptedEvent<R> event) throws Throwable {
                    Console.error("[GLOBAL LISTENER] Retry attempt detected (Attempt count: %d, Last Exception: %s)", event.getAttemptCount(), event.getLastException());
                }
            });
        }
        //return builder;
    }

    /**
     * Attempts to find the value for the specified key in the provided options map. If the key does not exists in the provided map, it will then attempt to search the global options map.
     *
     * @param key
     *         The option key to be used as a lookup
     * @param optionMap
     *         The {@link Map} to search on
     * @param <V>
     *         The captured return type
     *
     * @return The value of the specified {@link Option}
     */
    @SuppressWarnings("unchecked")
    private static <V> V findValue(String key, Map<Option<?>, Object> optionMap) {
        if (key == null)
            throw new IllegalStateException("Key is null");
        for (Map.Entry<Option<?>, Object> entry : optionMap.entrySet()) {
            String optionKey = entry.getKey().getKey();
            if (optionKey.equalsIgnoreCase(key)) {
                //noinspection unchecked
                return (V) entry.getValue();
            }
        }
        //check if present in global options
        Option<?> globalOptionKey = Option.ofGlobal(key);
        if (globalOptionKey == null)
            throw new IllegalStateException("Option key not found: " + key);
        V globalValue = (V) Option.getGlobal(globalOptionKey);
        return globalValue == null ? (V) globalOptionKey.getDefaultValue() : globalValue;
    }
}
