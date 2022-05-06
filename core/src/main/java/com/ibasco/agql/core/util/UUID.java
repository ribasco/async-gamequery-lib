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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import java.io.Serializable;
import java.util.Base64;
import java.util.Random;

/**
 * <p>UUID class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class UUID implements Serializable {

    private final byte[] data;

    private final int dataInt;

    private String dataStr;

    private UUID() {
        Random sRand = new Random();
        byte[] rbytes = new byte[8];
        sRand.nextBytes(rbytes);
        this.data = rbytes;
        //compute integer
        byte[] intBytes = new byte[4];
        intBytes[0] = data[0];
        intBytes[1] = data[2];
        intBytes[2] = data[4];
        intBytes[3] = data[6];
        this.dataInt = Math.abs(Bytes.toIntegerLE(intBytes));
    }

    /**
     * <p>create.</p>
     *
     * @return a {@link com.ibasco.agql.core.util.UUID} object
     */
    public static UUID create() {
        return new UUID();
    }

    /**
     * <p>getBytes.</p>
     *
     * @return an array of {@code byte} objects
     */
    public byte[] getBytes() {
        return data;
    }

    /**
     * <p>The integer representation of the instance</p>
     *
     * @return a int
     */
    public int getInteger() {
        return this.dataInt;
    }

    /**
     * <p>nextInteger.</p>
     *
     * @return a int
     */
    public int nextInteger() {
        byte[] intBytes = new byte[4];
        for (int i = 0; i < intBytes.length; i++)
            intBytes[i] = data[RandomUtils.nextInt(0, 8)];
        return Bytes.toIntegerLE(intBytes);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(dataInt).toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UUID uuid = (UUID) o;
        return new EqualsBuilder().append(dataInt, uuid.dataInt).isEquals();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        if (dataStr == null) {
            dataStr = Base64.getEncoder().encodeToString(data).toUpperCase().replaceAll("[/=+]", "");
        }
        return dataStr;
    }
}
