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

package com.ibasco.agql.examples;

import com.ibasco.agql.examples.base.BaseWebApiAuthExample;
import com.ibasco.agql.protocols.valve.csgo.webapi.CsgoWebApiClient;
import com.ibasco.agql.protocols.valve.csgo.webapi.interfaces.CsgoServers;
import com.ibasco.agql.protocols.valve.csgo.webapi.pojos.CsgoGameMapPlaytimeInfo;
import com.ibasco.agql.protocols.valve.csgo.webapi.pojos.CsgoGameServerStatus;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>CsgoWebApiExample class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class CsgoWebApiExample extends BaseWebApiAuthExample {

    private static final Logger log = LoggerFactory.getLogger(CsgoWebApiExample.class);

    private CsgoWebApiClient client;

    /**
     * <p>main.</p>
     *
     * @param args
     *         an array of {@link java.lang.String} objects
     *
     * @throws java.lang.Exception
     *         if any.
     */
    public static void main(String[] args) throws Exception {
        try (CsgoWebApiExample example = new CsgoWebApiExample()) {
            example.run(args);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void run(String[] args) throws Exception {
        String authToken = getToken("steam");
        client = new CsgoWebApiClient(authToken);

        CsgoServers servers = new CsgoServers(client);

        CsgoGameServerStatus status = servers.getGameServerStatus().get();
        System.out.printf("Game Server Status : %s%n", status);

        CsgoGameMapPlaytimeInfo playtimeInfo = servers.getMapPlaytimeInfo("day", "competitive", "operation").join();
        System.out.printf("Playtime Info: %s%n", playtimeInfo);
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {
        close(client, "Csgo Web API client");
    }
}
