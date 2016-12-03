/*
 * MIT License
 *
 * Copyright (c) 2016 Asynchronous Game Query Library
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ibasco.agql.protocols.valve.dota2.webapi.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.ibasco.agql.core.exceptions.AsyncGameLibUncheckedException;
import com.ibasco.agql.protocols.valve.dota2.webapi.pojos.Dota2MatchTeamInfo;

import java.io.IOException;

public class Dota2TeamInfoAdapter extends TypeAdapter<Dota2MatchTeamInfo> {
    @Override
    public Dota2MatchTeamInfo read(JsonReader in) throws IOException {
        in.beginObject();
        Dota2MatchTeamInfo teamInfo = new Dota2MatchTeamInfo();
        while (in.hasNext()) {
            String name = in.nextName();
            if (name.startsWith("league_id_")) {
                teamInfo.getLeagueIds().add(in.nextInt());
            } else if (name.startsWith("player_")) {
                teamInfo.getPlayerAccountIds().add(in.nextLong());
            } else {
                switch (name) {
                    case "name":
                        teamInfo.setName(in.nextString());
                        break;
                    case "tag":
                        teamInfo.setTag(in.nextString());
                        break;
                    case "time_created":
                        teamInfo.setTimeCreated(in.nextLong());
                        break;
                    case "calibration_games_remaining":
                        teamInfo.setCalibrationGamesRemaining(in.nextInt());
                        break;
                    case "logo":
                        teamInfo.setLogo(in.nextLong());
                        break;
                    case "logo_sponsor":
                        teamInfo.setLogoSponsor(in.nextLong());
                        break;
                    case "country_code":
                        teamInfo.setCountryCode(in.nextString());
                        break;
                    case "url":
                        teamInfo.setUrl(in.nextString());
                        break;
                    case "games_played":
                        teamInfo.setGamesPlayed(in.nextInt());
                        break;
                    case "admin_account_id":
                        teamInfo.setAdminAccountId(in.nextLong());
                        break;
                    default:
                        break;
                }
            }
        }
        in.endObject();
        return teamInfo;
    }

    @Override
    public void write(JsonWriter out, Dota2MatchTeamInfo value) throws IOException {
        throw new AsyncGameLibUncheckedException("Not implemented");
    }
}
