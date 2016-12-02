package com.ibasco.agql.examples.base;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;
import java.util.Scanner;

abstract public class BaseExample implements Closeable {

    private static final Logger log = LoggerFactory.getLogger(BaseExample.class);
    private static final String EXAMPLE_PROP_FILE = "example.properties";
    private Properties exampleProps = new Properties();

    abstract public void run() throws Exception;

    public BaseExample() {
        loadProps();
    }

    private void loadProps() {
        InputStream is = null;

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
        } catch (Exception e) {
        }
    }

    public void saveProp(String property, String value) {
        try {
            exampleProps.setProperty(property, value);
            File f = new File(EXAMPLE_PROP_FILE);
            OutputStream out = new FileOutputStream(f);
            exampleProps.store(out, String.format("From '%s'", this.getClass().getSimpleName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String getProp(String propertyName) {
        return exampleProps.getProperty(propertyName);
    }

    protected String promptInput(String message, boolean required) {
        return promptInput(message, required, null);
    }

    protected Boolean promptInputBool(String message, boolean required, String defaultReturnValue) {
        String tmpVal = promptInput(message, required, defaultReturnValue);
        return tmpVal != null ? BooleanUtils.toBoolean(tmpVal) : null;
    }

    protected String promptInput(String message, boolean required, String defaultReturnValue) {
        return promptInput(message, required, defaultReturnValue, null);
    }

    @SuppressWarnings("unchecked")
    protected String promptInput(String message, boolean required, String defaultReturnValue, String defaultProperty) {
        Scanner userInput = new Scanner(System.in);
        String returnValue;
        boolean inputEmpty;
        int retryCounter = 0;

        if (!StringUtils.isEmpty(defaultProperty)) {
            defaultReturnValue = getProp(defaultProperty);
        }

        do {
            if (!StringUtils.isEmpty(defaultReturnValue)) {
                System.out.printf("%s [%s]: ", message, defaultReturnValue);
            } else {
                System.out.printf("%s: ", message);
            }
            System.out.flush();
            returnValue = StringUtils.defaultIfEmpty(userInput.nextLine(), defaultReturnValue);
            inputEmpty = StringUtils.isEmpty(returnValue);
        } while ((inputEmpty && ++retryCounter < 3) && required);
        //If the token is still empty, throw an error
        if (inputEmpty && required) {
            System.err.println("Required parameter is missing");
        } else if (inputEmpty && !StringUtils.isEmpty(defaultReturnValue)) {
            returnValue = defaultReturnValue;
        }

        //Save to properties file
        if (!StringUtils.isEmpty(defaultProperty)) {
            saveProp(defaultProperty, returnValue);
        }

        return returnValue;
    }
}