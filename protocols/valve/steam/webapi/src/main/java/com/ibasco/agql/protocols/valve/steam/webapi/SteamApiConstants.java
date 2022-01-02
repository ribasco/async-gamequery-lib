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

package com.ibasco.agql.protocols.valve.steam.webapi;

@SuppressWarnings("all")
public class SteamApiConstants {
    //Interface definitions
    public static final String STEAM_BASE_URL_FORMAT = "https://api.steampowered.com/${interface}/${method}/v${version}";
    public static final String STEAM_APPS = "ISteamApps";
    public static final String STEAM_ECONOMY = "ISteamEconomy";
    public static final String STEAM_NEWS = "ISteamNews";
    public static final String STEAM_USER = "ISteamUser";
    public static final String STEAM_USER_STATS = "ISteamUserStats";
    public static final String STEAM_PLAYER_SERVICE = "IPlayerService";
    public static final String STEAM_GAME_SERVER_STATS = "ISteamGameServerStats";
    public static final String STEAM_GAME_LEADERBOARDS = "ISteamLeaderboards";
    public static final String STEAM_MICROTXN = "ISteamMicroTxn";
    public static final String STEAM_MICROTXN_SANDBOX = "ISteamMicroTxnSandbox";
    public static final String STEAM_PUBLISHED_ITEM_SEARCH = "ISteamPublishedItemSearch";
    public static final String STEAM_PUBLISHED_ITEM_VOTING = "ISteamPublishedItemVoting";
    public static final String STEAM_REMOTE_STORAGE = "ISteamRemoteStorage";
    public static final String STEAM_SPECIAL_SURVEY = "ISteamSpecialSurvey";
    public static final String STEAM_USER_AUTH = "ISteamUserAuth";
    public static final String STEAM_USER_OAUTH = "ISteamUserOAuth";
    public static final String STEAM_VIDEO = "ISteamVideo";
    public static final String STEAM_WEB_API_UTIL = "ISteamWebAPIUtil";
    public static final String STEAM_WEB_USER_PRESENCE_OAUTH = "ISteamWebUserPresenceOAuth";
    public static final String STEAM_ACCOUNT_RECOVERY = "IAccountRecoveryService";
    public static final String STEAM_BITPAY = "ISteamBitPay";
    public static final String STEAM_DIRECTORY = "ISteamDirectory";
    public static final String STEAM_ENVOY = "ISteamEnvoy";
    public static final String STEAM_PAYPAL_PAYMENTS_HUB = "ISteamPayPalPaymentsHub";
    public static final String STEAM_ECON_SERVICE = "IEconService";
    public static final String STEAM_ECON_ITEMS = "IEconItems_${appId}";
    public static final String STEAM_CHEATREPORT_SERVICE = "ICheatReportingService";

    //Steam URL Params
    public static final String STEAM_URLPARAM_STEAMID = "steamid";
    public static final String STEAM_URLPARAM_APPID = "appid";
    public static final String STEAM_URLPARAM_CURRENCY = "currency";
    public static final String STEAM_URLPARAM_LANGUAGE = "language";
    public static final String STEAM_URLPARAM_MAXLENGTH = "maxlength";
    public static final String STEAM_URLPARAM_STARTDATE = "startdate";
    public static final String STEAM_URLPARAM_ENDDATE = "enddate";
    public static final String STEAM_URLPARAM_COUNT = "count";
    public static final String STEAM_URLPARAM_FEEDS = "feeds";
    public static final String STEAM_URLPARAM_NAME = "name";

    //Steam Methods
    public static final String STEAM_METHOD_ECONITEMS_GETPLAYERITEMS = "GetPlayerItems";
    public static final String STEAM_METHOD_ECONITEMS_GETSCHEMA = "GetSchema";
    public static final String STEAM_METHOD_ECONITEMS_GETSCHEMAURL = "GetSchemaURL";
    public static final String STEAM_METHOD_ECONITEMS_GETSTOREMETA = "GetStoreMetaData";
    public static final String STEAM_METHOD_ECONITEMS_GETSTORESTATUS = "GetStoreStatus";

    public static final String STEAM_METHOD_CHEATREPORTSVC_REPORTCHEATDATA = "ReportCheatData";

    //Steam URL Properties
    public static final String STEAM_PROP_APPID = "appId";
    public static final String STEAM_PROP_INTERFACE = "interface";
    public static final String STEAM_PROP_METHOD = "method";
    public static final String STEAM_PROP_VERSION = "version";

    //Store Front URL Properties
    public static final String SF_PROP_BASEURL = "baseUrl";

    //Store Front Constants
    public static final String SF_BASE_URL_FORMAT = "https://store.steampowered.com/api";
    public static final String SF_METHOD_APPDETAILS = "${baseUrl}/appdetails";
    public static final String SF_METHOD_FEATURED = "${baseUrl}/featured";
    public static final String SF_METHOD_FEATURED_CATEGORIES = "${baseUrl}/featuredcategories";
    public static final String SF_METHOD_PACKAGE_DETAILS = "${baseUrl}/packagedetails";
    public static final String SF_METHOD_SALE_DETAILS = "${baseUrl}/salepage";
}
