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
 *
 * @author Rafael Luis Ibasco
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

    /**
     * <p>Getter for the field <code>description</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getDescription() {
        return description;
    }

    /**
     * <p>Setter for the field <code>description</code>.</p>
     *
     * @param description a {@link java.lang.String} object
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * <p>Getter for the field <code>clanMembers</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<CocPlayerBasicInfo> getClanMembers() {
        return clanMembers;
    }

    /**
     * <p>Setter for the field <code>clanMembers</code>.</p>
     *
     * @param clanMembers a {@link java.util.List} object
     */
    public void setClanMembers(List<CocPlayerBasicInfo> clanMembers) {
        this.clanMembers = clanMembers;
    }

    /**
     * <p>Getter for the field <code>type</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getType() {
        return type;
    }

    /**
     * <p>Setter for the field <code>type</code>.</p>
     *
     * @param type a {@link java.lang.String} object
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * <p>Getter for the field <code>location</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocLocation} object
     */
    public CocLocation getLocation() {
        return location;
    }

    /**
     * <p>Setter for the field <code>location</code>.</p>
     *
     * @param location a {@link com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocLocation} object
     */
    public void setLocation(CocLocation location) {
        this.location = location;
    }

    /**
     * <p>Getter for the field <code>clanPoints</code>.</p>
     *
     * @return a int
     */
    public int getClanPoints() {
        return clanPoints;
    }

    /**
     * <p>Setter for the field <code>clanPoints</code>.</p>
     *
     * @param clanPoints a int
     */
    public void setClanPoints(int clanPoints) {
        this.clanPoints = clanPoints;
    }

    /**
     * <p>Getter for the field <code>requiredTrophies</code>.</p>
     *
     * @return a int
     */
    public int getRequiredTrophies() {
        return requiredTrophies;
    }

    /**
     * <p>Setter for the field <code>requiredTrophies</code>.</p>
     *
     * @param requiredTrophies a int
     */
    public void setRequiredTrophies(int requiredTrophies) {
        this.requiredTrophies = requiredTrophies;
    }

    /**
     * <p>Getter for the field <code>warFrequency</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getWarFrequency() {
        return warFrequency;
    }

    /**
     * <p>Setter for the field <code>warFrequency</code>.</p>
     *
     * @param warFrequency a {@link java.lang.String} object
     */
    public void setWarFrequency(String warFrequency) {
        this.warFrequency = warFrequency;
    }

    /**
     * <p>Getter for the field <code>warWinStreak</code>.</p>
     *
     * @return a int
     */
    public int getWarWinStreak() {
        return warWinStreak;
    }

    /**
     * <p>Setter for the field <code>warWinStreak</code>.</p>
     *
     * @param warWinStreak a int
     */
    public void setWarWinStreak(int warWinStreak) {
        this.warWinStreak = warWinStreak;
    }

    /**
     * <p>Getter for the field <code>warWins</code>.</p>
     *
     * @return a int
     */
    public int getWarWins() {
        return warWins;
    }

    /**
     * <p>Setter for the field <code>warWins</code>.</p>
     *
     * @param warWins a int
     */
    public void setWarWins(int warWins) {
        this.warWins = warWins;
    }

    /**
     * <p>Getter for the field <code>warTies</code>.</p>
     *
     * @return a int
     */
    public int getWarTies() {
        return warTies;
    }

    /**
     * <p>Setter for the field <code>warTies</code>.</p>
     *
     * @param warTies a int
     */
    public void setWarTies(int warTies) {
        this.warTies = warTies;
    }

    /**
     * <p>Getter for the field <code>warLosses</code>.</p>
     *
     * @return a int
     */
    public int getWarLosses() {
        return warLosses;
    }

    /**
     * <p>Setter for the field <code>warLosses</code>.</p>
     *
     * @param warLosses a int
     */
    public void setWarLosses(int warLosses) {
        this.warLosses = warLosses;
    }

    /**
     * <p>isWarLogPublic.</p>
     *
     * @return a boolean
     */
    public boolean isWarLogPublic() {
        return warLogPublic;
    }

    /**
     * <p>Setter for the field <code>warLogPublic</code>.</p>
     *
     * @param warLogPublic a boolean
     */
    public void setWarLogPublic(boolean warLogPublic) {
        this.warLogPublic = warLogPublic;
    }

    /**
     * <p>Getter for the field <code>totalMembers</code>.</p>
     *
     * @return a int
     */
    public int getTotalMembers() {
        return totalMembers;
    }

    /**
     * <p>Setter for the field <code>totalMembers</code>.</p>
     *
     * @param totalMembers a int
     */
    public void setTotalMembers(int totalMembers) {
        this.totalMembers = totalMembers;
    }

    /** {@inheritDoc} */
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
