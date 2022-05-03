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
    @DisplayName("Test no duplicates")
    void testNoDuplicates() {
        filter.appId(550);
        filter.appId(730).allServers();
        String result = filter.toString();

        assertNotNull(result);
        assertEquals("\\appId\\730", result);
    }

    @Test
    void testAllServers() {
        filter.appId(550).allServers().gametypes("versus");
        assertEquals("\\appId\\550", filter.toString());
    }
}