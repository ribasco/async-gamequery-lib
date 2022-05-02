## Master Server Query

- This module implements the master server legacy protocol.
- The master servers are heavily rate-limited, so the rate-limiting features are enabled by default.
- If you encounter any issues with the legacy module, then I would recommend you try the undocumented web api version. See `GameServersService` interface in the web api module. Usage example also shown below.

1. [Failsafe Features](#failsafe-features)
2. [Examples](#examples)
   1. [Web API version](#web-api-version)
   2. [Legacy version](#legacy-version)

#### Failsafe Features

List of failsafe features implemented on this module

| Policy                 | Options Class   | Enabled by Default | Description                                                               |
|------------------------|-----------------|--------------------|---------------------------------------------------------------------------|
| Retry Policy           | FailsafeOptions | Yes                | A request is re-sent when a TimeoutException is thrown                    |
| Circuit Breaker Policy | FailsafeOptions | Yes                | Fail-fast when a certain number of failures reach the specified threshold |

#### Examples

##### Web API Version

- You can still use the `MasterServerFilter` class to help you in building server filters.

```java
class MasterWebApiExample {

   public static void main(String[] args) throws Exception {
      String authToken = "<steam api token>";
      try (SteamWebApiClient client = new SteamWebApiClient(authToken)) {
         GameServersService gameServersService = new GameServersService(client);
         MasterServerFilter filter = MasterServerFilter.create().appId(730).dedicated(true);
         CompletableFuture<List<Server>> serverListFuture = gameServersService.getServerList(filter.toString(), 10);
         int ctr = 1;
         for (Server server : serverListFuture.join()) {
            System.out.printf("%03d) name = %s, ip = %s%n", ctr++, server.getName(), server.getAddr());
         }
      }
   }
}
```

##### Legacy Version

- In this example, we retrieve addresses from all regions (`REGION_ALL`). We also pass a callback `TriConsumer<InetSocketAddress, InetSocketAddress, Throwable>` to display the addresses in real time.

~~~java
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;

class MasterServerQueryExample {

   public static void main(String[] args) throws Exception {
      try (MasterServerQueryClient client = new MasterServerQueryClient()) {
         MasterServerFilter filter = MasterServerFilter.create().dedicated(true).isEmpty(false);
         MasterServerResponse response = client.getServers(MasterServerType.SOURCE, MasterServerRegion.REGION_ALL, filter, this::displayIp).join();
         Set<InetSocketAddress> serverList = response.getServerList();
         System.out.printf("Got a total of %d addresses from the master server%n", serverList.size());
      }
   }

   public void displayIp(InetSocketAddress address, InetSocketAddress sender, Throwable error) {
      if (error != null) {
         error.printStackTrace(System.err);
         return;
      }
      System.out.printf("[%s] Server : %s%n", sender, address);
   }
}
~~~
