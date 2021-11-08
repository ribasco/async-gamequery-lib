/*
 * MIT License
 *
 * Copyright (c) 2018 Asynchronous Game Query Library
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ibasco.agql.protocols.valve.steam.webapi.interfaces;

import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreAppDetails;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

public class SteamStorefrontIT {

    private final SteamWebApiClient apiClient;

    private final SteamStorefront storeFront;

    public SteamStorefrontIT() throws IOException, URISyntaxException {
        Properties testProps = new Properties();
        testProps.load(getClass().getClassLoader().getResource("test.properties").openStream());
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