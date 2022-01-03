/*
 * Copyright 2022 Asynchronous Game Query Library
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
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raffy on 10/28/2016.
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
public class CocClanDetailedInfo extends CocClanBasicInfo {
    private String type;
    private String description;
    private CocLocation location;
    private int clanPoints;
    private int requiredTrophies;
    private String warFrequency;
    private int warWinStreak;
    private int warWins;
    private int warTies;
    private int warLosses;
    @SerializedName("isWarLogPublic")
    private boolean warLogPublic;
    @SerializedName("members")
    private int totalMembers;
    @SerializedName("memberList")
    private List<CocPlayerBasicInfo> clanMembers = new ArrayList<>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CocPlayerBasicInfo> getClanMembers() {
        return clanMembers;
    }

    public void setClanMembers(List<CocPlayerBasicInfo> clanMembers) {
        this.clanMembers = clanMembers;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CocLocation getLocation() {
        return location;
    }

    public void setLocation(CocLocation location) {
        this.location = location;
    }

    public int getClanPoints() {
        return clanPoints;
    }

    public void setClanPoints(int clanPoints) {
        this.clanPoints = clanPoints;
    }

    public int getRequiredTrophies() {
        return requiredTrophies;
    }

    public void setRequiredTrophies(int requiredTrophies) {
        this.requiredTrophies = requiredTrophies;
    }

    public String getWarFrequency() {
        return warFrequency;
    }

    public void setWarFrequency(String warFrequency) {
        this.warFrequency = warFrequency;
    }

    public int getWarWinStreak() {
        return warWinStreak;
    }

    public void setWarWinStreak(int warWinStreak) {
        this.warWinStreak = warWinStreak;
    }

    public int getWarWins() {
        return warWins;
    }

    public void setWarWins(int warWins) {
        this.warWins = warWins;
    }

    public int getWarTies() {
        return warTies;
    }

    public void setWarTies(int warTies) {
        this.warTies = warTies;
    }

    public int getWarLosses() {
        return warLosses;
    }

    public void setWarLosses(int warLosses) {
        this.warLosses = warLosses;
    }

    public boolean isWarLogPublic() {
        return warLogPublic;
    }

    public void setWarLogPublic(boolean warLogPublic) {
        this.warLogPublic = warLogPublic;
    }

    public int getTotalMembers() {
        return totalMembers;
    }

    public void setTotalMembers(int totalMembers) {
        this.totalMembers = totalMembers;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("name", getName())
                .append("tag", getTag())
                .append("type", getType())
                .append("desc", getDescription())
                .append("members", getTotalMembers())
                .append("memberListSize", defaultIfNull(getClanMembers(), new ArrayList<>()).size())
                .toString();
    }
}
