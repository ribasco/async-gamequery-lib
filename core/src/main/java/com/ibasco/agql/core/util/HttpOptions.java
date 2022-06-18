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
 * <p>HttpOptions class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class HttpOptions extends AbstractOptions {

    /**
     * The authentication token to use
     */
    public static final Option<String> API_KEY = Option.create("webApiKey", null);

    /**
     * <p>Create a new {@link OptionBuilder} for {@link HttpOptions}</p>
     *
     * @return a newly instantiated {@link com.ibasco.agql.core.util.OptionBuilder} object
     */
    public static OptionBuilder<HttpOptions> builder() {
        return OptionBuilder.newBuilder(HttpOptions.class);
    }
}
