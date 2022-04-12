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

package com.ibasco.agql.protocols.valve.steam.webapi.interfaces.cheatreport;

import com.ibasco.agql.protocols.valve.steam.webapi.SteamApiConstants;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.CheatData;
import com.ibasco.agql.protocols.valve.steam.webapi.requests.SteamReportCheatRequest;
import io.netty.handler.codec.http.HttpMethod;

/**
 * <p>ReportCheatData class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class ReportCheatData extends SteamReportCheatRequest {
    /**
     * <p>Constructor for ReportCheatData.</p>
     *
     * @param cheatInfo a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.CheatData} object
     * @param apiVersion a int
     */
    public ReportCheatData(CheatData cheatInfo, int apiVersion) {
        super(SteamApiConstants.STEAM_METHOD_CHEATREPORTSVC_REPORTCHEATDATA, apiVersion);
        method(HttpMethod.POST);
        urlParam("steamid", cheatInfo.getSteamId());
        urlParam("appid", cheatInfo.getAppId());
        urlParam("pathandfilename", cheatInfo.getFilePath());
        urlParam("webcheaturl", cheatInfo.getWebCheatUrl());
        urlParam("time_now", cheatInfo.getTimeNow());
        urlParam("time_started", cheatInfo.getTimeStarted());
        urlParam("time_stopped", cheatInfo.getTimeStopped());
        urlParam("cheatname", cheatInfo.getCheatName());
        urlParam("game_process_id", cheatInfo.getGameProcessId());
        urlParam("cheat_process_id", cheatInfo.getCheatProcessId());
        urlParam("cheat_param_1", cheatInfo.getCheatParam1());
        urlParam("cheat_param_2", cheatInfo.getCheatParam2());
    }
}
