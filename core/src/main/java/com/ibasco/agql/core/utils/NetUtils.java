package com.ibasco.agql.core.utils;

import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetUtils {

    private static final Pattern PATTERN_IP = Pattern.compile("(?<ip>\\b(?:[0-9]{1,3}\\.){3}[0-9]{1,3}\\b)|\\:(?<port>\\d*)");

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
            if (StringUtils.isBlank(portStr) || !StringUtils.isNumeric(portStr))
                throw new ParseException(String.format("Invalid port number '%s'", portStr), 0);
            port = Integer.parseInt(portStr);
        }
        if (port == null)
            port = defaultPort;
        return new InetSocketAddress(ip, port);
    }
}
