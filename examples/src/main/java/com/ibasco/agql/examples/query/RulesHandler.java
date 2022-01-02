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

package com.ibasco.agql.examples.query;

import com.ibasco.agql.core.util.ConcurrentUtil;
import io.netty.handler.timeout.ReadTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.Phaser;

public class RulesHandler extends ResponseHandler<Map<String, String>> {

    private static final Logger log = LoggerFactory.getLogger(RulesHandler.class);

    public RulesHandler(Phaser phaser) {
        super("Rules", phaser);
    }

    @Override
    protected void onSuccess(Map<String, String> rules) {
        log.info("RULES [SUCCESS]: {}", rules.size());
    }

    @Override
    protected void onFail(Throwable error) {
        log.error("RULES [FAIL]: {}", error.getCause().getClass().getSimpleName());
        Throwable cause = ConcurrentUtil.unwrap(error);
        if (!(cause instanceof ReadTimeoutException))
            error.printStackTrace(System.err);
    }
}