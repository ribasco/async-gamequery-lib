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

package com.ibasco.agql.examples.base;

import com.ibasco.agql.core.Client;
import com.ibasco.agql.core.exceptions.AsyncGameLibUncheckedException;
import com.ibasco.agql.core.util.ConcurrentUtil;
import com.ibasco.agql.core.util.EncryptUtil;
import com.ibasco.agql.core.util.Strings;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;

@SuppressWarnings("SameParameterValue")
abstract public class BaseExample implements Closeable {

    private static final Logger log = LoggerFactory.getLogger(BaseExample.class);

    private static final String EXAMPLE_PROP_FILE = "example.properties";

    private final Properties exampleProps = new Properties();

    abstract public void run(String[] args) throws Exception;

    public BaseExample() {
        loadProps();
    }

    private void loadProps() {
        InputStream is;

        // First try loading from the current directory
        try {
            File f = new File(EXAMPLE_PROP_FILE);
            is = new FileInputStream(f);
        } catch (Exception e) {
            is = null;
        }

        try {
            if (is == null) {
                // Try loading from classpath
                is = getClass().getResourceAsStream(EXAMPLE_PROP_FILE);
            }
            if (is == null) {
                File f = new File(EXAMPLE_PROP_FILE);
                OutputStream out = new FileOutputStream(f);
                exampleProps.store(out, "AUTO GENERATED FILE");
            }
            // Try loading properties from the file (if found)
            exampleProps.load(is);
        } catch (Exception ignored) {
        }
    }

    public void saveProp(String property, String value) {
        try {
            String tmpValue = value == null ? "null" : value;
            exampleProps.setProperty(property, tmpValue);
            File f = new File(EXAMPLE_PROP_FILE);
            OutputStream out = new FileOutputStream(f);
            exampleProps.store(out, String.format("From '%s'", this.getClass().getSimpleName()));
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    protected String getProp(String propertyName) {
        String tmp = exampleProps.getProperty(propertyName);
        return "null".equalsIgnoreCase(tmp) ? null : tmp;
    }

    protected String promptInput(String message, boolean required) {
        return promptInput(message, required, null);
    }

    protected Boolean promptInputBool(String message, boolean required, String defaultReturnValue) {
        return promptInputBool(message, required, defaultReturnValue, null);
    }

    protected Boolean promptInputBool(String message, boolean required, String defaultReturnValue, String defaultProperty) {
        String tmpVal = promptInput(message, required, defaultReturnValue, defaultProperty);
        return tmpVal != null && !"skip".equalsIgnoreCase(tmpVal.trim()) ? BooleanUtils.toBoolean(tmpVal) : null;
    }

    protected Integer promptInputInt(String message, boolean required, String defaultReturnValue) {
        return promptInputInt(message, required, defaultReturnValue, null);
    }

    protected Integer promptInputInt(String message, boolean required, String defaultReturnValue, String defaultProperty) {
        String tmpVal = promptInput(message, required, defaultReturnValue, defaultProperty);
        return tmpVal != null && !"skip".equalsIgnoreCase(tmpVal.trim()) ? Integer.valueOf(tmpVal) : null;
    }

    protected String promptInput(String message, boolean required, String defaultReturnValue) {
        return promptInput(message, required, defaultReturnValue, null);
    }

    private final Scanner userInput = new Scanner(System.in);

    protected String promptInput(String message, boolean required, String defaultReturnValue, String defaultProperty) {
        String returnValue;
        //perform some bit of magic to determine if the prompt is a password type
        boolean inputEmpty, isPassword = StringUtils.containsIgnoreCase(message, "password");
        int retryCounter = 0;
        String defaultValue = defaultReturnValue;

        //Get value from file (if available)
        if (!StringUtils.isEmpty(defaultProperty)) {
            if (isPassword) {
                try {
                    String defaultProp = getProp(defaultProperty);
                    if (!StringUtils.isEmpty(defaultProp))
                        defaultValue = EncryptUtil.decrypt(defaultProp);
                } catch (Exception e) {
                    throw new AsyncGameLibUncheckedException(e);
                }
            } else {
                defaultValue = getProp(defaultProperty);
            }
        }

        do {
            if (!StringUtils.isEmpty(defaultValue)) {
                if (isPassword) {
                    System.out.printf("\033[0;33m%s\033[0m \033[0;36m[%s]\033[0m: ", message, RegExUtils.replaceAll(defaultValue, ".", "*"));
                } else
                    System.out.printf("\033[0;33m%s\033[0m \033[0;36m[%s]\033[0m: ", message, defaultValue);
            } else {
                System.out.printf("\033[0;33m%s\033[0m: ", message);
            }
            System.out.flush();
            returnValue = Strings.defaultIfEmpty(userInput.nextLine(), defaultValue);
            inputEmpty = Strings.isBlank(returnValue);
        } while ((inputEmpty && ++retryCounter < 3) && required);

        //If the token is still empty, throw an error
        if (inputEmpty && required) {
            System.err.println("Required parameter is missing");
        } else if (inputEmpty && !StringUtils.isEmpty(defaultValue)) {
            returnValue = defaultValue;
        }

        //Save to properties file
        if (!StringUtils.isEmpty(defaultProperty)) {
            if (isPassword) {
                try {
                    saveProp(defaultProperty, EncryptUtil.encrypt(returnValue));
                } catch (Exception e) {
                    throw new AsyncGameLibUncheckedException(e);
                }
            } else {
                saveProp(defaultProperty, returnValue);
            }
        }

        return returnValue;
    }

    protected void close(Client client, String name) throws IOException {
        System.out.printf("(CLOSE) \033[0;35mClosing \033[0;33m'%s'\033[0;35m client: \033[0m", name);
        String status;
        if (client != null) {
            client.close();
            status = "\033[0;32mOK\033[0m";
        } else {
            status = "\033[0;32mSKIPPED\033[0m";
        }
        ConcurrentUtil.sleepUninterrupted(500);
        System.out.print(status);
        System.out.println();
    }

    protected void close(ExecutorService executorService, String name) {
        if (executorService == null)
            return;
        System.out.printf("(CLOSE) \033[0;35mClosing \033[0;33m'%s'\033[0;35m executor service: \033[0m", name);
        String status;
        if (ConcurrentUtil.shutdown(executorService)) {
            status = "\033[0;32mOK\033[0m";
        } else {
            status = "\033[0;32mSKIPPED\033[0m";
        }
        ConcurrentUtil.sleepUninterrupted(100);
        System.out.print(status);
        System.out.println();
    }

    public final void clearConsole() {
        ExampleRunner.clearConsole();
    }

    public static void printHeader(String msg, Object... args) {
        if (Strings.isBlank(msg))
            return;
        int lineCount = msg.length() * 2;
        printLine(lineCount);
        System.out.printf("\033[0;33m" + msg + "\033[0m\n", args);
        printLine(lineCount);
    }

    public static void printLine() {
        printLine(130);
    }

    public static void printLine(int count) {
        System.out.printf("\033[0;36m%s\033[0m\n", StringUtils.repeat('=', count));
        System.out.flush();
    }
}