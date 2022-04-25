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

package com.ibasco.agql.protocols.valve.steam.master;

import com.ibasco.agql.core.util.*;

/**
 * Configuration options container for the Master Server query module
 *
 * @author Rafael Luis Ibasco
 * @see Options
 * @see OptionBuilder
 * @see MasterServerQueryClient
 */
@Inherit(options = FailsafeOptions.class)
public final class MasterServerOptions extends AbstractOptions {

    /**
     * <p>Create a new {@link OptionBuilder} for {@link MasterServerOptions}</p>
     *
     * @return a newly instantiated {@link com.ibasco.agql.core.util.OptionBuilder} object
     */
    public static OptionBuilder<MasterServerOptions> builder() {
        return OptionBuilder.newBuilder(MasterServerOptions.class);
    }

}
