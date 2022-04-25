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

    /** Constant <code>FAILSAFE_ENABLED="failsafeEnabled"</code> */
    String FAILSAFE_ENABLED = "failsafeEnabled";

    //<editor-fold desc="Rate Limit Policy">

    /** Constant <code>FAILSAFE_RATELIMIT_ENABLED="failsafeRateLimitEnabled"</code> */
    String FAILSAFE_RATELIMIT_ENABLED = "failsafeRateLimitEnabled";

    /** Constant <code>FAILSAFE_RATELIMIT_MAX_EXEC="failsafeRateLimitMaxExec"</code> */
    String FAILSAFE_RATELIMIT_MAX_EXEC = "failsafeRateLimitMaxExec";

    /** Constant <code>FAILSAFE_RATELIMIT_TYPE="failsafeRateLimitType"</code> */
    String FAILSAFE_RATELIMIT_TYPE = "failsafeRateLimitType";

    /** Constant <code>FAILSAFE_RATELIMIT_PERIOD="failsafeRateLimitPeriod"</code> */
    String FAILSAFE_RATELIMIT_PERIOD = "failsafeRateLimitPeriod";

    /** Constant <code>FAILSAFE_RATELIMIT_MAX_WAIT_TIME="failsafeRateLimitMaxWaitTime"</code> */
    String FAILSAFE_RATELIMIT_MAX_WAIT_TIME = "failsafeRateLimitMaxWaitTime";
    //</editor-fold>

    //<editor-fold desc="Retry Policy">

    /** Constant <code>FAILSAFE_RETRY_ENABLED="failsafeRetryRetryEnabled"</code> */
    String FAILSAFE_RETRY_ENABLED = "failsafeRetryRetryEnabled";

    /** Constant <code>FAILSAFE_RETRY_DELAY="failsafeRetryDelay"</code> */
    String FAILSAFE_RETRY_DELAY = "failsafeRetryDelay";

    /** Constant <code>FAILSAFE_RETRY_MAX_ATTEMPTS="failsafeRetryMaxAttempts"</code> */
    String FAILSAFE_RETRY_MAX_ATTEMPTS = "failsafeRetryMaxAttempts";

    /** Constant <code>FAILSAFE_RETRY_BACKOFF_ENABLED="failsafeRetryBackoffEnabled"</code> */
    String FAILSAFE_RETRY_BACKOFF_ENABLED = "failsafeRetryBackoffEnabled";

    /** Constant <code>FAILSAFE_RETRY_BACKOFF_DELAY="failsafeRetryBackoffDelay"</code> */
    String FAILSAFE_RETRY_BACKOFF_DELAY = "failsafeRetryBackoffDelay";

    /** Constant <code>FAILSAFE_RETRY_BACKOFF_MAX_DELAY="failsafeRetryBackoffMaxDelay"</code> */
    String FAILSAFE_RETRY_BACKOFF_MAX_DELAY = "failsafeRetryBackoffMaxDelay";

    /** Constant <code>FAILSAFE_RETRY_BACKOFF_DELAY_FACTOR="failsafeRetryBackoffDelayFactor"</code> */
    String FAILSAFE_RETRY_BACKOFF_DELAY_FACTOR = "failsafeRetryBackoffDelayFactor";

    /** Constant <code>FAILSAFE_CIRCBREAKER_ENABLED="failsafeCircuitBreakerEnabled"</code> */
    String FAILSAFE_CIRCBREAKER_ENABLED = "failsafeCircuitBreakerEnabled";

    /** Constant <code>FAILSAFE_CIRCBREAKER_FAILURE_THRESHOLD="failsafeCircuitBreakerFailureThreshold"</code> */
    String FAILSAFE_CIRCBREAKER_FAILURE_THRESHOLD = "failsafeCircuitBreakerFailureThreshold";

    /** Constant <code>FAILSAFE_CIRCBREAKER_FAILURE_THRESHOLDING_CAP="failsafeCircuitBreakerFailureThresholdi"{trunked}</code> */
    String FAILSAFE_CIRCBREAKER_FAILURE_THRESHOLDING_CAP = "failsafeCircuitBreakerFailureThresholdingCapacity";

    /** Constant <code>FAILSAFE_CIRCBREAKER_DELAY="failsafeCircuitBreakerDelay"</code> */
    String FAILSAFE_CIRCBREAKER_DELAY = "failsafeCircuitBreakerDelay";

    /** Constant <code>FAILSAFE_CIRCBREAKER_SUCCESS_THRESHOLD="failsafeCircuitBreakerSuccessThreshold"</code> */
    String FAILSAFE_CIRCBREAKER_SUCCESS_THRESHOLD = "failsafeCircuitBreakerSuccessThreshold";
    //</editor-fold>

}
