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

/**
 * Common {@link dev.failsafe.Failsafe} configuration option keys
 *
 * @author Rafael Luis Ibasco
 */
public interface FailsafeProperties {

    String FAILSAFE_ENABLED = "failsafeEnabled";

    //<editor-fold desc="Rate Limit Policy">
    String FAILSAFE_RATELIMIT_ENABLED = "failsafeRateLimitEnabled";

    String FAILSAFE_RATELIMIT_MAX_EXEC = "failsafeRateLimitMaxExec";

    String FAILSAFE_RATELIMIT_TYPE = "failsafeRateLimitType";

    String FAILSAFE_RATELIMIT_PERIOD = "failsafeRateLimitPeriod";

    String FAILSAFE_RATELIMIT_MAX_WAIT_TIME = "failsafeRateLimitMaxWaitTime";
    //</editor-fold>

    //<editor-fold desc="Retry Policy">
    String FAILSAFE_RETRY_ENABLED = "failsafeRetryRetryEnabled";

    String FAILSAFE_RETRY_DELAY = "failsafeRetryDelay";

    String FAILSAFE_RETRY_MAX_ATTEMPTS = "failsafeRetryMaxAttempts";

    String FAILSAFE_RETRY_BACKOFF_ENABLED = "failsafeRetryBackoffEnabled";

    String FAILSAFE_RETRY_BACKOFF_DELAY = "failsafeRetryBackoffDelay";

    String FAILSAFE_RETRY_BACKOFF_MAX_DELAY = "failsafeRetryBackoffMaxDelay";

    String FAILSAFE_RETRY_BACKOFF_DELAY_FACTOR = "failsafeRetryBackoffDelayFactor";

    String FAILSAFE_CIRCBREAKER_ENABLED = "failsafeCircuitBreakerEnabled";

    String FAILSAFE_CIRCBREAKER_FAILURE_THRESHOLD = "failsafeCircuitBreakerFailureThreshold";

    String FAILSAFE_CIRCBREAKER_FAILURE_THRESHOLDING_CAP = "failsafeCircuitBreakerFailureThresholdingCapacity";

    String FAILSAFE_CIRCBREAKER_DELAY = "failsafeCircuitBreakerDelay";

    String FAILSAFE_CIRCBREAKER_SUCCESS_THRESHOLD = "failsafeCircuitBreakerSuccessThreshold";
    //</editor-fold>

}
