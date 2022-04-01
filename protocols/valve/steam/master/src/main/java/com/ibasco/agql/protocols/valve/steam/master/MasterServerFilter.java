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

package com.ibasco.agql.protocols.valve.steam.master;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>A Master Server Filter Utility class</p>
 */
public final class MasterServerFilter {

    private int appId;

    private boolean allServers;

    private final StringBuffer filter;

    /**
     * Default constructor
     */
    private MasterServerFilter() {
        filter = new StringBuffer();
    }

    /**
     * <p>A factory method to create a new {@link MasterServerFilter} instance</p>
     *
     * @return Instance of {@link MasterServerFilter}
     */
    public static MasterServerFilter create() {
        return new MasterServerFilter();
    }

    /**
     * A filter to return all available servers. Once called, every other filter will be overriden and ignored.
     *
     * @return Instance of {@link MasterServerFilter}
     */
    public MasterServerFilter allServers() {
        allServers = true;
        return this;
    }

    /**
     * <p>Servers that are spectator proxies</p>
     *
     * @param value
     *         Set to True to filter spectator proxy servers
     *
     * @return Instance of {@link MasterServerFilter}
     */
    public MasterServerFilter isSpecProxy(Boolean value) {
        return create("proxy", value);
    }

    /**
     * <p>Servers that are not full</p>
     *
     * @param value
     *         Set to True to filter servers that are full
     *
     * @return Instance of {@link MasterServerFilter}
     */
    public MasterServerFilter isFull(Boolean value) {
        return create("full", value);
    }

    /**
     * <p>Servers that are not empty</p>
     *
     * @param value Set to true to only filter servers that are empty
     *
     * @return Instance of {@link MasterServerFilter}
     */
    public MasterServerFilter isEmpty(Boolean value) {
        return create("empty", value);
    }

    /**
     * <p>Filter for password protected servers</p>
     *
     * @param value Set to true to only filter servers that are password protected
     *
     * @return Instance of {@link MasterServerFilter}
     */
    public MasterServerFilter isPasswordProtected(Boolean value) {
        return create("password", value);
    }

    /**
     * <p>Servers running on a Linux platform</p>
     *
     * @param value Set to true to filter servers only running under linux
     *
     * @return Instance of {@link MasterServerFilter}
     */
    public MasterServerFilter isLinuxServer(Boolean value) {
        return create("linux", value);
    }

    /**
     * <p>Servers running the specified map (ex. cs_italy)</p>
     *
     * @param value Map name
     *
     * @return Instance of {@link MasterServerFilter}
     */
    public MasterServerFilter mapName(String value) {
        return create("map", value);
    }

    /**
     * <p>Servers running the specified modification (ex. cstrike)</p>
     *
     * @param value The mode/game directory name (e.g. cstrike)
     *
     * @return Instance of {@link MasterServerFilter}
     */
    public MasterServerFilter gamedir(String value) {
        return create("gamedir", value);
    }

    /**
     * <p>Servers using anti-cheat technology (VAC, but potentially others as well)</p>
     *
     * @param value Set to true to filter only secure servers (VAC protected)
     *
     * @return Instance of {@link MasterServerFilter}
     */
    public MasterServerFilter isSecure(Boolean value) {
        return create("secure", value);
    }

    /**
     * <p>Servers running dedicated</p>
     *
     * @param value Set to true to filter only dedicated servers
     *
     * @return Instance of {@link MasterServerFilter}
     */
    public MasterServerFilter dedicated(Boolean value) {
        return create("dedicated", value);
    }

    /**
     * <p>A special filter, specifies that servers matching all of the following [x] conditions should not be
     * returned</p>
     *
     * @return Instance of {@link MasterServerFilter}
     */
    public MasterServerFilter nand() {
        return create("nand", "");
    }

    /**
     * <p>A special filter, specifies that servers matching any of the following [x] conditions should not be
     * returned</p>
     *
     * @return Instance of {@link MasterServerFilter}
     */
    public MasterServerFilter nor() {
        return create("nor", "");
    }

    /**
     * <p>Servers that are NOT running game [appid] (This was introduced to block Left 4 Dead games from the Steam
     * Server Browser)</p>
     *
     * @param appId
     *         An integer representing the appId of a game
     *
     * @return Instance of {@link MasterServerFilter}
     */
    public MasterServerFilter napp(Integer appId) {
        if (appId != null && appId > 0)
            return create("napp", appId);
        return this;
    }

