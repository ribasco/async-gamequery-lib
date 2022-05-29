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

package com.ibasco.agql.protocols.valve.steam.webapi;

/**
 * <p>SteamApiConstants class.</p>
 *
 * @author Rafael Luis Ibasco
 */
@SuppressWarnings("all")
public class SteamApiConstants {
    //Interface definitions

    /** Constant <code>STEAM_BASE_URL_FORMAT="https://api.steampowered.com/${interfac"{trunked}</code> */
    public static final String STEAM_BASE_URL_FORMAT = "https://api.steampowered.com/${interface}/${method}/v${version}";

    /** Constant <code>STEAM_APPS="ISteamApps"</code> */
    public static final String STEAM_APPS = "ISteamApps";

    /** Constant <code>STEAM_ECONOMY="ISteamEconomy"</code> */
    public static final String STEAM_ECONOMY = "ISteamEconomy";

    /** Constant <code>STEAM_NEWS="ISteamNews"</code> */
    public static final String STEAM_NEWS = "ISteamNews";

    /** Constant <code>STEAM_USER="ISteamUser"</code> */
    public static final String STEAM_USER = "ISteamUser";

    /** Constant <code>STEAM_USER_STATS="ISteamUserStats"</code> */
    public static final String STEAM_USER_STATS = "ISteamUserStats";

    /** Constant <code>STEAM_PLAYER_SERVICE="IPlayerService"</code> */
    public static final String STEAM_PLAYER_SERVICE = "IPlayerService";

    /** Constant <code>STEAM_GAME_SERVER_STATS="ISteamGameServerStats"</code> */
    public static final String STEAM_GAME_SERVER_STATS = "ISteamGameServerStats";

    /** Constant <code>STEAM_GAME_LEADERBOARDS="ISteamLeaderboards"</code> */
    public static final String STEAM_GAME_LEADERBOARDS = "ISteamLeaderboards";

    /** Constant <code>STEAM_MICROTXN="ISteamMicroTxn"</code> */
    public static final String STEAM_MICROTXN = "ISteamMicroTxn";

    /** Constant <code>STEAM_MICROTXN_SANDBOX="ISteamMicroTxnSandbox"</code> */
    public static final String STEAM_MICROTXN_SANDBOX = "ISteamMicroTxnSandbox";

    /** Constant <code>STEAM_PUBLISHED_ITEM_SEARCH="ISteamPublishedItemSearch"</code> */
    public static final String STEAM_PUBLISHED_ITEM_SEARCH = "ISteamPublishedItemSearch";

    /** Constant <code>STEAM_PUBLISHED_ITEM_VOTING="ISteamPublishedItemVoting"</code> */
    public static final String STEAM_PUBLISHED_ITEM_VOTING = "ISteamPublishedItemVoting";

    /** Constant <code>STEAM_REMOTE_STORAGE="ISteamRemoteStorage"</code> */
    public static final String STEAM_REMOTE_STORAGE = "ISteamRemoteStorage";

    /** Constant <code>STEAM_SPECIAL_SURVEY="ISteamSpecialSurvey"</code> */
    public static final String STEAM_SPECIAL_SURVEY = "ISteamSpecialSurvey";

    /** Constant <code>STEAM_USER_AUTH="ISteamUserAuth"</code> */
    public static final String STEAM_USER_AUTH = "ISteamUserAuth";

    /** Constant <code>STEAM_USER_OAUTH="ISteamUserOAuth"</code> */
    public static final String STEAM_USER_OAUTH = "ISteamUserOAuth";

    /** Constant <code>STEAM_VIDEO="ISteamVideo"</code> */
    public static final String STEAM_VIDEO = "ISteamVideo";

    /** Constant <code>STEAM_WEB_API_UTIL="ISteamWebAPIUtil"</code> */
    public static final String STEAM_WEB_API_UTIL = "ISteamWebAPIUtil";

    /** Constant <code>STEAM_WEB_USER_PRESENCE_OAUTH="ISteamWebUserPresenceOAuth"</code> */
    public static final String STEAM_WEB_USER_PRESENCE_OAUTH = "ISteamWebUserPresenceOAuth";

    /** Constant <code>STEAM_ACCOUNT_RECOVERY="IAccountRecoveryService"</code> */
    public static final String STEAM_ACCOUNT_RECOVERY = "IAccountRecoveryService";

    /** Constant <code>STEAM_BITPAY="ISteamBitPay"</code> */
    public static final String STEAM_BITPAY = "ISteamBitPay";

    /** Constant <code>STEAM_DIRECTORY="ISteamDirectory"</code> */
    public static final String STEAM_DIRECTORY = "ISteamDirectory";

    /** Constant <code>STEAM_ENVOY="ISteamEnvoy"</code> */
    public static final String STEAM_ENVOY = "ISteamEnvoy";

    /** Constant <code>STEAM_PAYPAL_PAYMENTS_HUB="ISteamPayPalPaymentsHub"</code> */
    public static final String STEAM_PAYPAL_PAYMENTS_HUB = "ISteamPayPalPaymentsHub";

    /** Constant <code>STEAM_ECON_SERVICE="IEconService"</code> */
    public static final String STEAM_ECON_SERVICE = "IEconService";

    /** Constant <code>STEAM_ECON_ITEMS="IEconItems_${appId}"</code> */
    public static final String STEAM_ECON_ITEMS = "IEconItems_${appId}";

    /** Constant <code>STEAM_CHEATREPORT_SERVICE="ICheatReportingService"</code> */
    public static final String STEAM_CHEATREPORT_SERVICE = "ICheatReportingService";

