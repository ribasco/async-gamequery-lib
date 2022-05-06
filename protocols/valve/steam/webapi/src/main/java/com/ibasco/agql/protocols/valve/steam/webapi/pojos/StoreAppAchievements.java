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
import java.util.ArrayList;
import java.util.List;

/**
 * <p>StoreAppAchievements class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class StoreAppAchievements {

    private int total;

    @SerializedName("highlighted")
    private List<StoreAppAchievementHighlight> achievementHighlights = new ArrayList<>();

    /**
     * <p>Getter for the field <code>total</code>.</p>
     *
     * @return a int
     */
    public int getTotal() {
        return total;
    }

    /**
     * <p>Setter for the field <code>total</code>.</p>
     *
     * @param total
     *         a int
     */
    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * <p>Getter for the field <code>achievementHighlights</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<StoreAppAchievementHighlight> getAchievementHighlights() {
        return achievementHighlights;
    }

    /**
     * <p>Setter for the field <code>achievementHighlights</code>.</p>
     *
     * @param achievementHighlights
     *         a {@link java.util.List} object
     */
    public void setAchievementHighlights(List<StoreAppAchievementHighlight> achievementHighlights) {
        this.achievementHighlights = achievementHighlights;
    }
}
