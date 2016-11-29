package com.ibasco.agql.examples.base;

import org.apache.commons.lang3.StringUtils;

import java.io.Closeable;
import java.io.Console;

abstract public class BaseExample implements Closeable {
    abstract public void run() throws Exception;

    protected String promptInput(String message, boolean required) {
        return promptInput(message, required, null);
    }

    @SuppressWarnings("unchecked")
    protected String promptInput(String message, boolean required, String defaultReturnValue) {
        Console c = System.console();

        if (c == null) {
            System.err.println("ERROR: A console interface is required to run the examples.");
            System.exit(1);
            return null;
        }

        String returnValue;
        boolean inputEmpty;
        int retryCounter = 0;
        do {
            returnValue = StringUtils.defaultIfEmpty(c.readLine(message), defaultReturnValue);
            inputEmpty = StringUtils.isEmpty(returnValue);
        } while (inputEmpty && ++retryCounter < 3);
        //If the token is still empty, throw an error
        if (inputEmpty && required) {
            System.err.println("Required parameter is missing");
        } else if (inputEmpty && !StringUtils.isEmpty(defaultReturnValue)) {
            returnValue = defaultReturnValue;
        }
        return returnValue;
    }
}