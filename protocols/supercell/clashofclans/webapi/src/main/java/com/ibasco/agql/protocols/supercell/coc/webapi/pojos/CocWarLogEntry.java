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
public class CocWarLogEntry {
    private String result;
    private String endTime;
    private int teamSize;
    private CocWarLogEntryClan clan;
    private CocWarLogEntryClan opponent;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    public CocWarLogEntryClan getClan() {
        return clan;
    }

    public void setClan(CocWarLogEntryClan clan) {
        this.clan = clan;
    }

    public CocWarLogEntryClan getOpponent() {
        return opponent;
    }

    public void setOpponent(CocWarLogEntryClan opponent) {
        this.opponent = opponent;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("clan", getClan())
                .append("opponent", getOpponent())
                .append("result", getResult())
                .toString();
    }
}
