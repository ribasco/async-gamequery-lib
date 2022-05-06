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

import org.jetbrains.annotations.ApiStatus;

/**
 * Created by raffy on 10/27/2016.
 *
 * @author Rafael Luis Ibasco
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
public class SteamKeyValuePair<K, V> {

    private K name;

    private V value;

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return a K object
     */
    public K getName() {
        return name;
    }

    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name
     *         a K object
     */
    public void setName(K name) {
        this.name = name;
    }

    /**
     * <p>Getter for the field <code>value</code>.</p>
     *
     * @return a V object
     */
    public V getValue() {
        return value;
    }

    /**
     * <p>Setter for the field <code>value</code>.</p>
     *
     * @param value
     *         a V object
     */
    public void setValue(V value) {
        this.value = value;
    }
}
