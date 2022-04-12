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

package com.ibasco.agql.protocols.valve.dota2.webapi;

/**
 * <p>Dota2ApiConstants class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class Dota2ApiConstants {
    //Default Values
    /** Constant <code>DEFAULT_LANG="en"</code> */
    public static final String DEFAULT_LANG = "en";

    //Interface Definitions
    /** Constant <code>DOTA2_INTERFACE_ECON="IEconDOTA2_570"</code> */
    public static final String DOTA2_INTERFACE_ECON = "IEconDOTA2_570";
    /** Constant <code>DOTA2_INTERFACE_FANTASY="IDOTA2Fantasy_570"</code> */
    public static final String DOTA2_INTERFACE_FANTASY = "IDOTA2Fantasy_570";
    /** Constant <code>DOTA2_INTERFACE_MATCH="IDOTA2Match_570"</code> */
    public static final String DOTA2_INTERFACE_MATCH = "IDOTA2Match_570";
    /** Constant <code>DOTA2_INTERFACE_MATCHSTATS="IDOTA2MatchStats_570"</code> */
    public static final String DOTA2_INTERFACE_MATCHSTATS = "IDOTA2MatchStats_570";
    /** Constant <code>DOTA2_INTERFACE_STREAMSYS="IDOTA2StreamSystem_570"</code> */
    public static final String DOTA2_INTERFACE_STREAMSYS = "IDOTA2StreamSystem_570";
    /** Constant <code>DOTA2_INTERFACE_TEAMS="IDOTA2Teams_570"</code> */
    public static final String DOTA2_INTERFACE_TEAMS = "IDOTA2Teams_570";
    /** Constant <code>DOTA2_INTERFACE_TICKET="IDOTA2Ticket_570"</code> */
    public static final String DOTA2_INTERFACE_TICKET = "IDOTA2Ticket_570";

    //Method Definitions - ECON
    /** Constant <code>DOTA2_METHOD_GETGAMEITEMS="GetGameItems"</code> */
    public static final String DOTA2_METHOD_GETGAMEITEMS = "GetGameItems";
    /** Constant <code>DOTA2_METHOD_GETHEROES="GetHeroes"</code> */
    public static final String DOTA2_METHOD_GETHEROES = "GetHeroes";
    /** Constant <code>DOTA2_METHOD_GETACCNTEVENTSTATS="GetEventStatsForAccount"</code> */
    public static final String DOTA2_METHOD_GETACCNTEVENTSTATS = "GetEventStatsForAccount";
    /** Constant <code>DOTA2_METHOD_GETITEMICONPATH="GetItemIconPath"</code> */
    public static final String DOTA2_METHOD_GETITEMICONPATH = "GetItemIconPath";
    /** Constant <code>DOTA2_METHOD_GETRARITIES="GetRarities"</code> */
    public static final String DOTA2_METHOD_GETRARITIES = "GetRarities";
    /** Constant <code>DOTA2_METHOD_GETTOURNPRIZEPOOL="GetTournamentPrizePool"</code> */
    public static final String DOTA2_METHOD_GETTOURNPRIZEPOOL = "GetTournamentPrizePool";

    //Method Definitions - FANTASY
    /** Constant <code>DOTA2_METHOD_FANTASYPLAYERSTATS="GetFantasyPlayerStats"</code> */
    public static final String DOTA2_METHOD_FANTASYPLAYERSTATS = "GetFantasyPlayerStats";
    /** Constant <code>DOTA2_METHOD_GETPLAYERINFO="GetPlayerOfficialInfo"</code> */
    public static final String DOTA2_METHOD_GETPLAYERINFO = "GetPlayerOfficialInfo";
    /** Constant <code>DOTA2_METHOD_GETPROPLAYERLSIT="GetProPlayerList"</code> */
    public static final String DOTA2_METHOD_GETPROPLAYERLSIT = "GetProPlayerList";

    //Method Definitions - MATCH
    /** Constant <code>DOTA2_METHOD_GETLEAGUELIST="GetLeagueListing"</code> */
    public static final String DOTA2_METHOD_GETLEAGUELIST = "GetLeagueListing";
    /** Constant <code>DOTA2_METHOD_GETLIVELEAGUEGAMES="GetLiveLeagueGames"</code> */
    public static final String DOTA2_METHOD_GETLIVELEAGUEGAMES = "GetLiveLeagueGames";
    /** Constant <code>DOTA2_METHOD_GETMATCHDETAIL="GetMatchDetails"</code> */
    public static final String DOTA2_METHOD_GETMATCHDETAIL = "GetMatchDetails";
    /** Constant <code>DOTA2_METHOD_GETMATCHHISTORY="GetMatchHistory"</code> */
    public static final String DOTA2_METHOD_GETMATCHHISTORY = "GetMatchHistory";
    /** Constant <code>DOTA2_METHOD_GETMATCHHISTORYBYSEQNUM="GetMatchHistoryBySequenceNum"</code> */
    public static final String DOTA2_METHOD_GETMATCHHISTORYBYSEQNUM = "GetMatchHistoryBySequenceNum";
    /** Constant <code>DOTA2_METHOD_GETTEAMINFOBYID="GetTeamInfoByTeamID"</code> */
    public static final String DOTA2_METHOD_GETTEAMINFOBYID = "GetTeamInfoByTeamID";
    /** Constant <code>DOTA2_METHOD_GETTOPLIVEGAME="GetTopLiveGame"</code> */
    public static final String DOTA2_METHOD_GETTOPLIVEGAME = "GetTopLiveGame";
    /** Constant <code>DOTA2_METHOD_GETTOURNPLAYERSTATS="GetTournamentPlayerStats"</code> */
    public static final String DOTA2_METHOD_GETTOURNPLAYERSTATS = "GetTournamentPlayerStats";
    /** Constant <code>DOTA2_METHOD_GETSCHEDULEDLEAGUEGAMES="GetScheduledLeagueGames"</code> */
    public static final String DOTA2_METHOD_GETSCHEDULEDLEAGUEGAMES = "GetScheduledLeagueGames";
    /** Constant <code>DOTA2_METHOD_GETTOPWEEKENDTOURNGAMES="GetTopWeekendTourneyGames"</code> */
    public static final String DOTA2_METHOD_GETTOPWEEKENDTOURNGAMES = "GetTopWeekendTourneyGames";

    //Method Definitions - MATCH STATS
    /** Constant <code>DOTA2_METHOD_GETREALTIMESTATS="GetRealtimeStats"</code> */
    public static final String DOTA2_METHOD_GETREALTIMESTATS = "GetRealtimeStats";

    //Method Definitions - STREAM SYSTEM
    /** Constant <code>DOTA2_METHOD_GETBROADCASTERINFO="GetBroadcasterInfo"</code> */
    public static final String DOTA2_METHOD_GETBROADCASTERINFO = "GetBroadcasterInfo";

    //Method Definitions - TEAMS
    /** Constant <code>DOTA2_METHOD_GETTEAMINFO="GetTeamInfo"</code> */
    public static final String DOTA2_METHOD_GETTEAMINFO = "GetTeamInfo";

    //Method Definitions - TICKET

    //URL Param Definitions
    /** Constant <code>DOTA2_URLPARAM_LANG="language"</code> */
    public static final String DOTA2_URLPARAM_LANG = "language";
}
