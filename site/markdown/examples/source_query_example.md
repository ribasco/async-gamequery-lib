## Source Server Query

Demonstrates usage of Source Based Queries. 

### Server Info Query
Retrieve information asynchronously from a source compatible server. In this example, we pass a completion handler callback that will process the result once it receives a message from the server. There is no need to pass a challenge number as this query does not require it.
 
~~~java
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

~~~java
SourceServer serverInfo = sourceQueryClient.getServerInfo(serverAddress).get();
~~~

### Player Info Query

Retrieve a list of active players from a Source Server, you simply do the following: 

~~~java
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

~~~java
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
  
  


