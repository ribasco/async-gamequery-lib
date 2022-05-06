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

package com.ibasco.agql.protocols.valve.dota2.webapi.interfaces;

import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;

/**
 * <p>Dota2 class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public final class Dota2 {

    /**
     * <p>createEcon.</p>
     *
     * @param client
     *         a {@link com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient} object
     *
     * @return a {@link com.ibasco.agql.protocols.valve.dota2.webapi.interfaces.Dota2Econ} object
     */
    public static Dota2Econ createEcon(SteamWebApiClient client) {
        return new Dota2Econ(client);
    }

    /**
     * <p>createFantasy.</p>
     *
     * @param client
     *         a {@link com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient} object
     *
     * @return a {@link com.ibasco.agql.protocols.valve.dota2.webapi.interfaces.Dota2Fantasy} object
     */
    public static Dota2Fantasy createFantasy(SteamWebApiClient client) {
        return new Dota2Fantasy(client);
    }

    /**
     * <p>createMatch.</p>
     *
     * @param client
     *         a {@link com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient} object
     *
     * @return a {@link com.ibasco.agql.protocols.valve.dota2.webapi.interfaces.Dota2Match} object
     */
    public static Dota2Match createMatch(SteamWebApiClient client) {
        return new Dota2Match(client);
    }
}
