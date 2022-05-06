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

package com.ibasco.agql.protocols.supercell.coc.webapi;

import org.jetbrains.annotations.ApiStatus;

/**
 * <p>CocApiConstants class.</p>
 *
 * @author Rafael Luis Ibasco
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
public class CocApiConstants {

    //url formats

    /** Constant <code>UF_COC_BASE="https://api.clashofclans.com/v${version"{trunked}</code> */
    public static final String UF_COC_BASE = "https://api.clashofclans.com/v${version}";

    //uf clan interface

    /** Constant <code>UF_COC_CLAN_SEARCH="${baseUrl}/clans"</code> */
    public static final String UF_COC_CLAN_SEARCH = "${baseUrl}/clans";

    /** Constant <code>UF_COC_CLAN_INFO="${baseUrl}/clans/${clanTag}"</code> */
    public static final String UF_COC_CLAN_INFO = "${baseUrl}/clans/${clanTag}";

    /** Constant <code>UF_COC_CLAN_MEMBERS="${baseUrl}/clans/${clanTag}/members"</code> */
    public static final String UF_COC_CLAN_MEMBERS = "${baseUrl}/clans/${clanTag}/members";

    /** Constant <code>UF_COC_CLAN_WARLOG="${baseUrl}/clans/${clanTag}/warlog"</code> */
    public static final String UF_COC_CLAN_WARLOG = "${baseUrl}/clans/${clanTag}/warlog";

    //uf locations

    /** Constant <code>UF_COC_LOCATIONS="${baseUrl}/locations"</code> */
    public static final String UF_COC_LOCATIONS = "${baseUrl}/locations";

    /** Constant <code>UF_COC_LOCATION_INFO="${baseUrl}/locations/${locationId}"</code> */
    public static final String UF_COC_LOCATION_INFO = "${baseUrl}/locations/${locationId}";

    /** Constant <code>UF_COC_LOCATION_CLAN_RANK="${baseUrl}/locations/${locationId}/rank"{trunked}</code> */
    public static final String UF_COC_LOCATION_CLAN_RANK = "${baseUrl}/locations/${locationId}/rankings/clans";

    /** Constant <code>UF_COC_LOCATION_PLAYER_RANK="${baseUrl}/locations/${locationId}/rank"{trunked}</code> */
    public static final String UF_COC_LOCATION_PLAYER_RANK = "${baseUrl}/locations/${locationId}/rankings/players";

    //uf league interface

    /** Constant <code>UF_COC_LEAGUES="${baseUrl}/leagues"</code> */
    public static final String UF_COC_LEAGUES = "${baseUrl}/leagues";

    /** Constant <code>UF_COC_LEAGUE_INFO="${baseUrl}/leagues/${leagueId}"</code> */
    public static final String UF_COC_LEAGUE_INFO = "${baseUrl}/leagues/${leagueId}";

    /** Constant <code>UF_COC_LEAGUE_SEASONS="${baseUrl}/leagues/${leagueId}/seasons"</code> */
    public static final String UF_COC_LEAGUE_SEASONS = "${baseUrl}/leagues/${leagueId}/seasons";

    /** Constant <code>UF_COC_LEAGUE_SEASON_RANKINGS="${baseUrl}/leagues/${leagueId}/seasons/"{trunked}</code> */
    public static final String UF_COC_LEAGUE_SEASON_RANKINGS = "${baseUrl}/leagues/${leagueId}/seasons/${seasonId}";

    //uf player interface

    /** Constant <code>UF_COC_PLAYER_INFO="${baseUrl}/players/${playerTag}"</code> */
    public static final String UF_COC_PLAYER_INFO = "${baseUrl}/players/${playerTag}";

    //url format properties

    /** Constant <code>UF_PROP_VERSION="version"</code> */
    public static final String UF_PROP_VERSION = "version";

    /** Constant <code>UF_PROP_CLANTAG="clanTag"</code> */
    public static final String UF_PROP_CLANTAG = "clanTag";

    /** Constant <code>UF_PROP_PLAYERTAG="playerTag"</code> */
    public static final String UF_PROP_PLAYERTAG = "playerTag";

    /** Constant <code>UF_PROP_BASEURL="baseUrl"</code> */
    public static final String UF_PROP_BASEURL = "baseUrl";

    /** Constant <code>UF_PROP_LEAGUE_ID="leagueId"</code> */
    public static final String UF_PROP_LEAGUE_ID = "leagueId";

    /** Constant <code>UF_PROP_SEASON_ID="seasonId"</code> */
    public static final String UF_PROP_SEASON_ID = "seasonId";

    /** Constant <code>UF_PROP_LOCATION_ID="locationId"</code> */
    public static final String UF_PROP_LOCATION_ID = "locationId";
}
