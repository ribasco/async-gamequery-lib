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

/**
 * Created by raffy on 10/27/2016.
 *
 * @author Rafael Luis Ibasco
 */
public class SteamQuestStatus {
    @SerializedName("questid")
    private int questId;
    private boolean completed;

    /**
     * <p>Getter for the field <code>questId</code>.</p>
     *
     * @return a int
     */
    public int getQuestId() {
        return questId;
    }

    /**
     * <p>Setter for the field <code>questId</code>.</p>
     *
     * @param questId a int
     */
    public void setQuestId(int questId) {
        this.questId = questId;
    }

    /**
     * <p>isCompleted.</p>
     *
     * @return a boolean
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * <p>Setter for the field <code>completed</code>.</p>
     *
     * @param completed a boolean
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
