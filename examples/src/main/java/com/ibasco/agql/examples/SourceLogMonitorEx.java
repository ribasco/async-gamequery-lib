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

package com.ibasco.agql.examples;

import com.ibasco.agql.core.utils.ConcurrentUtils;
import com.ibasco.agql.examples.base.BaseExample;
import com.ibasco.agql.protocols.valve.source.query.logger.SourceLogEntry;
import com.ibasco.agql.protocols.valve.source.query.logger.SourceLogListenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

public class SourceLogMonitorEx extends BaseExample {
    private static final Logger log = LoggerFactory.getLogger(SourceLogMonitorEx.class);

    private SourceLogListenService logListenService;

    private static void processLogData(SourceLogEntry message) {
        log.info("{}", message);
    }

    public static void main(String[] args) throws Exception {
        SourceLogMonitorEx app = new SourceLogMonitorEx();
        app.run();
    }

    @Override
    public void run() throws Exception {
        String address = promptInput("Please enter server address to listen on: ", true, null, "listenAddress");
        int port = Integer.valueOf(promptInput("Please enter the server port (default: 27500) : ", false, "27500"));
        logListenService = new SourceLogListenService(new InetSocketAddress(address, port), SourceLogMonitorEx::processLogData);
        logListenService.listen();
        log.info("Listening to {}:{}", address, port);
        ConcurrentUtils.sleepUninterrupted(99999999);
    }

    @Override
    public void close() throws IOException {
        logListenService.close();
    }
}