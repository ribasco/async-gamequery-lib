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

public interface Envelope<C> extends Supplier<Envelope<C>> {

    C content();

    void content(C content);

    <A extends SocketAddress> A recipient();

    <A extends SocketAddress> void recipient(A recipient);

    <A extends SocketAddress> A sender();

    <A extends SocketAddress> void sender(A sender);
}
