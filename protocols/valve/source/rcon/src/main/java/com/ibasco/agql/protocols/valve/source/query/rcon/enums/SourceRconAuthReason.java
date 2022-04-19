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

package com.ibasco.agql.protocols.valve.source.query.rcon.enums;

/**
 * An enumeration for identifying the type of authentication failure
 *
 * @author Rafael Luis Ibasco
 */
public enum SourceRconAuthReason {
    /**
     * Credentials have been invalidated for the address. Must re-authenticate with server with new Credentials
     */
    INVALIDATED,
    /**
     * Credentials supplied is invalid
     */
    INVALID_CREDENTIALS,
    /**
     * Connection has been dropped by the remote server
     */
    CONNECTION_DROPPED,
    /**
     * An attempt to execute a command request on an address that has not yet been authenticated by the server.
     */
    NOT_AUTHENTICATED
}
