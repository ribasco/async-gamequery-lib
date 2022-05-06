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

package com.ibasco.agql.protocols.valve.source.query;

import com.ibasco.agql.core.util.AbstractOptions;
import com.ibasco.agql.core.util.ConnectOptions;
import com.ibasco.agql.core.util.FailsafeOptions;
import com.ibasco.agql.core.util.Inherit;
import com.ibasco.agql.core.util.OptionBuilder;
import com.ibasco.agql.core.util.Options;

/**
 * Configuration options container for the Source Query module
 *
 * @author Rafael Luis Ibasco
 * @see OptionBuilder
 * @see Options
 * @see SourceQueryClient
 */
@Inherit(options = {FailsafeOptions.class, ConnectOptions.class})
public final class SourceQueryOptions extends AbstractOptions {

    /**
     * <p>Create a new {@link OptionBuilder} for {@link SourceQueryOptions}</p>
     *
     * @return a newly instantiated {@link com.ibasco.agql.core.util.OptionBuilder} object
     */
    public static OptionBuilder<SourceQueryOptions> builder() {
        return OptionBuilder.newBuilder(SourceQueryOptions.class);
    }
}
