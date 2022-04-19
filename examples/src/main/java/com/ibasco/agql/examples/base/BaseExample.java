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
import com.ibasco.agql.core.exceptions.AgqlRuntimeException;
import com.ibasco.agql.core.util.Concurrency;
import com.ibasco.agql.core.util.Encryption;
import com.ibasco.agql.core.util.Strings;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;

/**
 * <p>Abstract BaseExample class.</p>
 *
 * @author Rafael Luis Ibasco
 */
@SuppressWarnings("SameParameterValue")
abstract public class BaseExample implements Closeable {

    private static final Logger log = LoggerFactory.getLogger(BaseExample.class);

    private static final String EXAMPLE_PROP_FILE = "example.properties";

    private final Scanner userInput = new Scanner(System.in);

    private final Properties exampleProps = new Properties();

    /**
     * <p>run.</p>
     *
     * @param args an array of {@link java.lang.String} objects
     * @throws java.lang.Exception if any.
     */
    abstract public void run(String[] args) throws Exception;

    /**
     * <p>Constructor for BaseExample.</p>
     */
    public BaseExample() {
        loadProps();
    }

    private void loadProps() {
        InputStream is;

        // First try loading from the current directory
        try {
            File f = new File(EXAMPLE_PROP_FILE);
            is = Files.newInputStream(f.toPath());
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

    /**
     * <p>saveProp.</p>
     *
     * @param property a {@link java.lang.String} object
     * @param value a {@link java.lang.String} object
     */
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

    /**
     * <p>getProp.</p>
     *
     * @param propertyName a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    protected String getProp(String propertyName) {
        String tmp = exampleProps.getProperty(propertyName);
        return "null".equalsIgnoreCase(tmp) ? null : tmp;
    }

    /**
     * <p>promptInput.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param required a boolean
     * @return a {@link java.lang.String} object
     */
    protected String promptInput(String message, boolean required) {
        return promptInput(message, required, null);
    }

    /**
     * <p>promptInputPassword.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param required a boolean
     * @param defaultReturnValue a {@link java.lang.String} object
     * @param defaultProperty a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    protected String promptInputPassword(String message, boolean required, String defaultReturnValue, String defaultProperty) {
        return promptInput(message, required, defaultReturnValue, defaultProperty, true);
    }

    /**
     * <p>promptInputBool.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param required a boolean
     * @param defaultReturnValue a {@link java.lang.String} object
     * @return a {@link java.lang.Boolean} object
     */
    protected Boolean promptInputBool(String message, boolean required, String defaultReturnValue) {
        return promptInputBool(message, required, defaultReturnValue, null);
    }

    /**
     * <p>promptInputBool.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param required a boolean
     * @param defaultReturnValue a {@link java.lang.String} object
     * @param defaultProperty a {@link java.lang.String} object
     * @return a {@link java.lang.Boolean} object
     */
    protected Boolean promptInputBool(String message, boolean required, String defaultReturnValue, String defaultProperty) {
        String tmpVal = promptInput(message, required, defaultReturnValue, defaultProperty, false);
        return tmpVal != null && !"skip".equalsIgnoreCase(tmpVal.trim()) ? BooleanUtils.toBoolean(tmpVal) : null;
    }

    /**
     * <p>promptInputInt.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param required a boolean
     * @param defaultReturnValue a {@link java.lang.String} object
     * @return a {@link java.lang.Integer} object
     */
    protected Integer promptInputInt(String message, boolean required, String defaultReturnValue) {
        return promptInputInt(message, required, defaultReturnValue, null);
    }

    /**
     * <p>promptInputInt.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param required a boolean
     * @param defaultReturnValue a {@link java.lang.String} object
     * @param defaultProperty a {@link java.lang.String} object
     * @return a {@link java.lang.Integer} object
     */
    protected Integer promptInputInt(String message, boolean required, String defaultReturnValue, String defaultProperty) {
        String tmpVal = promptInput(message, required, defaultReturnValue, defaultProperty, false);
        return tmpVal != null && !"skip".equalsIgnoreCase(tmpVal.trim()) ? Integer.valueOf(tmpVal) : null;
    }

    /**
     * <p>promptInput.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param required a boolean
     * @param defaultReturnValue a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    protected String promptInput(String message, boolean required, String defaultReturnValue) {
        return promptInput(message, required, defaultReturnValue, null);
    }

    /**
     * <p>promptInput.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param required a boolean
     * @param defaultReturnValue a {@link java.lang.String} object
     * @param defaultProperty a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    protected String promptInput(String message, boolean required, String defaultReturnValue, String defaultProperty) {
        return promptInput(message, required, defaultReturnValue, defaultProperty, StringUtils.containsIgnoreCase(message, "password"));
    }

    /**
     * <p>promptInput.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param required a boolean
     * @param defaultReturnValue a {@link java.lang.String} object
     * @param defaultProperty a {@link java.lang.String} object
     * @param isPassword a boolean
     * @return a {@link java.lang.String} object
     */
    protected String promptInput(String message, boolean required, String defaultReturnValue, String defaultProperty, boolean isPassword) {
        String returnValue;
        //perform some bit of magic to determine if the prompt is a password type
        boolean inputEmpty;
        int retryCounter = 0;
        String defaultValue = defaultReturnValue;

        //Get value from file (if available)
        if (!StringUtils.isEmpty(defaultProperty)) {
            if (isPassword) {
                try {
                    String defaultProp = getProp(defaultProperty);
                    if (!StringUtils.isEmpty(defaultProp))
                        defaultValue = Encryption.decrypt(defaultProp);
                } catch (Exception e) {
                    throw new AgqlRuntimeException(e);
                }
            } else {
                defaultValue = getProp(defaultProperty);
            }
        }

        do {
            if (!StringUtils.isEmpty(defaultValue)) {
                if (isPassword) {
                    System.out.printf("\033[0;33m%s\033[0m \033[0;36m[%s]\033[0m: ", message, StringUtils.repeat('*', defaultValue.length() / 2));
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
                    saveProp(defaultProperty, Encryption.encrypt(returnValue));
                } catch (Exception e) {
                    throw new AgqlRuntimeException(e);
                }
            } else {
                saveProp(defaultProperty, returnValue);
            }
        }

        return returnValue;
    }

    /**
     * <p>close.</p>
     *
     * @param client a {@link com.ibasco.agql.core.Client} object
     * @param name a {@link java.lang.String} object
     * @throws java.io.IOException if any.
     */
    protected void close(Client client, String name) throws IOException {
        System.out.printf("(CLOSE) \033[0;35mClosing \033[0;33m'%s'\033[0;35m client: \033[0m", name);
        String status;
        if (client != null) {
            client.close();
            status = "\033[0;32mOK\033[0m";
        } else {
            status = "\033[0;32mSKIPPED\033[0m";
        }
        Concurrency.sleepUninterrupted(500);
        System.out.print(status);
        System.out.println();
    }

    /**
     * <p>close.</p>
     *
     * @param executorService a {@link java.util.concurrent.ExecutorService} object
     * @param name a {@link java.lang.String} object
     */
    protected void close(ExecutorService executorService, String name) {
        if (executorService == null)
            return;
        System.out.printf("(CLOSE) \033[0;35mClosing \033[0;33m'%s'\033[0;35m executor service: \033[0m", name);
        String status;
        if (Concurrency.shutdown(executorService)) {
            status = "\033[0;32mOK\033[0m";
        } else {
            status = "\033[0;32mSKIPPED\033[0m";
        }
        Concurrency.sleepUninterrupted(100);
        System.out.print(status);
        System.out.println();
    }

    /**
     * <p>clearConsole.</p>
     */
    public final void clearConsole() {
        ExampleRunner.clearConsole();
    }

    /**
     * <p>printHeader.</p>
     *
     * @param msg a {@link java.lang.String} object
     * @param args a {@link java.lang.Object} object
     */
    public static void printHeader(String msg, Object... args) {
        if (Strings.isBlank(msg))
            return;
        int lineCount = msg.length() * 2;
        printLine(lineCount);
        System.out.printf("\033[0;33m" + msg + "\033[0m\n", args);
        printLine(lineCount);
    }

    /**
     * <p>printLine.</p>
     */
    public static void printLine() {
        printLine(130);
    }

    /**
     * <p>printLine.</p>
     *
     * @param count a int
     */
    public static void printLine(int count) {
        System.out.printf("\033[0;36m%s\033[0m\n", StringUtils.repeat('=', count));
        System.out.flush();
    }
}
