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

package org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by raffy on 10/27/2016.
 */
public class SteamPlayerProfile {
    @SerializedName("steamid")
    private String steamId;
    @SerializedName("communityvisibilitystate")
    private int communityVisibilityState;
    @SerializedName("profilestate")
    private int profileState;
    @SerializedName("personaname")
    private String name;
    @SerializedName("lastlogoff")
    private long lastLogOff;
    @SerializedName("profileurl")
    private String profileUrl;
    @SerializedName("avatar")
    private String avatarUrl;
    @SerializedName("avatarmedium")
    private String avatarMediumUrl;
    @SerializedName("avatarfull")
    private String avatarFullUrl;
    @SerializedName("personastate")
    private int personaState;
    @SerializedName("primaryclanid")
    private long primaryGroupId;
    @SerializedName("timecreated")
    private long timeCreated;
    @SerializedName("personastateflags")
    private int personaStateFlags;

    public String getSteamId() {
        return steamId;
    }

    public void setSteamId(String steamId) {
        this.steamId = steamId;
    }

    public int getCommunityVisibilityState() {
        return communityVisibilityState;
    }

    public void setCommunityVisibilityState(int communityVisibilityState) {
        this.communityVisibilityState = communityVisibilityState;
    }

    public int getProfileState() {
        return profileState;
    }

    public void setProfileState(int profileState) {
        this.profileState = profileState;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLastLogOff() {
        return lastLogOff;
    }

    public void setLastLogOff(long lastLogOff) {
        this.lastLogOff = lastLogOff;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAvatarMediumUrl() {
        return avatarMediumUrl;
    }

    public void setAvatarMediumUrl(String avatarMediumUrl) {
        this.avatarMediumUrl = avatarMediumUrl;
    }

    public String getAvatarFullUrl() {
        return avatarFullUrl;
    }

    public void setAvatarFullUrl(String avatarFullUrl) {
        this.avatarFullUrl = avatarFullUrl;
    }

    public int getPersonaState() {
        return personaState;
    }

    public void setPersonaState(int personaState) {
        this.personaState = personaState;
    }

    public long getPrimaryGroupId() {
        return primaryGroupId;
    }

    public void setPrimaryGroupId(long primaryGroupId) {
        this.primaryGroupId = primaryGroupId;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public int getPersonaStateFlags() {
        return personaStateFlags;
    }

    public void setPersonaStateFlags(int personaStateFlags) {
        this.personaStateFlags = personaStateFlags;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("steamId", steamId)
                .append("name", name)
                .append("lastLogOff", lastLogOff)
                .append("profileUrl", profileUrl).toString();
    }
}
