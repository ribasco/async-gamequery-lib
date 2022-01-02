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

package com.ibasco.agql.protocols.supercell.coc.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by raffy on 10/28/2016.
 */
public class CocPlayerBasicInfo {
    private String tag;
    private String name;
    private String role;
    private String expLevel;
    private CocLeague league;
    private int trophies;
    private int clanRank;
    private int previousClanRank;
    @SerializedName("donations")
    private int totalDonations;
    @SerializedName("donationsReceived")
    private int totalDonationsReceived;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getExpLevel() {
        return expLevel;
    }

    public void setExpLevel(String expLevel) {
        this.expLevel = expLevel;
    }

    public CocLeague getLeague() {
        return league;
    }

    public void setLeague(CocLeague league) {
        this.league = league;
    }

    public int getTrophies() {
        return trophies;
    }

    public void setTrophies(int trophies) {
        this.trophies = trophies;
    }

    public int getClanRank() {
        return clanRank;
    }

    public void setClanRank(int clanRank) {
        this.clanRank = clanRank;
    }

    public int getPreviousClanRank() {
        return previousClanRank;
    }

    public void setPreviousClanRank(int previousClanRank) {
        this.previousClanRank = previousClanRank;
    }

    public int getTotalDonations() {
        return totalDonations;
    }

    public void setTotalDonations(int totalDonations) {
        this.totalDonations = totalDonations;
    }

    public int getTotalDonationsReceived() {
        return totalDonationsReceived;
    }

    public void setTotalDonationsReceived(int totalDonationsReceived) {
        this.totalDonationsReceived = totalDonationsReceived;
    }

    protected ToStringBuilder superStringBuilder() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("name", getName())
                .append("tag", getTag())
                .append("role", getRole())
                .append("trophies", getTrophies())
                .append("expLevel", getExpLevel());
    }

    @Override
    public String toString() {
        return superStringBuilder().toString();
    }
}
