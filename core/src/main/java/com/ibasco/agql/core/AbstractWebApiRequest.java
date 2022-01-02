/*
 * Copyright 2018-2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
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

abstract public class AbstractWebApiRequest extends AbstractWebRequest {

    public static final Logger log = LoggerFactory.getLogger(AbstractWebApiRequest.class);

    private final Map<String, String> baseUrlParams;

    private final StrSubstitutor substitutor;

    private int apiVersion;

    private String baseUrlFormat;

    public AbstractWebApiRequest(int apiVersion) {
        this.apiVersion = apiVersion;
        this.baseUrlParams = new HashMap<>();
        this.substitutor = new StrSubstitutor(this.baseUrlParams);
    }

    @SuppressWarnings("unchecked")
    protected void urlParam(AbstractCriteriaBuilder criteria) {
        Set<Map.Entry<String, Object>> criteraSet = criteria.getCriteriaSet();
        criteraSet.forEach(c -> urlParam(c.getKey(), c.getValue()));
    }

    public int apiVersion() {
        return apiVersion;
    }

    public void apiVersion(int apiVersion) {
        this.apiVersion = apiVersion;
    }

    protected boolean hasProperty(String property) {
        return property(property) != null;
    }

    protected void property(String property, Object value) {
        if (!StringUtils.isEmpty(property) && value != null)
            baseUrlParams.put(property, String.valueOf(value));
    }

    protected String property(String property) {
        return baseUrlParams.get(property);
    }

    protected String resolveProperties(String textWithVariables) {
        return substitutor.replace(textWithVariables);
    }

    public String resolveBaseUrl() {
        return resolveProperties(this.baseUrlFormat);
    }

    public String baseUrlFormat() {
        return baseUrlFormat;
    }

    public void baseUrlFormat(String baseUrlFormat) {
        this.baseUrlFormat = baseUrlFormat;
    }

    @Override
    public Request getMessage() {
        baseUrl(resolveBaseUrl());
        return super.getMessage();
    }
}
