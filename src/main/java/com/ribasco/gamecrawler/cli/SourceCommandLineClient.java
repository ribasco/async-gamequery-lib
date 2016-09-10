/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Ibasco
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **************************************************************************************************/

package com.ribasco.gamecrawler.cli;

import com.ribasco.gamecrawler.clients.SourceClient;
import com.ribasco.gamecrawler.protocols.ResponseCallback;
import com.ribasco.gamecrawler.protocols.valve.server.SourceMasterFilter;
import com.ribasco.gamecrawler.protocols.valve.server.SourcePlayer;
import com.ribasco.gamecrawler.protocols.valve.server.SourceServer;
import com.ribasco.gamecrawler.protocols.valve.server.packets.requests.SourceMasterRequestPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import static com.ribasco.gamecrawler.protocols.valve.server.SourceConstants.REQUEST_PLAYER_HEADER;

public class SourceCommandLineClient {
    private static final Logger log = LoggerFactory.getLogger(SourceCommandLineClient.class);

    public static void main(String[] args) {
        AtomicInteger totalMasterRecSuccess = new AtomicInteger();
        AtomicInteger totalMasterRecError = new AtomicInteger();
        AtomicInteger totalMasterReceived = new AtomicInteger();

        AtomicInteger totalInfoReqSent = new AtomicInteger(); //Total Server Info Requests Sent
        AtomicInteger totalInfoRecSuccess = new AtomicInteger(); //Total Server Info Response Received Successfully
        AtomicInteger totalInfoRecError = new AtomicInteger(); //Total Server Info Response Received in Error
        AtomicInteger totalInfoReceived = new AtomicInteger(); //Sum of response in both error and success
        AtomicInteger totalInfoErrorTimedout = new AtomicInteger();
        AtomicInteger totalInfoErrorOthers = new AtomicInteger();

        AtomicInteger totalChallengeRecieved = new AtomicInteger();
        AtomicInteger totalChallengeNotReceived = new AtomicInteger();
        int cancelled = 0;

        //Run client test code
        try (SourceClient client = new SourceClient()) {

            //Create our filter
            SourceMasterFilter masterFilter = new SourceMasterFilter();
            masterFilter.dedicated(true).isSecure(true);

            log.info("Querying Master Server with Filter {}", masterFilter.toString());

            ResponseCallback<Map<String, String>> sourceRulesResponseCallback;
            sourceRulesResponseCallback = (ruleMap, sender, error) -> {
                if (error != null) {
                    log.error("[RULE:ERROR] From {}:{} ({} = {})", sender.getAddress().getHostAddress(), sender.getPort(), error.getClass().getSimpleName(), error.getMessage());
                } else {
                    log.info("[RULE:INFO] Rules of : {}:{}", sender.getAddress().getHostAddress(), sender.getPort());
                    ruleMap.entrySet().forEach(entry -> log.info("---> {} = {}", entry.getKey(), entry.getValue()));
                }
            };

            ResponseCallback<List<SourcePlayer>> sourcePlayerResponseCallback = (playerList, sender, error) -> {
                if (error != null) {
                    log.error("[PLAYER:ERROR] From {}:{} ({} = {})", sender.getAddress().getHostAddress(), sender.getPort(), error.getClass().getSimpleName(), error.getMessage());
                } else {
                    log.info("[PLAYER:INFO] From {}:{}, Total Players : {}", sender.getAddress().getHostAddress(), sender.getPort(), playerList.size());
                    playerList.forEach(player -> log.info("---> {}", player.toString()));
                }
            };

            ResponseCallback<Integer> sourceChallengeCallback = (challenge, sender, error) -> {
                if (error != null) {
                    log.error("[CHALLENGE:ERROR] From {}:{} ({} = {})", sender.getAddress().getHostAddress(), sender.getPort(), error.getClass().getSimpleName(), error.getMessage());
                    totalChallengeNotReceived.incrementAndGet();
                } else {
                    log.info("[CHALLENGE:INFO]: Challenge Number from {}:{} = {}", sender.getAddress().getHostAddress(), sender.getPort(), challenge);
                    totalChallengeRecieved.incrementAndGet();

                    //Retrieve player from server using challenge number
                    client.getPlayerDetails(challenge, sender, sourcePlayerResponseCallback);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                    }
                    client.getServerRules(challenge, sender, sourceRulesResponseCallback);
                }
            };

            ResponseCallback<SourceServer> sourceInfoCallback = (serverInfo, sender, error) -> {
                if (error != null) {
                    log.error("[SERVERINFO:ERROR] From {}:{} ({} = {})", sender.getAddress().getHostAddress(), sender.getPort(), error.getClass().getSimpleName(), error.getMessage());
                    totalInfoRecError.incrementAndGet();
                    if (error instanceof TimeoutException)
                        totalInfoErrorTimedout.incrementAndGet();
                    else
                        totalInfoErrorOthers.incrementAndGet();
                } else {
                    log.info("[SERVERINFO:INFO] From {}:{} (Name = {}, Map = {}, Players = {}, Max Players = {})", sender.getAddress().getHostAddress(), sender.getPort(), serverInfo.getName(), serverInfo.getMapName(), serverInfo.getNumOfPlayers(), serverInfo.getMaxPlayers());
                    totalInfoRecSuccess.incrementAndGet();

                    //Retrieve Server Challenge only if there are players in the server
                    client.getServerChallenge(sender, REQUEST_PLAYER_HEADER, sourceChallengeCallback);
                }
                totalInfoReceived.incrementAndGet();
            };

            ResponseCallback<InetSocketAddress> sourceMasterCallback = (addr, sender, error) -> {
                if (error != null) {
                    log.error("[MASTER:ERROR] From {}:{} ({} : {})", sender.getAddress().getHostAddress(), sender.getPort(), error.getClass().getSimpleName(), error.getMessage());
                    totalMasterRecError.incrementAndGet();
                } else {
                    log.info("[MASTER:INFO] From {}:{} (IP Received: {}:{})", sender.getAddress().getHostAddress(), sender.getPort(), addr.getAddress().getHostAddress(), addr.getPort());
                    totalMasterRecSuccess.incrementAndGet();
                    totalInfoReqSent.incrementAndGet();

                    //Retrieve the server details after
                    log.debug("Sending Server Info Request {}", addr);
                    client.getServerDetails(addr, sourceInfoCallback);
                }
                totalMasterReceived.incrementAndGet();
            };

            //Retrieve servers from master
            client.getServersFromMaster(SourceMasterRequestPacket.REGION_ALL, masterFilter, sourceMasterCallback).sync();
            log.info("Waiting till all requests have been processed");

            //Iterate remaining requests that have not been processed
            client.waitForAll();

            cancelled = client.getCancelledTaskCount();

        } catch (Exception e) {
            log.error("Error occured in main", e);
        } finally {
            log.info("===================================================================");
            log.info("Report Summary");
            log.info("===================================================================");
            log.info("Total Master Records Received Successfully : {}", totalMasterRecSuccess.get());
            log.info("Total Master Records Received in Error: {}", totalMasterRecError.get());
            log.info("Total Master Records Received (Both): {}", totalMasterReceived.get());
            log.info("Total Server Info Requests Sent: {}", totalInfoReqSent.get());
            log.info("Total Server Info Records Received Successfully: {}", totalInfoRecSuccess.get());
            log.info("Total Server Info Records that Timed Out: {}", totalInfoErrorTimedout.get());
            log.info("Total Server Info Records that Received other Errors: {}", totalInfoErrorOthers.get());
            log.info("Total Server Info Records Received in Error: {}", totalInfoRecError.get());
            log.info("Total Server Info Replies Received (errors + success): {}", totalInfoReceived.get());
            log.info("Total Challenge Records Received: {}", totalChallengeRecieved.get());
            log.info("Total Challenge Records NOT Received: {}", totalChallengeNotReceived.get());
            log.info("Total Reported Requests Expired by Task Monitor: {}", cancelled);
            log.info("===================================================================");

            //log.info("Received a total of {} servers from the master, {} server details, {} server challenge numbers, {} cancelled tasks", ctr1.get(), ctr2.get(), ctr3.get(), cancelledTasks);
        }
    }
}
