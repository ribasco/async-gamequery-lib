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

package com.ibasco.agql.protocols.valve.csgo.webapi.interfaces;

import com.ibasco.agql.protocols.valve.csgo.webapi.CsgoApiConstants;
import com.ibasco.agql.protocols.valve.csgo.webapi.CsgoWebApiRequest;

/**
 * <p>Abstract CsgoServersRequest class.</p>
 *
 * @author Rafael Luis Ibasco
 */
abstract public class CsgoServersRequest extends CsgoWebApiRequest {

    /**
     * <p>Constructor for CsgoServersRequest.</p>
     *
     * @param apiMethod
     *         a {@link java.lang.String} object
     * @param apiVersion
     *         a int
     */
    public CsgoServersRequest(String apiMethod, int apiVersion) {
        super(CsgoApiConstants.CSGO_INTERFACE_SERVERS, apiMethod, apiVersion);
    }
}
