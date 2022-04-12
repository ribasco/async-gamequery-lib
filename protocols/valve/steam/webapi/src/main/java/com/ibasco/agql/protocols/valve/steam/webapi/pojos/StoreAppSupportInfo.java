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

package com.ibasco.agql.protocols.valve.steam.webapi.pojos;

/**
 * <p>StoreAppSupportInfo class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class StoreAppSupportInfo {
    private String url;
    private String email;

    /**
     * <p>Getter for the field <code>url</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getUrl() {
        return url;
    }

    /**
     * <p>Setter for the field <code>url</code>.</p>
     *
     * @param url a {@link java.lang.String} object
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * <p>Getter for the field <code>email</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getEmail() {
        return email;
    }

    /**
     * <p>Setter for the field <code>email</code>.</p>
     *
     * @param email a {@link java.lang.String} object
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
