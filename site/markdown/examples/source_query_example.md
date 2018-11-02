## Source Server Query

Demonstrates usage of Source Based Queries. 

### Server Info Query
Retrieve information asynchronously from a source compatible server. In this example, we pass a completion handler callback that will process the result once it receives a message from the server. There is no need to pass a challenge number as this query does not require it.
 
~~~
try (SourceQueryClient sourceQueryClient = new SourceQueryClient()) {
    InetSocketAddress serverAddress = new InetSocketAddress("192.168.1.13", 27015);
    sourceQueryClient.getServerInfo(serverAddress).whenComplete((sourceServer, serverInfoError) -> {
        //Check if we received an error
        if (serverInfoError != null) {
            log.debug("[SERVER : ERROR] : {}", serverInfoError.getMessage());
            return;
        }
        //Received a server info message successfully
        log.debug("[SERVER : INFO] : {}", sourceServer);
    });
} catch (Exception e) {
    log.error(e.getMessage(), e);
}
~~~

The blocking approach:

~~~
SourceServer serverInfo = sourceQueryClient.getServerInfo(serverAddress).get();
~~~

### Player Info Query

Retrieve a list of active players from a Source Server, you simply do the following: 

~~~
try (SourceQueryClient sourceQueryClient = new SourceQueryClient()) {
    InetSocketAddress serverAddress = new InetSocketAddress("192.168.1.14", 27015);
    CompletableFuture<List<SourcePlayer>> playerInfoFuture = sourceQueryClient.getPlayers(serverAddress);
    playerInfoFuture.whenComplete((players, playerError) -> {
        if (playerError != null) {
            log.debug("[PLAYERS : ERROR] Message: '{}')", playerError.getMessage());
            return;
        }
        log.debug("[PLAYERS : INFO] : PlayerData = {}", players);
    }).join(); //Wait for the operation to complete
} catch (Exception e) {
    log.error(e.getMessage(), e);
}
~~~

### Server Rules Query

Retrieve rules from a Source Server.

~~~
try (SourceQueryClient sourceQueryClient = new SourceQueryClient()) {
    InetSocketAddress serverAddress = new InetSocketAddress("192.168.1.14", 27015);
    CompletableFuture<Map<String, String>> rulesFuture = sourceQueryClient.getServerRules(serverAddress);
    rulesFuture.whenComplete((rules, rulesError) -> {
        if (rulesError != null) {
            log.debug("[RULES : ERROR] Message: '{}')", rulesError.getMessage());
            return;
        }
        rulesCtr.incrementAndGet();
        log.debug("[RULES : INFO] Rules = {}", rules);
    }).join(); //Wait for the operation to complete
} catch (Exception e) {
    log.error(e.getMessage(), e);
}
~~~

### Advanced Usage

When executing `getServerRules(address)` or `getPlayers(address)`, a challenge request is initially sent to obtain a valid 
challenge number from the server. This is a required step as stated in the official documentation (see [A2S_PLAYER](https://developer.valvesoftware.com/wiki/Server_queries#A2S_PLAYER) and [A2S_RULES](https://developer.valvesoftware.com/wiki/Server_queries#A2S_RULES)).

Keep in mind that a same challenge number is returned for every succeeding invocation of the request, but they do get renewed after a given time. 
So if you were to make periodic requests to a server, it only makes sense to store it's challenge number to an internal cache and reuse it, avoiding the need to make redundant challenge requests to the server. 

The library provides it's own default caching mechanism. To take advantage of this feature, use `getPlayersCached()` or `getServerRulesCached()` instead.
  
In this example, we create a PlayerMonitor class to periodically check a server for players. It simply outputs all active players within the server.  
  
~~~
public class PlayerMonitor
{
    private SourceQueryClient client;
    private ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
    
    public PlayerMonitor() {
        client = new SourceQueryClient();
        //Refresh the challenge number every 5 minutes
        client.setCacheRefreshInterval(Duration.ofMinutes(5));            
    }

    public void start() {
         //Check for players in a server every second
         service.scheduleAtFixedRate(() -> {
             try {
                 List<SourcePlayer> players = client.getPlayersCached(new InetSocketAddress("192.168.1.14", 27015)).get();
                 if (players.size() > 0) {
                     players.forEach(this::displayPlayerInfo);
                 }
             } catch (InterruptedException | ExecutionException e) {
                 log.error(e.getMessage(), e);
             }
         }, 1, 1, TimeUnit.SECONDS);
    }
    
    private void displayPlayerInfo(SourcePlayer player) {
        log.info("Player : {}", player.getName());
    }
    
    public void stop() {
        try {
            scheduledExecutorService.shutdown();
            scheduledExecutorService.awaitTermination(1, TimeUnit.SECONDS);
            client.close();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }
}
~~~

Testing the monitor app

~~~
public static void main(String[] args) throws Exception {
    PlayerMonitor monitor = new PlayerMonitor();
    monitor.start();
    //Wait for a minute
    Thread.sleep(60000);
    monitor.stop();
}
~~~

If for some reason you do not want to use the built-in caching mechanism. You could retrieve the challenge number yourself and pass that along to the request.

~~~
CompletableFuture<Integer> challengeFuture = sourceQueryClient.getServerChallenge(SourceChallengeType.PLAYER, serverAddress);

challengeFuture.whenComplete((challenge, serverChallengeError) -> {
    if (serverChallengeError != null) {
        log.debug("[CHALLENGE : ERROR] Message: '{}')", serverChallengeError.getMessage());
        return;
    }
    log.debug("[CHALLENGE : INFO] Challenge '{}'", challenge);

    CompletableFuture<List<SourcePlayer>> playersFuture = sourceQueryClient.getPlayers(challenge, serverAddress);
    playersFuture.whenComplete((players, playerError) -> {
        if (playerError != null) {
            log.debug("[PLAYERS : ERROR] Message: '{}')", playerError.getMessage());
            return;
        }
        log.debug("[PLAYERS : INFO] : PlayerData = {}", players);
    });

    CompletableFuture<Map<String, String>> rulesFuture = sourceQueryClient.getServerRules(challenge, serverAddress);
    rulesFuture.whenComplete((rules, rulesError) -> {
        if (rulesError != null) {
            log.debug("[RULES : ERROR] Message: '{}')", rulesError.getMessage());
            return;
        }
        log.debug("[RULES : INFO] Rules = {}", rules);
    });
}).join();
~~~
  
  


