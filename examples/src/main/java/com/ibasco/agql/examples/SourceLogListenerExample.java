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

package com.ibasco.agql.examples;

import com.ibasco.agql.examples.base.BaseExample;
import com.ibasco.agql.protocols.valve.source.query.logger.SourceLogEntry;
import com.ibasco.agql.protocols.valve.source.query.logger.SourceLogListenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * <p>SourceLogListenerExample class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SourceLogListenerExample extends BaseExample {

    private static final Logger log = LoggerFactory.getLogger(SourceLogListenerExample.class);

    private SourceLogListenService logListenService;

    private static void processLogData(SourceLogEntry message) {
        System.out.printf("\u001B[36m%s:\u001B[0m %s\n", message.getSourceAddress(), message.getMessage());
    }

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects
     * @throws java.lang.Exception if any.
     */
    public static void main(String[] args) throws Exception {
        new SourceLogListenerExample().run(args);
    }

    /** {@inheritDoc} */
    @Override
    public void run(String[] args) throws Exception {
        String address = promptInput("Please enter server address to listen on: ", true, null, "listenAddress");
        int port = promptInputInt("Please enter the server port (default: 27500) : ", false, "27500");
        logListenService = new SourceLogListenService(new InetSocketAddress(address, port), SourceLogListenerExample::processLogData);
        CompletableFuture<Void> f = logListenService.listen();
        f.join();
        System.out.println("Log service has been closed");
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {
        logListenService.close();
    }
}
