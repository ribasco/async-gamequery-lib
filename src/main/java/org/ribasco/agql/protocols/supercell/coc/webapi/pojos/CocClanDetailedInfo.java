/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Luis Ibasco
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **************************************************************************************************/

package org.ribasco.agql.protocols.supercell.coc.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

/**
 * Created by raffy on 10/28/2016.
 */
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
    private List<CocPlayerBasicInfo> clanMembers;

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
