package org.ribasco.agql.examples;

import org.ribasco.agql.protocols.valve.csgo.webapi.CsgoWebApiClient;
import org.ribasco.agql.protocols.valve.csgo.webapi.interfaces.CsgoServers;
import org.ribasco.agql.protocols.valve.csgo.webapi.pojos.CsgoGameServerStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class CsgoWebApiExample {
    private static final Logger log = LoggerFactory.getLogger(CsgoWebApiExample.class);

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        String authToken = "903BC0B13739EF74242523BC3013F076";
        CsgoWebApiClient apiClient = new CsgoWebApiClient(authToken);

        CsgoServers servers = new CsgoServers(apiClient);

        try {
            CsgoGameServerStatus status = servers.getGameServerStatus().get();
            log.info("Game Server Status : {}", status);
        } finally {
            apiClient.close();
        }
    }
}
