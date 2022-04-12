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

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * <p>ContextProperties interface.</p>
 *
 * @author Rafael Luis Ibasco
 */
public interface ContextProperties {
    /**
     * <p>autoRelease.</p>
     *
     * @return a boolean
     */
    boolean autoRelease();

    /**
     * <p>autoRelease.</p>
     *
     * @param autoRelease a boolean
     */
    void autoRelease(boolean autoRelease);

    /**
     * <p>localAddress.</p>
     *
     * @return a {@link java.net.InetSocketAddress} object
     */
    InetSocketAddress localAddress();

    /**
     * <p>remoteAddress.</p>
     *
     * @return a {@link java.net.InetSocketAddress} object
     */
    InetSocketAddress remoteAddress();

    /**
     * <p>request.</p>
     *
     * @param <V> a V class
     * @return a V object
     */
    <V extends AbstractRequest> V request();

    /**
     * <p>request.</p>
     *
     * @param request a {@link com.ibasco.agql.core.AbstractRequest} object
     */
    void request(AbstractRequest request);

    /**
     * <p>response.</p>
     *
     * @param <V> a V class
     * @return a V object
     */
    <V extends AbstractResponse> V response();

    /**
     * <p>error.</p>
     *
     * @return a {@link java.lang.Throwable} object
     */
    Throwable error();

    /**
     * <p>writeInProgress.</p>
     *
     * @return a boolean
     */
    boolean writeInProgress();

    /**
     * <p>writeDone.</p>
     *
     * @return a boolean
     */
    boolean writeDone();

    /**
     * <p>writeInError.</p>
     *
     * @return a boolean
     */
    boolean writeInError();

    /**
     * <p>beginWrite.</p>
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    CompletableFuture<NettyChannelContext> beginWrite();

    /**
     * <p>endWrite.</p>
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    CompletableFuture<NettyChannelContext> endWrite();

    /**
     * <p>endWrite.</p>
     *
     * @param error a {@link java.lang.Throwable} object
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    CompletableFuture<NettyChannelContext> endWrite(Throwable error);

    /**
     * <p>envelope.</p>
     *
     * @param <A> a A class
     * @return a {@link com.ibasco.agql.core.Envelope} object
     */
    <A extends AbstractRequest> Envelope<A> envelope();

    /**
     * <p>responsePromise.</p>
     *
     * @param <V> a V class
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    <V extends AbstractResponse> CompletableFuture<V> responsePromise();

    /**
     * Reset context properties. This should re-initialize the response and write promises, clear error
     */
    void reset();
}
