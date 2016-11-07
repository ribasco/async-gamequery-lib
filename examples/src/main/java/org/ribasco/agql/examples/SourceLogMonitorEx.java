package org.ribasco.agql.examples;

import org.ribasco.agql.protocols.valve.source.query.logger.SourceLogEntry;
import org.ribasco.agql.protocols.valve.source.query.logger.SourceLogListenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class SourceLogMonitorEx {
    private static final Logger log = LoggerFactory.getLogger(SourceLogMonitorEx.class);

    public static void main(String[] args) {
        SourceLogListenService logListenService = new SourceLogListenService(new InetSocketAddress("192.168.1.10", 27500), SourceLogMonitorEx::processLogData);
        logListenService.listen();
    }

    private static void processLogData(SourceLogEntry message) {
        log.info("Got Data : {}", message);
    }
}
