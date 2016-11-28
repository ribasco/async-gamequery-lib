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

package com.ibasco.agql.examples.base;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStream;
import java.io.InputStreamReader;

abstract public class BaseWebApiAuthExample implements BaseExample {

    protected String getToken(String key) {
        JsonParser parser = new JsonParser();
        InputStream authFile = getClass().getResourceAsStream("/auth.json");
        if (authFile != null) {
            JsonElement root = parser.parse(new JsonReader(new BufferedReader(new InputStreamReader(authFile))));
            String token = root.getAsJsonObject().get(key).getAsString();
            if (!StringUtils.isEmpty(token))
                return token;
        }

        Console c = System.console();
        String token = null;
        boolean tokenEmpty = true;
        int retryCounter = 0;

        //Ask the user for the token
        do {
            token = c.readLine("Please input your API Token");
            tokenEmpty = StringUtils.isEmpty(token);
        } while (tokenEmpty && ++retryCounter < 3);

        //If the token is still empty, throw an error
        if (tokenEmpty) {
            throw new RuntimeException("No API Token provided");
        }

        return token;
    }
}
