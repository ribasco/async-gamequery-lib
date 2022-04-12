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

package com.ibasco.agql.protocols.valve.source.query.common.message;

import com.ibasco.agql.core.AbstractResponse;

/**
 * Base class for all types of source query responses
 *
 * @param <T>
 *         The underlying type of the response
 * @author Rafael Luis Ibasco
 */
abstract public class SourceQueryResponse<T> extends AbstractResponse<T> {

    /**
     * <p>Constructor for SourceQueryResponse.</p>
     *
     * @param result a T object
     */
    protected SourceQueryResponse(T result) {
        super(result);
    }

    /**
     * <p>getResult.</p>
     *
     * @return The result of the query
     */
    public final T getResult() {
        return super.getResult();
    }
}
