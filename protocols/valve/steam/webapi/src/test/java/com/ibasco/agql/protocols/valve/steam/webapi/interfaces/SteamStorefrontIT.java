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

package com.ibasco.agql.protocols.valve.steam.webapi.interfaces;

import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreAppDetails;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Properties;

public class SteamStorefrontIT {

    private final SteamWebApiClient apiClient;

    private final SteamStorefront storeFront;

    public SteamStorefrontIT() throws IOException, URISyntaxException {
        Properties testProps = new Properties();
        testProps.load(Objects.requireNonNull(getClass().getClassLoader().getResource("test.properties")).openStream());
        apiClient = new SteamWebApiClient(testProps.getProperty("web.token"));
        storeFront = new SteamStorefront(apiClient);
    }

    @Test
    public void testEmptyAppDetails() {
        StoreAppDetails details = storeFront.getAppDetails(1).join();
        assertNull(details);
    }

    @Test
    public void testGetAppDetails9() {
        StoreAppDetails details = storeFront.getAppDetails(10).join();
        assertNotNull(details);
        assertEquals(10, details.getAppId());
    }

    @Test
    public void testGetAppDetails410() {
        StoreAppDetails details = storeFront.getAppDetails(410).join();
        assertNotNull(details);
        assertEquals(410, details.getAppId());
    }

    @Test
    public void testGetAppDetails550() {
        StoreAppDetails details = storeFront.getAppDetails(550).join();
        assertNotNull(details);
        assertEquals(550, details.getAppId());
    }
}