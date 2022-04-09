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

import org.apache.commons.lang3.RandomUtils;

import java.io.Serializable;
import java.util.Base64;
import java.util.Random;

public class UUID implements Serializable {

    private transient static Random sRand;

    private byte[] data;

    private String dataStr;

    private UUID() {
        //sRand = SecureRandom.getInstanceStrong();
        sRand = new Random();
    }

    public static UUID create() {
        UUID id = new UUID();
        byte[] rbytes = new byte[8];
        sRand.nextBytes(rbytes);
        id.data = rbytes;
        return id;
    }

    public byte[] getBytes() {
        return data;
    }

    public int getInteger() {
        byte[] intBytes = new byte[4];
        intBytes[0] = data[0];
        intBytes[1] = data[2];
        intBytes[2] = data[4];
        intBytes[3] = data[6];
        return Math.abs(Bytes.toIntegerLE(intBytes));
    }

    public int nextInteger() {
        byte[] intBytes = new byte[4];
        for (int i = 0; i < intBytes.length; i++)
            intBytes[i] = data[RandomUtils.nextInt(0, 8)];
        return Bytes.toIntegerLE(intBytes);
    }

    @Override
    public String toString() {
        if (dataStr == null) {
            dataStr = Base64.getEncoder().encodeToString(data).toUpperCase().replaceAll("[/=+]", "");
        }
        return dataStr;
    }
}
