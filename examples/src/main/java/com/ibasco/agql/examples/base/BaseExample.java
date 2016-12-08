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

import com.ibasco.agql.core.exceptions.AsyncGameLibUncheckedException;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.Base64;
import java.util.Properties;
import java.util.Scanner;

abstract public class BaseExample implements Closeable {
    private static final String worldsMostSecureUnhackableIvKey = "aGqLsOurc3querYs";
    private static String worldsMostSecureUnhackableKey = "0123456789abcdef";
    private static final Logger log = LoggerFactory.getLogger(BaseExample.class);
    private static final String EXAMPLE_PROP_FILE = "example.properties";
    private Properties exampleProps = new Properties();

    abstract public void run() throws Exception;

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
        } catch (Exception e) {
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
            e.printStackTrace();
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
        return tmpVal != null ? BooleanUtils.toBoolean(tmpVal) : null;
    }

    protected String promptInput(String message, boolean required, String defaultReturnValue) {
        return promptInput(message, required, defaultReturnValue, null);
    }

    @SuppressWarnings("unchecked")
    protected String promptInput(String message, boolean required, String defaultReturnValue, String defaultProperty) {
        Scanner userInput = new Scanner(System.in);
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
                        defaultValue = decrypt(defaultProp);
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
                    System.out.printf("%s [%s]: ", message, StringUtils.replaceAll(defaultValue, ".", "*"));
                } else
                    System.out.printf("%s [%s]: ", message, defaultValue);
            } else {
                System.out.printf("%s: ", message);
            }
            System.out.flush();
            returnValue = StringUtils.defaultIfEmpty(userInput.nextLine(), defaultValue);
            inputEmpty = StringUtils.isEmpty(returnValue);
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
                    saveProp(defaultProperty, encrypt(returnValue));
                } catch (Exception e) {
                    throw new AsyncGameLibUncheckedException(e);
                }
            } else {
                saveProp(defaultProperty, returnValue);
            }
        }

        return returnValue;
    }

    /**
     * @see <a href="https://gist.github.com/bricef/2436364">https://gist.github.com/bricef/2436364</a>
     */
    public static String encrypt(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
        SecretKeySpec key = new SecretKeySpec(worldsMostSecureUnhackableKey.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(worldsMostSecureUnhackableIvKey.getBytes("UTF-8")));
        return Base64.getEncoder().encodeToString((cipher.doFinal(padNullBytes(plainText))));
    }

    /**
     * @see <a href="https://gist.github.com/bricef/2436364">https://gist.github.com/bricef/2436364</a>
     */
    public static String decrypt(String cipherText) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
        SecretKeySpec key = new SecretKeySpec(worldsMostSecureUnhackableKey.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(worldsMostSecureUnhackableIvKey.getBytes("UTF-8")));
        byte[] cipherBytes = Base64.getDecoder().decode(cipherText);
        return new String(cipher.doFinal(cipherBytes), "UTF-8");
    }


    private static byte[] padNullBytes(String text) {
        if (StringUtils.isEmpty(text))
            return null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            bos.write(text.getBytes("UTF-8"));
            while ((bos.size() % 16) != 0) {
                bos.write(0);
            }
        } catch (IOException e) {
            throw new AsyncGameLibUncheckedException(e);
        }
        return bos.toByteArray();
    }
}