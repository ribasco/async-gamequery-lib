/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Ibasco
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **************************************************************************************************/

package com.ribasco.rglib.protocols.valve.steam;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by raffy on 9/6/2016.
 */
public class MasterServerFilter {
    private StringBuilder filter;
    private boolean allServersSet = false;

    public MasterServerFilter() {
        filter = new StringBuilder();
    }

    public MasterServerFilter allServers() {
        filter.setLength(0);
        allServersSet = true;
        return this;
    }

    public MasterServerFilter isSpecProxy(boolean value) {
        return create("proxy", new Boolean(value));
    }

    public MasterServerFilter isFull(boolean value) {
        return create("full", new Boolean(value));
    }

    public MasterServerFilter isEmpty(boolean value) {
        return create("empty", new Boolean(value));
    }

    public MasterServerFilter isPasswordProtected(boolean value) {
        return create("password", new Boolean(value));
    }

    public MasterServerFilter isLinuxServer(boolean value) {
        return create("linux", new Boolean(value));
    }

    public MasterServerFilter mapName(String value) {
        return create("map", value);
    }

    public MasterServerFilter gamedir(String value) {
        return create("gamedir", value);
    }

    public MasterServerFilter isSecure(boolean value) {
        return create("secure", new Boolean(value));
    }

    public MasterServerFilter dedicated(boolean value) {
        return create("dedicated", new Boolean(value));
    }

    public MasterServerFilter and() {
        return create("and", "");
    }

    public MasterServerFilter nor() {
        return create("nor", "");
    }

    public MasterServerFilter napp(int appId) {
        if (appId > 0)
            return create("napp", appId);
        return this;
    }

    public MasterServerFilter hasNoPlayers(boolean value) {
        return create("noplayers", new Boolean(value));
    }

    public MasterServerFilter gametypes(String... tags) {
        return create("gametype", StringUtils.join(tags, ","));
    }

    /**
     * Servers with ALL of the given tag(s) in their 'hidden' tags (e.g. L4D2)
     *
     * @param tags Array of String game server tags
     *
     * @return MasterServerFilter
     */
    public MasterServerFilter gamedata(String... tags) {
        return create("gamedata", StringUtils.join(tags, ","));
    }

    /**
     * Servers with ANY of the given tag(s) in their 'hidden' tags (e.g. L4D2)
     *
     * @param tags Array of String game server tags
     *
     * @return MasterServerFilter
     */
    public MasterServerFilter gamedataOr(String... tags) {
        return create("gamedataor", StringUtils.join(tags, ","));
    }

    /**
     * Servers with their hostname matching [hostname]
     *
     * @param nameWildcard Hostname to lookup (can use * as a wildcard)
     *
     * @return MasterServerFilter
     */
    public MasterServerFilter withHostName(String nameWildcard) {
        return create("name_match", nameWildcard);
    }

    /**
     * Servers running version [version]
     *
     * @param version Version to search (can use * as a wildcard)
     *
     * @return MasterServerFilter
     */
    public MasterServerFilter hasVersion(String version) {
        return create("version_match", version);
    }

    /**
     * Return only one server for each unique IP address matched
     *
     * @param value True to return only one server for each unique IP
     *
     * @return MasterServerFilter
     */
    public MasterServerFilter onlyOneServerPerUniqueIp(boolean value) {
        return create("collapse_addr_hash", new Boolean(value));
    }

    /**
     * Return only servers on the specified IP address (port supported and optional)
     *
     * @param ipPort IP[:port] format
     *
     * @return MasterServerFilter
     */
    public MasterServerFilter hasServerIp(String ipPort) {
        return create("gameaddr", ipPort);
    }

    public MasterServerFilter isWhitelisted(boolean value) {
        return create("white", new Boolean(value));
    }

    public MasterServerFilter appId(int appId) {
        if (appId > 0)
            return create("appId", appId);
        return this;
    }

    private MasterServerFilter create(String key, Object value) {
        if (allServersSet)
            throw new RuntimeException("All servers filter have been selected. You can not add additional filters in the chain if this property get set");

        if (StringUtils.isEmpty(key) && value == null) {
            return this;
        }


        if (value instanceof Boolean)
            value = (((Boolean) value).booleanValue()) ? "1" : "0";

        if (value != null) {
            if ("and".equals(key) || "or".equals(key) || "nor".equals(key))
                filter.append("\\").append(key);
            else
                filter.append("\\").append(key).append("\\").append(value);
        }
        return this;
    }

    @Override
    public String toString() {
        return filter.toString();
    }
}
