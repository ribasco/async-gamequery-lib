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

package com.ibasco.agql.core.util;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Net {

    private static final Pattern PATTERN_IP = Pattern.compile("(?<ip>\\b(?:[0-9]{1,3}\\.){3}[0-9]{1,3}\\b)|\\:(?<port>\\d*)");

    public static String hostString(SocketAddress address) {
        if (address == null)
            return "N/A";
        if (address instanceof InetSocketAddress) {
            return ((InetSocketAddress) address).getHostString();
        }
        return address.toString();
    }

    public static InetSocketAddress parseAddress(String address, int defaultPort) throws ParseException {
        if (address == null || address.trim().equalsIgnoreCase(""))
            throw new ParseException("Address cannot be blank", 0);
        Matcher matcher = PATTERN_IP.matcher(address);
        if (!matcher.find())
            throw new ParseException(String.format("Invalid address: '%s'", address), 0);
        String ip = matcher.group("ip");
        Integer port = null;
        if (matcher.find()) {
            String portStr = matcher.group("port");
            if (Strings.isBlank(portStr) || !Strings.isNumeric(portStr))
                throw new ParseException(String.format("Invalid port number '%s'", portStr), 0);
            port = Integer.parseInt(portStr);
        }
        if (port == null)
            port = defaultPort;
        return new InetSocketAddress(ip, port);
    }
}
