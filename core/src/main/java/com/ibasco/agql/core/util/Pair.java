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

/**
 * <p>Pair class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class Pair<A, B> {

    private A first;

    private B second;

    /**
     * <p>Constructor for Pair.</p>
     */
    public Pair() {}

    /**
     * <p>Constructor for Pair.</p>
     *
     * @param first
     *         a A object
     * @param second
     *         a B object
     */
    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    /**
     * <p>Getter for the field <code>first</code>.</p>
     *
     * @return a A object
     */
    public A getFirst() {
        return first;
    }

    /**
     * <p>Setter for the field <code>first</code>.</p>
     *
     * @param first
     *         a A object
     */
    public void setFirst(A first) {
        this.first = first;
    }

    /**
     * <p>Getter for the field <code>second</code>.</p>
     *
     * @return a B object
     */
    public B getSecond() {
        return second;
    }

    /**
     * <p>Setter for the field <code>second</code>.</p>
     *
     * @param second
     *         a B object
     */
    public void setSecond(B second) {
        this.second = second;
    }

    /**
     * <p>setBoth.</p>
     *
     * @param first
     *         a A object
     * @param second
     *         a B object
     *
     * @return a {@link com.ibasco.agql.core.util.Pair} object
     */
    public Pair<A, B> setBoth(A first, B second) {
        this.first = first;
        this.second = second;
        return this;
    }
}
