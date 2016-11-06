package org.ribasco.agql.examples;

import org.ribasco.agql.protocols.valve.steam.master.MasterServerFilter;
import org.ribasco.agql.protocols.valve.steam.master.client.MasterServerQueryClient;
import org.ribasco.agql.protocols.valve.steam.master.enums.MasterServerRegion;
import org.ribasco.agql.protocols.valve.steam.master.enums.MasterServerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MasterServerQuery {

    private static final Logger log = LoggerFactory.getLogger(MasterServerQuery.class);

    private MasterServerQueryClient masterServerQueryClient;

    public MasterServerQuery() {
        masterServerQueryClient = new MasterServerQueryClient();
    }

    public static void main(String[] args) throws IOException {
        MasterServerQuery masterQuery = new MasterServerQuery();
        try {
            masterQuery.listAllServers();
        } finally {
            masterQuery.close();
        }
    }

    public void close() throws IOException {
        masterServerQueryClient.close();
    }

    public void listAllServers() {
        MasterServerFilter filter = MasterServerFilter.create().dedicated(true);
        masterServerQueryClient.setSleepTime(8);
        masterServerQueryClient.getServerList(MasterServerType.SOURCE, MasterServerRegion.REGION_ALL, filter, this::displayResult).join();
    }

    public void displayResult(InetSocketAddress address, InetSocketAddress sender, Throwable error) {
        log.info("Server : {}", address);
    }

}
