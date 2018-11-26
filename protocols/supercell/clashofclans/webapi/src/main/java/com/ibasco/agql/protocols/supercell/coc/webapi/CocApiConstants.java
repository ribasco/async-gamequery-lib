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
