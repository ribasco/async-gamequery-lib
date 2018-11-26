/*
 * MIT License
 *
 * Copyright (c) 2018 Asynchronous Game Query Library
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

package com.ibasco.agql.protocols.valve.steam.webapi.interfaces.cheatreport;

import com.ibasco.agql.protocols.valve.steam.webapi.SteamApiConstants;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.CheatData;
import com.ibasco.agql.protocols.valve.steam.webapi.requests.SteamReportCheatRequest;
import io.netty.handler.codec.http.HttpMethod;

public class ReportCheatData extends SteamReportCheatRequest {
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
