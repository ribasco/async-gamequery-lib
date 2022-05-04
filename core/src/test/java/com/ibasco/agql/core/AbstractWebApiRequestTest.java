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

package com.ibasco.agql.core;

import static com.ibasco.agql.core.AbstractWebApiInterface.VERSION_1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class AbstractWebApiRequestTest {

    private AbstractWebApiRequest apiRequest;

    @BeforeEach
    void setUp() {
        apiRequest = new AbstractWebApiRequest(VERSION_1) {

            {
                baseUrlFormat("https://api.steampowered.com/${interface}/${method}/v${version}");
                property("interface", "IStoreService");
                property("method", "GetLocalizedNameForTags");
                property("version", "1");
            }
        };
    }

    @Test
    @DisplayName("Test params with values of type Collection")
    void testArrayParams() {
        apiRequest.urlParam("tagids", Arrays.asList("493", "113"));
        assertEquals("https://api.steampowered.com/IStoreService/GetLocalizedNameForTags/v1?tagids%5B0%5D=493&tagids%5B1%5D=113", apiRequest.getMessage().getUrl());
    }
}