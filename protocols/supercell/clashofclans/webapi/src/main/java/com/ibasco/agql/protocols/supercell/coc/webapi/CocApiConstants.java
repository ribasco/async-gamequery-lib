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

package com.ibasco.agql.protocols.supercell.coc.webapi;

public class CocApiConstants {
    //url formats
    public static final String UF_COC_BASE = "https://api.clashofclans.com/v${version}";
    //uf clan interface
    public static final String UF_COC_CLAN_SEARCH = "${baseUrl}/clans";
    public static final String UF_COC_CLAN_INFO = "${baseUrl}/clans/${clanTag}";
    public static final String UF_COC_CLAN_MEMBERS = "${baseUrl}/clans/${clanTag}/members";
    public static final String UF_COC_CLAN_WARLOG = "${baseUrl}/clans/${clanTag}/warlog";
    //uf locations
    public static final String UF_COC_LOCATIONS = "${baseUrl}/locations";
    public static final String UF_COC_LOCATION_INFO = "${baseUrl}/locations/${locationId}";
    public static final String UF_COC_LOCATION_CLAN_RANK = "${baseUrl}/locations/${locationId}/rankings/clans";
    public static final String UF_COC_LOCATION_PLAYER_RANK = "${baseUrl}/locations/${locationId}/rankings/players";
    //uf league interface
    public static final String UF_COC_LEAGUES = "${baseUrl}/leagues";
    public static final String UF_COC_LEAGUE_INFO = "${baseUrl}/leagues/${leagueId}";
    public static final String UF_COC_LEAGUE_SEASONS = "${baseUrl}/leagues/${leagueId}/seasons";
    public static final String UF_COC_LEAGUE_SEASON_RANKINGS = "${baseUrl}/leagues/${leagueId}/seasons/${seasonId}";
    //uf player interface
    public static final String UF_COC_PLAYER_INFO = "${baseUrl}/players/${playerTag}";

    //url format properties
    public static final String UF_PROP_VERSION = "version";
    public static final String UF_PROP_CLANTAG = "clanTag";
    public static final String UF_PROP_PLAYERTAG = "playerTag";
    public static final String UF_PROP_BASEURL = "baseUrl";
    public static final String UF_PROP_LEAGUE_ID = "leagueId";
    public static final String UF_PROP_SEASON_ID = "seasonId";
    public static final String UF_PROP_LOCATION_ID = "locationId";
}
