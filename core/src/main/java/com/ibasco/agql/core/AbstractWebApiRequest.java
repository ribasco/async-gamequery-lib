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
