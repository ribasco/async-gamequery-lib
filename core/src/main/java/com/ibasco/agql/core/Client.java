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

package com.ibasco.agql.core;

import com.ibasco.agql.core.util.UUID;
import java.io.Closeable;
import java.util.concurrent.Executor;

/**
 * <p>Client interface.</p>
 *
 * @author Rafael Luis Ibasco
 */
public interface Client extends Closeable {

    /**
     * <p>The unique-id of this instance</p>
     *
     * @return A {@link com.ibasco.agql.core.util.UUID} object
     */
    UUID id();

    /**
     * <p>The underlying {@link Executor} used by this instance</p>
     *
     * @return The {@link java.util.concurrent.Executor} object
     */
    Executor getExecutor();
}
