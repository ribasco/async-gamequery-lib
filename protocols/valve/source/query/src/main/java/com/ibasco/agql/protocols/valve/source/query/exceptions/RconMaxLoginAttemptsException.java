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

package com.ibasco.agql.protocols.valve.source.query.exceptions;

import java.net.InetSocketAddress;

public class RconMaxLoginAttemptsException extends RconAuthException {

    public RconMaxLoginAttemptsException(InetSocketAddress address) {
        super(address);
    }

    public RconMaxLoginAttemptsException(String message, InetSocketAddress address) {
        super(message, address);
    }

    public RconMaxLoginAttemptsException(String message, Throwable cause, InetSocketAddress address) {
        super(message, cause, address);
    }
}