    /**
     * <p>Servers that are empty</p>
     *
     * @param value Set to true to filter only empty servers
     *
     * @return Instance of {@link MasterServerFilter}
     */
    public MasterServerFilter hasNoPlayers(Boolean value) {
        return create("noplayers", value);
    }

    /**
     * <p>Servers with all of the given tag(s) in sv_tags</p>
     *
     * @param tags
     *         A {@link String} array of tags
     *
     * @return Instance of {@link MasterServerFilter}
     */
    public MasterServerFilter gametypes(String... tags) {
        return create("gametype", StringUtils.join(tags, ","));
    }

    /**
     * Servers with ALL of the given tag(s) in their 'hidden' tags (e.g. L4D2)
     *
     * @param tags
     *         Array of String game server tags
     *
     * @return Instance of {@link MasterServerFilter}
     */
    public MasterServerFilter gamedata(String... tags) {
        return create("gamedata", StringUtils.join(tags, ","));
    }

    /**
     * Servers with ANY of the given tag(s) in their 'hidden' tags (e.g. L4D2)
     *
     * @param tags
     *         Array of String game server tags
     *
     * @return Instance of {@link MasterServerFilter}
     */
    public MasterServerFilter gamedataOr(String... tags) {
        return create("gamedataor", StringUtils.join(tags, ","));
    }

    /**
     * Servers with their hostname matching [hostname]
     *
     * @param nameWildcard
     *         Hostname to lookup (can use * as a wildcard)
     *
     * @return Instance of {@link MasterServerFilter}
     */
    public MasterServerFilter withHostName(String nameWildcard) {
        return create("name_match", nameWildcard);
    }

    /**
     * Servers running version [version]
     *
     * @param version
     *         Version to search (can use * as a wildcard)
     *
     * @return Instance of {@link MasterServerFilter}
     */
    public MasterServerFilter hasVersion(String version) {
        return create("version_match", version);
    }

    /**
     * Return only one server for each unique IP address matched
     *
     * @param value
     *         Set to True to return only one server for each unique IP
     *
     * @return Instance of {@link MasterServerFilter}
     */
    public MasterServerFilter onlyOneServerPerUniqueIp(Boolean value) {
        return create("collapse_addr_hash", value);
    }

    /**
     * <p>Return only servers on the specified IP address (port supported and optional)</p>
     *
     * @param ipPort
     *         IP[:port] format
     *
     * @return Instance of {@link MasterServerFilter}
     */
    public MasterServerFilter hasServerIp(String ipPort) {
        return create("gameaddr", ipPort);
    }

    /**
     * <p>Servers that are whitelisted</p>
     *
     * @param value Set to true to filter only whitelisted servers
     *
     * @return Instance of {@link MasterServerFilter}
     */
    public MasterServerFilter isWhitelisted(Boolean value) {
        return create("white", value);
    }

    /**
     * <p>Servers that are running game [appid]</p>
     *
     * @param appId
     *         An integer representing the appId of a game
     *
     * @return Instance of {@link MasterServerFilter}
     */
    public MasterServerFilter appId(Integer appId) {
        if (appId > 0) {
            this.appId = appId;
            return create("appId", appId);
        }
        return this;
    }

    /**
     * A utility method to create a Key-Value string pair
     *
     * @param key
     *         A {@link String} representing the key
     * @param value
     *         A {@link String} representing the value associated with the key
     *
     * @return Instance of {@link MasterServerFilter}
     */
    private MasterServerFilter create(String key, Object value) {
        if (StringUtils.isEmpty(key) || value == null)
            return this;

        Object tmpValue = value;

        if (tmpValue instanceof Boolean)
            tmpValue = ((Boolean) tmpValue) ? "1" : "0";

        if ("and".equals(key) || "or".equals(key) || "nor".equals(key))
            filter.append("\\").append(key);
        else
            filter.append("\\").append(key).append("\\").append(tmpValue);
        return this;
    }

    /**
     * Returns a {@link String} representation of this filter builder instance
     *
     * @return A {@link String}
     */
    @Override
    public String toString() {
        if (allServers) {
            if (appId > 0) {
                filter.setLength(0);
                appId(this.appId);
            }
        }
        return filter.toString();
    }
}
