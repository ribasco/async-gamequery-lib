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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.asynchttpclient.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p>Abstract AbstractWebApiRequest class.</p>
 *
 * @author Rafael Luis Ibasco
 */
abstract public class AbstractWebApiRequest extends AbstractWebRequest {

    /** Constant <code>log</code> */
    public static final Logger log = LoggerFactory.getLogger(AbstractWebApiRequest.class);

    private final Map<String, String> baseUrlParams;

    private final StrSubstitutor substitutor;

    private int apiVersion;

    private String baseUrlFormat;

    /**
     * <p>Constructor for AbstractWebApiRequest.</p>
     *
     * @param apiVersion a int
     */
    public AbstractWebApiRequest(int apiVersion) {
        this.apiVersion = apiVersion;
        this.baseUrlParams = new HashMap<>();
        this.substitutor = new StrSubstitutor(this.baseUrlParams);
    }

    /**
     * <p>urlParam.</p>
     *
     * @param criteria a {@link com.ibasco.agql.core.AbstractCriteriaBuilder} object
     */
    @SuppressWarnings("unchecked")
    protected void urlParam(AbstractCriteriaBuilder criteria) {
        Set<Map.Entry<String, Object>> criteraSet = criteria.getCriteriaSet();
        criteraSet.forEach(c -> urlParam(c.getKey(), c.getValue()));
    }

    /**
     * <p>apiVersion.</p>
     *
     * @return a int
     */
    public int apiVersion() {
        return apiVersion;
    }

    /**
     * <p>apiVersion.</p>
     *
     * @param apiVersion a int
     */
    public void apiVersion(int apiVersion) {
        this.apiVersion = apiVersion;
    }

    /**
     * <p>hasProperty.</p>
     *
     * @param property a {@link java.lang.String} object
     * @return a boolean
     */
    protected boolean hasProperty(String property) {
        return property(property) != null;
    }

    /**
     * <p>property.</p>
     *
     * @param property a {@link java.lang.String} object
     * @param value a {@link java.lang.Object} object
     */
    protected void property(String property, Object value) {
        if (!StringUtils.isEmpty(property) && value != null)
            baseUrlParams.put(property, String.valueOf(value));
    }

    /**
     * <p>property.</p>
     *
     * @param property a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    protected String property(String property) {
        return baseUrlParams.get(property);
    }

    /**
     * <p>resolveProperties.</p>
     *
     * @param textWithVariables a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    protected String resolveProperties(String textWithVariables) {
        return substitutor.replace(textWithVariables);
    }

    /**
     * <p>resolveBaseUrl.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String resolveBaseUrl() {
        return resolveProperties(this.baseUrlFormat);
    }

    /**
     * <p>baseUrlFormat.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String baseUrlFormat() {
        return baseUrlFormat;
    }

    /**
     * <p>baseUrlFormat.</p>
     *
     * @param baseUrlFormat a {@link java.lang.String} object
     */
    public void baseUrlFormat(String baseUrlFormat) {
        this.baseUrlFormat = baseUrlFormat;
    }

    /** {@inheritDoc} */
    @Override
    public Request getMessage() {
        baseUrl(resolveBaseUrl());
        return super.getMessage();
    }
}
