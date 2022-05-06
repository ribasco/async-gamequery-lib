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
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by raffy on 10/27/2016.
 *
 * @author Rafael Luis Ibasco
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

    /**
     * <p>Getter for the field <code>steamId</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getSteamId() {
        return steamId;
    }

    /**
     * <p>Setter for the field <code>steamId</code>.</p>
     *
     * @param steamId
     *         a {@link java.lang.String} object
     */
    public void setSteamId(String steamId) {
        this.steamId = steamId;
    }

    /**
     * <p>Getter for the field <code>communityVisibilityState</code>.</p>
     *
     * @return a int
     */
    public int getCommunityVisibilityState() {
        return communityVisibilityState;
    }

    /**
     * <p>Setter for the field <code>communityVisibilityState</code>.</p>
     *
     * @param communityVisibilityState
     *         a int
     */
    public void setCommunityVisibilityState(int communityVisibilityState) {
        this.communityVisibilityState = communityVisibilityState;
    }

    /**
     * <p>Getter for the field <code>profileState</code>.</p>
     *
     * @return a int
     */
    public int getProfileState() {
        return profileState;
    }

    /**
     * <p>Setter for the field <code>profileState</code>.</p>
     *
     * @param profileState
     *         a int
     */
    public void setProfileState(int profileState) {
        this.profileState = profileState;
    }

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name
     *         a {@link java.lang.String} object
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>Getter for the field <code>lastLogOff</code>.</p>
     *
     * @return a long
     */
    public long getLastLogOff() {
        return lastLogOff;
    }

    /**
     * <p>Setter for the field <code>lastLogOff</code>.</p>
     *
     * @param lastLogOff
     *         a long
     */
    public void setLastLogOff(long lastLogOff) {
        this.lastLogOff = lastLogOff;
    }

    /**
     * <p>Getter for the field <code>profileUrl</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getProfileUrl() {
        return profileUrl;
    }

    /**
     * <p>Setter for the field <code>profileUrl</code>.</p>
     *
     * @param profileUrl
     *         a {@link java.lang.String} object
     */
    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    /**
     * <p>Getter for the field <code>avatarUrl</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * <p>Setter for the field <code>avatarUrl</code>.</p>
     *
     * @param avatarUrl
     *         a {@link java.lang.String} object
     */
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    /**
     * <p>Getter for the field <code>avatarMediumUrl</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getAvatarMediumUrl() {
        return avatarMediumUrl;
    }

    /**
     * <p>Setter for the field <code>avatarMediumUrl</code>.</p>
     *
     * @param avatarMediumUrl
     *         a {@link java.lang.String} object
     */
    public void setAvatarMediumUrl(String avatarMediumUrl) {
        this.avatarMediumUrl = avatarMediumUrl;
    }

    /**
     * <p>Getter for the field <code>avatarFullUrl</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getAvatarFullUrl() {
        return avatarFullUrl;
    }

    /**
     * <p>Setter for the field <code>avatarFullUrl</code>.</p>
     *
     * @param avatarFullUrl
     *         a {@link java.lang.String} object
     */
    public void setAvatarFullUrl(String avatarFullUrl) {
        this.avatarFullUrl = avatarFullUrl;
    }

    /**
     * <p>Getter for the field <code>personaState</code>.</p>
     *
     * @return a int
     */
    public int getPersonaState() {
        return personaState;
    }

    /**
     * <p>Setter for the field <code>personaState</code>.</p>
     *
     * @param personaState
     *         a int
     */
    public void setPersonaState(int personaState) {
        this.personaState = personaState;
    }

    /**
     * <p>Getter for the field <code>primaryGroupId</code>.</p>
     *
     * @return a long
     */
    public long getPrimaryGroupId() {
        return primaryGroupId;
    }

    /**
     * <p>Setter for the field <code>primaryGroupId</code>.</p>
     *
     * @param primaryGroupId
     *         a long
     */
    public void setPrimaryGroupId(long primaryGroupId) {
        this.primaryGroupId = primaryGroupId;
    }

    /**
     * <p>Getter for the field <code>timeCreated</code>.</p>
     *
     * @return a long
     */
    public long getTimeCreated() {
        return timeCreated;
    }

    /**
     * <p>Setter for the field <code>timeCreated</code>.</p>
     *
     * @param timeCreated
     *         a long
     */
    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    /**
     * <p>Getter for the field <code>personaStateFlags</code>.</p>
     *
     * @return a int
     */
    public int getPersonaStateFlags() {
        return personaStateFlags;
    }

    /**
     * <p>Setter for the field <code>personaStateFlags</code>.</p>
     *
     * @param personaStateFlags
     *         a int
     */
    public void setPersonaStateFlags(int personaStateFlags) {
        this.personaStateFlags = personaStateFlags;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("steamId", steamId)
                .append("name", name)
                .append("lastLogOff", lastLogOff)
                .append("profileUrl", profileUrl).toString();
    }
}
