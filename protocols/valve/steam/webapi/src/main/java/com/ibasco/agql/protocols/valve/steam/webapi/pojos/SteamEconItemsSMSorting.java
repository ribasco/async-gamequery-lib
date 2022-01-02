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

package com.ibasco.agql.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

public class SteamEconItemsSMSorting {
    private List<SteamEconItemsSMSorter> sorters = new ArrayList<>();
    @SerializedName("sorting_prefabs")
    private List<SteamEconItemsSMSortingPrefab> sortingPrefabs = new ArrayList<>();

    public List<SteamEconItemsSMSorter> getSorters() {
        return sorters;
    }

    public void setSorters(List<SteamEconItemsSMSorter> sorters) {
        this.sorters = sorters;
    }

    public List<SteamEconItemsSMSortingPrefab> getSortingPrefabs() {
        return sortingPrefabs;
    }

    public void setSortingPrefabs(List<SteamEconItemsSMSortingPrefab> sortingPrefabs) {
        this.sortingPrefabs = sortingPrefabs;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