    public static final String STEAM_GAMESERVERS_SERVICE = "IGameServersService";

    public static final String STEAM_STORE_SERVICE = "IStoreService";

    public static final String STEAM_WEBAPI_UTIL = "ISteamWebAPIUtil";

    public static final String STEAM_COMMUNITY_SERVICE = "ICommunityService";

    //Steam URL Params

    /** Constant <code>STEAM_URLPARAM_STEAMID="steamid"</code> */
    public static final String STEAM_URLPARAM_STEAMID = "steamid";

    /** Constant <code>STEAM_URLPARAM_APPID="appid"</code> */
    public static final String STEAM_URLPARAM_APPID = "appid";

    /** Constant <code>STEAM_URLPARAM_CURRENCY="currency"</code> */
    public static final String STEAM_URLPARAM_CURRENCY = "currency";

    /** Constant <code>STEAM_URLPARAM_LANGUAGE="language"</code> */
    public static final String STEAM_URLPARAM_LANGUAGE = "language";

    /** Constant <code>STEAM_URLPARAM_MAXLENGTH="maxlength"</code> */
    public static final String STEAM_URLPARAM_MAXLENGTH = "maxlength";

    /** Constant <code>STEAM_URLPARAM_STARTDATE="startdate"</code> */
    public static final String STEAM_URLPARAM_STARTDATE = "startdate";

    /** Constant <code>STEAM_URLPARAM_ENDDATE="enddate"</code> */
    public static final String STEAM_URLPARAM_ENDDATE = "enddate";

    /** Constant <code>STEAM_URLPARAM_COUNT="count"</code> */
    public static final String STEAM_URLPARAM_COUNT = "count";

    /** Constant <code>STEAM_URLPARAM_FEEDS="feeds"</code> */
    public static final String STEAM_URLPARAM_FEEDS = "feeds";

    /** Constant <code>STEAM_URLPARAM_NAME="name"</code> */
    public static final String STEAM_URLPARAM_NAME = "name";

    //Steam Methods

    /** Constant <code>STEAM_METHOD_ECONITEMS_GETPLAYERITEMS="GetPlayerItems"</code> */
    public static final String STEAM_METHOD_ECONITEMS_GETPLAYERITEMS = "GetPlayerItems";

    /** Constant <code>STEAM_METHOD_ECONITEMS_GETSCHEMA="GetSchema"</code> */
    public static final String STEAM_METHOD_ECONITEMS_GETSCHEMA = "GetSchema";

    /** Constant <code>STEAM_METHOD_ECONITEMS_GETSCHEMAURL="GetSchemaURL"</code> */
    public static final String STEAM_METHOD_ECONITEMS_GETSCHEMAURL = "GetSchemaURL";

    /** Constant <code>STEAM_METHOD_ECONITEMS_GETSTOREMETA="GetStoreMetaData"</code> */
    public static final String STEAM_METHOD_ECONITEMS_GETSTOREMETA = "GetStoreMetaData";

    /** Constant <code>STEAM_METHOD_ECONITEMS_GETSTORESTATUS="GetStoreStatus"</code> */
    public static final String STEAM_METHOD_ECONITEMS_GETSTORESTATUS = "GetStoreStatus";

    /** Constant <code>STEAM_METHOD_CHEATREPORTSVC_REPORTCHEATDATA="ReportCheatData"</code> */
    public static final String STEAM_METHOD_CHEATREPORTSVC_REPORTCHEATDATA = "ReportCheatData";

    //Steam URL Properties

    /** Constant <code>STEAM_PROP_APPID="appId"</code> */
    public static final String STEAM_PROP_APPID = "appId";

    /** Constant <code>STEAM_PROP_INTERFACE="interface"</code> */
    public static final String STEAM_PROP_INTERFACE = "interface";

    /** Constant <code>STEAM_PROP_METHOD="method"</code> */
    public static final String STEAM_PROP_METHOD = "method";

    /** Constant <code>STEAM_PROP_VERSION="version"</code> */
    public static final String STEAM_PROP_VERSION = "version";

    //Store Front URL Properties

    /** Constant <code>SF_PROP_BASEURL="baseUrl"</code> */
    public static final String SF_PROP_BASEURL = "baseUrl";

    //Store Front Constants

    /** Constant <code>SF_BASE_URL_FORMAT="https://store.steampowered.com/api"</code> */
    public static final String SF_BASE_URL_FORMAT = "https://store.steampowered.com/api";

    /** Constant <code>SF_METHOD_APPDETAILS="${baseUrl}/appdetails"</code> */
    public static final String SF_METHOD_APPDETAILS = "${baseUrl}/appdetails";

    /** Constant <code>SF_METHOD_FEATURED="${baseUrl}/featured"</code> */
    public static final String SF_METHOD_FEATURED = "${baseUrl}/featured";

    /** Constant <code>SF_METHOD_FEATURED_CATEGORIES="${baseUrl}/featuredcategories"</code> */
    public static final String SF_METHOD_FEATURED_CATEGORIES = "${baseUrl}/featuredcategories";

    /** Constant <code>SF_METHOD_PACKAGE_DETAILS="${baseUrl}/packagedetails"</code> */
    public static final String SF_METHOD_PACKAGE_DETAILS = "${baseUrl}/packagedetails";

    /** Constant <code>SF_METHOD_SALE_DETAILS="${baseUrl}/salepage"</code> */
    public static final String SF_METHOD_SALE_DETAILS = "${baseUrl}/salepage";

}
