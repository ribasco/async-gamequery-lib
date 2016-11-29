package com.ibasco.agql.examples.base;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Closeable;
import java.util.Scanner;

abstract public class BaseExample implements Closeable {
    abstract public void run() throws Exception;

    protected String promptInput(String message, boolean required) {
        return promptInput(message, required, null);
    }

    protected Boolean promptInputBool(String message, boolean required, String defaultReturnValue) {
        String tmpVal = promptInput(message, required, defaultReturnValue);
        return tmpVal != null ? BooleanUtils.toBoolean(tmpVal) : null;
    }

    @SuppressWarnings("unchecked")
    protected String promptInput(String message, boolean required, String defaultReturnValue) {
        Scanner userInput = new Scanner(System.in);
        String returnValue;
        boolean inputEmpty;
        int retryCounter = 0;
        do {
            System.out.printf("%s", message);
            returnValue = StringUtils.defaultIfEmpty(userInput.nextLine(), defaultReturnValue);
            inputEmpty = StringUtils.isEmpty(returnValue);
        } while ((inputEmpty && ++retryCounter < 3) && required);
        //If the token is still empty, throw an error
        if (inputEmpty && required) {
            System.err.println("Required parameter is missing");
        } else if (inputEmpty && !StringUtils.isEmpty(defaultReturnValue)) {
            returnValue = defaultReturnValue;
        }
        return returnValue;
    }
}