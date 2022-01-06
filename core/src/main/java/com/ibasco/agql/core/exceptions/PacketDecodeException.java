/*
 * Copyright 2022-2022 Asynchronous Game Query Library
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

package com.ibasco.agql.core.exceptions;

public class PacketDecodeException extends Exception {

    public PacketDecodeException() {
    }

    public PacketDecodeException(String message) {
        super(message);
    }

    public PacketDecodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PacketDecodeException(Throwable cause) {
        super(cause);
    }

    public PacketDecodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}