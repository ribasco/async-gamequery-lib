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
