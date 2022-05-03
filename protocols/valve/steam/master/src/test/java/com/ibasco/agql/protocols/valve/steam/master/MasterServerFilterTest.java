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

import com.ibasco.agql.core.util.Strings;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MasterServerFilterTest {

    private MasterServerFilter filter;

    @BeforeEach
    void setUp() {
        filter = MasterServerFilter.create();
    }

    @Test
    @DisplayName("Ensure there are no duplicate entries")
    void testNoDuplicates() {
        filter.appId(550);
        filter.appId(730);

        filter.dedicated(true);
        filter.dedicated(false);

        String result = filter.toString();

        assertNotNull(result);
        assertEquals("\\dedicated\\0\\appId\\730", result);
    }

    @Test
    @DisplayName("Test 'all servers' criteria")
    void testAllServers() {
        //filter.gamedata("confogl", "versus");
        filter.appId(550).allServers().gametypes("coop", "empty", "secure");
        assertEquals(Strings.EMPTY, filter.toString());
    }

    @Test
    @DisplayName("Test server tags")
    void testTags() {
        //filter.gamedata("confogl", "versus");
        filter.appId(550).gametypes("coop", "empty", "secure");
        String result = filter.toString();
        assertEquals("\\appId\\550\\gametype\\coop,empty,secure", result);
    }

    @Test
    @DisplayName("Test special filters")
    void testSpecialFilters() {
        filter.appId(550).nand().dedicated(true).nor().isWhitelisted(true);
        assertEquals("\\nor\\dedicated\\1\\nand\\white\\1\\appId\\550", filter.toString());
    }
}