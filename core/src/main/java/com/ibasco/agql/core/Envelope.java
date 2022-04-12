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

package com.ibasco.agql.core;

import com.google.common.base.Supplier;

import java.net.SocketAddress;

/**
 * <p>Envelope interface.</p>
 *
 * @author Rafael Luis Ibasco
 */
public interface Envelope<C> extends Supplier<Envelope<C>> {

    /**
     * <p>content.</p>
     *
     * @return a C object
     */
    C content();

    /**
     * <p>content.</p>
     *
     * @param content a C object
     */
    void content(C content);

    /**
     * <p>recipient.</p>
     *
     * @param <A> a A class
     * @return a A object
     */
    <A extends SocketAddress> A recipient();

    /**
     * <p>recipient.</p>
     *
     * @param recipient a A object
     * @param <A> a A class
     */
    <A extends SocketAddress> void recipient(A recipient);

    /**
     * <p>sender.</p>
     *
     * @param <A> a A class
     * @return a A object
     */
    <A extends SocketAddress> A sender();

    /**
     * <p>sender.</p>
     *
     * @param sender a A object
     * @param <A> a A class
     */
    <A extends SocketAddress> void sender(A sender);
}
