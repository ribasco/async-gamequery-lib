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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jetbrains.annotations.ApiStatus;

/**
 * Created by raffy on 10/28/2016.
 *
 * @author Rafael Luis Ibasco
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
public class CocWarLogEntry {

    private String result;

    private String endTime;

    private int teamSize;

    private CocWarLogEntryClan clan;

    private CocWarLogEntryClan opponent;

    /**
     * <p>Getter for the field <code>endTime</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * <p>Setter for the field <code>endTime</code>.</p>
     *
     * @param endTime
     *         a {@link java.lang.String} object
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * <p>Getter for the field <code>teamSize</code>.</p>
     *
     * @return a int
     */
    public int getTeamSize() {
        return teamSize;
    }

    /**
     * <p>Setter for the field <code>teamSize</code>.</p>
     *
     * @param teamSize
     *         a int
     */
    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("clan", getClan())
                .append("opponent", getOpponent())
                .append("result", getResult())
                .toString();
    }

    /**
     * <p>Getter for the field <code>clan</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocWarLogEntryClan} object
     */
    public CocWarLogEntryClan getClan() {
        return clan;
    }

    /**
     * <p>Setter for the field <code>clan</code>.</p>
     *
     * @param clan
     *         a {@link com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocWarLogEntryClan} object
     */
    public void setClan(CocWarLogEntryClan clan) {
        this.clan = clan;
    }

    /**
     * <p>Getter for the field <code>opponent</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocWarLogEntryClan} object
     */
    public CocWarLogEntryClan getOpponent() {
        return opponent;
    }

    /**
     * <p>Getter for the field <code>result</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getResult() {
        return result;
    }

    /**
     * <p>Setter for the field <code>result</code>.</p>
     *
     * @param result
     *         a {@link java.lang.String} object
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * <p>Setter for the field <code>opponent</code>.</p>
     *
     * @param opponent
     *         a {@link com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocWarLogEntryClan} object
     */
    public void setOpponent(CocWarLogEntryClan opponent) {
        this.opponent = opponent;
    }
}
