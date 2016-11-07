/*
 * MIT License
 *
 * Copyright (c) 2016 Asynchronous Game Query Library
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

package org.ribasco.agql.protocols.valve.csgo.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class CsgoMatchmakingStatus {
    private String scheduler;
    @SerializedName("online_servers")
    private int onlineServerCount;
    @SerializedName("online_players")
    private int onlinePlayerCount;
    @SerializedName("searching_players")
    private int searchingPlayersCount;
    @SerializedName("search_seconds_avg")
    private int searchSecondsAvg;

    public String getScheduler() {
        return scheduler;
    }

    public void setScheduler(String scheduler) {
        this.scheduler = scheduler;
    }

    public int getOnlineServerCount() {
        return onlineServerCount;
    }

    public void setOnlineServerCount(int onlineServerCount) {
        this.onlineServerCount = onlineServerCount;
    }

    public int getOnlinePlayerCount() {
        return onlinePlayerCount;
    }

    public void setOnlinePlayerCount(int onlinePlayerCount) {
        this.onlinePlayerCount = onlinePlayerCount;
    }

    public int getSearchingPlayersCount() {
        return searchingPlayersCount;
    }

    public void setSearchingPlayersCount(int searchingPlayersCount) {
        this.searchingPlayersCount = searchingPlayersCount;
    }

    public int getSearchSecondsAvg() {
        return searchSecondsAvg;
    }

    public void setSearchSecondsAvg(int searchSecondsAvg) {
        this.searchSecondsAvg = searchSecondsAvg;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
