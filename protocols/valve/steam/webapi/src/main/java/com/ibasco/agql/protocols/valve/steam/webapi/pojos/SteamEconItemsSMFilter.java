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

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>SteamEconItemsSMFilter class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SteamEconItemsSMFilter {

    private long id;

    private String name;

    @SerializedName("url_history_param_name")
    private String urlHistoryParamName;

    private SteamEconItemsSMFilterElement allElement;

    /**
     * A list of elements within the filter.
     */
    private List<SteamEconItemsSMFilterElement> elements = new ArrayList<>();

    private int count;

    /**
     * <p>Getter for the field <code>id</code>.</p>
     *
     * @return a long
     */
    public long getId() {
        return id;
    }

    /**
     * <p>Setter for the field <code>id</code>.</p>
     *
     * @param id
     *         a long
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name
     *         a {@link java.lang.String} object
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>Getter for the field <code>urlHistoryParamName</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getUrlHistoryParamName() {
        return urlHistoryParamName;
    }

    /**
     * <p>Setter for the field <code>urlHistoryParamName</code>.</p>
     *
     * @param urlHistoryParamName
     *         a {@link java.lang.String} object
     */
    public void setUrlHistoryParamName(String urlHistoryParamName) {
        this.urlHistoryParamName = urlHistoryParamName;
    }

    /**
     * <p>Getter for the field <code>allElement</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamEconItemsSMFilterElement} object
     */
    public SteamEconItemsSMFilterElement getAllElement() {
        return allElement;
    }

    /**
     * <p>Setter for the field <code>allElement</code>.</p>
     *
     * @param allElement
     *         a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamEconItemsSMFilterElement} object
     */
    public void setAllElement(SteamEconItemsSMFilterElement allElement) {
        this.allElement = allElement;
    }

    /**
     * <p>Getter for the field <code>elements</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<SteamEconItemsSMFilterElement> getElements() {
        return elements;
    }

    /**
     * <p>Setter for the field <code>elements</code>.</p>
     *
     * @param elements
     *         a {@link java.util.List} object
     */
    public void setElements(List<SteamEconItemsSMFilterElement> elements) {
        this.elements = elements;
    }

    /**
     * <p>Getter for the field <code>count</code>.</p>
     *
     * @return a int
     */
    public int getCount() {
        return count;
    }

    /**
     * <p>Setter for the field <code>count</code>.</p>
     *
     * @param count
     *         a int
     */
    public void setCount(int count) {
        this.count = count;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
