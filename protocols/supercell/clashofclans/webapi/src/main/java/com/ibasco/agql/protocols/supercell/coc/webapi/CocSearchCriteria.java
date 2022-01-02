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

package com.ibasco.agql.protocols.supercell.coc.webapi;

import com.ibasco.agql.protocols.supercell.coc.webapi.enums.CocWarFrequency;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A Builder class for Clan Search Criteria
 */
public class CocSearchCriteria {
    private final Map<String, Object> criteria = new HashMap<>();

    /**
     * <p>A factory method to create a {@link CocSearchCriteria} instance</p>
     *
     * @return A {@link CocSearchCriteria} instance
     */
    public static CocSearchCriteria create() {
        return new CocSearchCriteria();
    }

    /**
     * <p>
     * Search clans by name. If name is used as part of search query, it needs to be at least three characters long.
     * Name search parameter is interpreted as wild card search, so it may appear anywhere in the clan name.
     * </p>
     *
     * @param name
     *
     * @return A {@link CocSearchCriteria} instance
     */
    public CocSearchCriteria name(String name) {
        criteria.put("name", name);
        return this;
    }

    /**
     * <p>Filter by clan war frequency</p>
     *
     * @param frequency
     *
     * @return A {@link CocSearchCriteria} instance
     */
    public CocSearchCriteria warFrequency(CocWarFrequency frequency) {
        criteria.put("warFrequency", frequency.getCode());
        return this;
    }

    /**
     * <p>Filter by clan location identifier. For list of available locations, refer to getLocations operation.</p>
     *
     * @param locationId
     *
     * @return A {@link CocSearchCriteria} instance
     */
    public CocSearchCriteria locationId(int locationId) {
        criteria.put("locationId", locationId);
        return this;
    }

    /**
     * <p>Filter by minimum amount of clan members.</p>
     *
     * @param minMembers
     *
     * @return A {@link CocSearchCriteria} instance
     */
    public CocSearchCriteria minMembers(int minMembers) {
        criteria.put("minMembers", minMembers);
        return this;
    }

    /**
     * <p>Filter by maximum amount of clan members.</p>
     *
     * @param maxMembers
     *
     * @return A {@link CocSearchCriteria} instance
     */
    public CocSearchCriteria maxMembers(int maxMembers) {
        criteria.put("maxMembers", maxMembers);
        return this;
    }

    /**
     * <p>Filter by minimum amount of clan points.</p>
     *
     * @param minClanPoints
     *
     * @return A {@link CocSearchCriteria} instance
     */
    public CocSearchCriteria minClanPoints(int minClanPoints) {
        criteria.put("minClanPoints", minClanPoints);
        return this;
    }

    /**
     * <p>Filter by minimum clan level.</p>
     *
     * @param minClanLevel
     *
     * @return A {@link CocSearchCriteria} instance
     */
    public CocSearchCriteria minClanLevel(int minClanLevel) {
        criteria.put("minClanLevel", minClanLevel);
        return this;
    }

    /**
     * <p>Limit the number of items returned in the response.</p>
     *
     * @param limit
     *
     * @return A {@link CocSearchCriteria} instance
     */
    public CocSearchCriteria limit(int limit) {
        criteria.put("limit", limit);
        return this;
    }

    /**
     * <p>
     * Return only items that occur after this marker. After marker can be found from the response, inside the 'paging'
     * property.
     * Note that only after or before can be specified for a request, not both.
     * </p>
     *
     * @param after
     *
     * @return A {@link CocSearchCriteria} instance
     */
    public CocSearchCriteria after(int after) {
        criteria.put("after", after);
        return this;
    }

    /**
     * <p>
     * Return only items that occur before this marker. Before marker can be found from the response, inside the
     * 'paging' property.
     * Note that only after or before can be specified for a request, not both.
     * </p>
     *
     * @param before
     *
     * @return A {@link CocSearchCriteria} instance
     */
    public CocSearchCriteria before(int before) {
        criteria.put("before", before);
        return this;
    }

    /**
     * <p>Returns the underlying set containing all the search criterias</p>
     *
     * @return A {@link Set} containing all the search criterias wrapped within a {@link java.util.Map.Entry}
     */
    public Set<Map.Entry<String, Object>> getCriteriaSet() {
        return criteria.entrySet();
    }
}
