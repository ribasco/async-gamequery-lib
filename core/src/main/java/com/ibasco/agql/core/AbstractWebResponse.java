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

import io.netty.handler.codec.http.HttpStatusClass;
import org.asynchttpclient.Response;

/**
 * <p>Abstract AbstractWebResponse class.</p>
 *
 * @author Rafael Luis Ibasco
 */
abstract public class AbstractWebResponse extends AbstractResponse<Response> {

    /**
     * <p>Constructor for AbstractWebResponse.</p>
     *
     * @param response a {@link org.asynchttpclient.Response} object
     */
    public AbstractWebResponse(Response response) {
        super(response);
    }

    /**
     * <p>getStatus.</p>
     *
     * @return a {@link io.netty.handler.codec.http.HttpStatusClass} object
     */
    public HttpStatusClass getStatus() {
        return HttpStatusClass.valueOf(getMessage().getStatusCode());
    }

    /**
     * <p>getMessage.</p>
     *
     * @return a {@link org.asynchttpclient.Response} object
     */
    public Response getMessage() {
        return super.getResult();
    }
}
