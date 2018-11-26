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

package com.ibasco.agql.protocols.valve.dota2.webapi;

public class Dota2ApiConstants {
    //Default Values
    public static final String DEFAULT_LANG = "en";

    //Interface Definitions
    public static final String DOTA2_INTERFACE_ECON = "IEconDOTA2_570";
    public static final String DOTA2_INTERFACE_FANTASY = "IDOTA2Fantasy_570";
    public static final String DOTA2_INTERFACE_MATCH = "IDOTA2Match_570";
    public static final String DOTA2_INTERFACE_MATCHSTATS = "IDOTA2MatchStats_570";
    public static final String DOTA2_INTERFACE_STREAMSYS = "IDOTA2StreamSystem_570";
    public static final String DOTA2_INTERFACE_TEAMS = "IDOTA2Teams_570";
    public static final String DOTA2_INTERFACE_TICKET = "IDOTA2Ticket_570";

    //Method Definitions - ECON
    public static final String DOTA2_METHOD_GETGAMEITEMS = "GetGameItems";
    public static final String DOTA2_METHOD_GETHEROES = "GetHeroes";
    public static final String DOTA2_METHOD_GETACCNTEVENTSTATS = "GetEventStatsForAccount";
    public static final String DOTA2_METHOD_GETITEMICONPATH = "GetItemIconPath";
    public static final String DOTA2_METHOD_GETRARITIES = "GetRarities";
    public static final String DOTA2_METHOD_GETTOURNPRIZEPOOL = "GetTournamentPrizePool";

    //Method Definitions - FANTASY
    public static final String DOTA2_METHOD_FANTASYPLAYERSTATS = "GetFantasyPlayerStats";
    public static final String DOTA2_METHOD_GETPLAYERINFO = "GetPlayerOfficialInfo";
    public static final String DOTA2_METHOD_GETPROPLAYERLSIT = "GetProPlayerList";

    //Method Definitions - MATCH
    public static final String DOTA2_METHOD_GETLEAGUELIST = "GetLeagueListing";
    public static final String DOTA2_METHOD_GETLIVELEAGUEGAMES = "GetLiveLeagueGames";
    public static final String DOTA2_METHOD_GETMATCHDETAIL = "GetMatchDetails";
    public static final String DOTA2_METHOD_GETMATCHHISTORY = "GetMatchHistory";
    public static final String DOTA2_METHOD_GETMATCHHISTORYBYSEQNUM = "GetMatchHistoryBySequenceNum";
    public static final String DOTA2_METHOD_GETTEAMINFOBYID = "GetTeamInfoByTeamID";
    public static final String DOTA2_METHOD_GETTOPLIVEGAME = "GetTopLiveGame";
    public static final String DOTA2_METHOD_GETTOURNPLAYERSTATS = "GetTournamentPlayerStats";
    public static final String DOTA2_METHOD_GETSCHEDULEDLEAGUEGAMES = "GetScheduledLeagueGames";
    public static final String DOTA2_METHOD_GETTOPWEEKENDTOURNGAMES = "GetTopWeekendTourneyGames";

    //Method Definitions - MATCH STATS
    public static final String DOTA2_METHOD_GETREALTIMESTATS = "GetRealtimeStats";

    //Method Definitions - STREAM SYSTEM
    public static final String DOTA2_METHOD_GETBROADCASTERINFO = "GetBroadcasterInfo";

    //Method Definitions - TEAMS
    public static final String DOTA2_METHOD_GETTEAMINFO = "GetTeamInfo";

    //Method Definitions - TICKET

    //URL Param Definitions
    public static final String DOTA2_URLPARAM_LANG = "language";
}
