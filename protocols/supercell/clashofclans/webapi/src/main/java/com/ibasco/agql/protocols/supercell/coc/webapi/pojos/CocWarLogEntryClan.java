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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by raffy on 10/28/2016.
 */
public class CocWarLogEntryClan {
    private String tag;
    private String name;
    private CocClanBadgeUrls badgeUrls;
    private int clanLevel;
    private int attacks;
    private int stars;
    private int destructionPercentage;
    private int expEarned;

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

    public CocClanBadgeUrls getBadgeUrls() {
        return badgeUrls;
    }

    public void setBadgeUrls(CocClanBadgeUrls badgeUrls) {
        this.badgeUrls = badgeUrls;
    }

    public int getClanLevel() {
        return clanLevel;
    }

    public void setClanLevel(int clanLevel) {
        this.clanLevel = clanLevel;
    }

    public int getAttacks() {
        return attacks;
    }

    public void setAttacks(int attacks) {
        this.attacks = attacks;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getDestructionPercentage() {
        return destructionPercentage;
    }

    public void setDestructionPercentage(int destructionPercentage) {
        this.destructionPercentage = destructionPercentage;
    }

    public int getExpEarned() {
        return expEarned;
    }

    public void setExpEarned(int expEarned) {
        this.expEarned = expEarned;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("name", getName())
                .append("tag", getTag())
                .append("attacks", getAttacks())
                .append("avgDestruction", getDestructionPercentage())
                .toString();
    }
}
