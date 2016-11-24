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

    //Steam URL Properties
    public static final String STEAM_PROP_APPID = "appId";
    public static final String STEAM_PROP_INTERFACE = "interface";
    public static final String STEAM_PROP_METHOD = "method";
    public static final String STEAM_PROP_VERSION = "version";

    //Store Front URL Properties
    public static final String SF_PROP_BASEURL = "baseUrl";

    //Store Front Constants
    public static final String SF_BASE_URL_FORMAT = "http://store.steampowered.com/api";
    public static final String SF_METHOD_APPDETAILS = "${baseUrl}/appdetails";
    public static final String SF_METHOD_FEATURED = "${baseUrl}/featured";
    public static final String SF_METHOD_FEATURED_CATEGORIES = "${baseUrl}/featuredcategories";
    public static final String SF_METHOD_PACKAGE_DETAILS = "${baseUrl}/packagedetails";
    public static final String SF_METHOD_SALE_DETAILS = "${baseUrl}/salepage";
}
