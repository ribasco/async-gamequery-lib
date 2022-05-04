Introduction
---------

1. [Features](#features)
2. [Implementations](#implementations)
    1. [Web API](#web-api)
    2. [Server Queries](#server-queries)
    3. [Others](#others)
3. [Best Practices](#best-practices)

### Features

- Simple and easy to use API.
- All operations are asynchronous. Every request returns a [CompletableFuture](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)
- It's fast and capable of handling large transactions.
- Resource efficient
    - Uses netty's off-heap [pooled direct buffers](https://netty.io/wiki/using-as-a-generic-library.html) (Helps reduce GC pressure for high volume/throughput transactions)
    - Built-in thread and connection pooling support. Takes advantage of netty's [event loop](https://netty.io/4.1/api/io/netty/channel/EventLoop.html) model.
    - Makes use of native transports (if available) for increased performance (e.g. [epoll](https://man7.org/linux/man-pages/man7/epoll.7.html), [kqueue](https://developer.apple.com/library/archive/documentation/System/Conceptual/ManPages_iPhoneOS/man2/kqueue.2.html)). Java's NIO is used by default.
- Highly Configurable. Clients can be configured to satisfy your requirements (e.g. providing a custom executor, adjusting rate limit parameters, selecting connection pool strategy etc)
- Transactions are [Failsafe](https://failsafe.dev/) (except web api). Resilience [policies](https://failsafe.dev/policies/) have been implemented to guarantee the delivery and receipt of queries. Below are the policies available by default.
    - **[Retry Policy](https://failsafe.dev/retry/):** A failed query is re-attempted until a response has either been received or the maximum number attempts has been reached.
    - **[Rate Limiter Policy](https://failsafe.dev/rate-limiter/):** This prevents overloading the servers by sending requests too fast causing the requests to timeout due to rate limits being exceeded.
    - **[Circuit Breaker Policy](https://failsafe.dev/circuit-breaker/):** When certain number of failures reach the threshold, the library will transition to an "OPEN" state, temporarily rejecting new requests.

### Implementations

##### Web API

A list of supported web service implementations

| **Vendor** | **Module**                  | **Supported Interfaces**                                                                                   |
|------------|-----------------------------|------------------------------------------------------------------------------------------------------------|
| Supercell  | Clash of Clans (Deprecated) | Clans, Leagues, Locations, Players                                                                         |
| Valve      | Steam                       | Apps, Community, Econ, Economy, Player Service, User, User Stats, Store Front, Game Servers, Store Service |
| Valve      | Dota 2                      | Econ, Fantasy, Match, Stats, Stream, Teams                                                                 |
| Valve      | CS:GO                       | Servers                                                                                                    |

##### Server Queries

A list of supported game server query protocols

| **Vendor** | **Module**     | **Description**                                                                                    |
|------------|----------------|----------------------------------------------------------------------------------------------------|
| Valve      | Source Query   | Implementations of the A2S_INFO, A2S_PLAYERS, A2S_RULES and A2S_SERVERQUERY_GETCHALLENGE protocols |
| Valve      | Source RCON    | TCP/IP-based communication protocol used by Source Dedicated Server                                |

##### Others

Other supported protocols

| **Vendor** | **Module Name**              | **Description**                                                        |
|------------|------------------------------|------------------------------------------------------------------------|
| Valve      | Source Log Listener          | A standalone service which listens to source server log events via UDP |
| Valve      | Master Server Query Protocol | Legacy protocol to query valve master servers                          |    

### Best Practices

Keep in mind that you should **NEVER BLOCK** the thread of your completion handlers. If you have to perform synchronization within your handlers/callbacks, then it is highly recommended that you execute them asynchronously, otherwise your application may
hang indefinitely.
Java's [CompletableFuture](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html) provides a convenient way of executing your handlers asynchronously. Refer to the working examples below.

<span style="color:red; text-transform: uppercase;">**Not Recommended**</span>

In this example, a command query `cvarlist` is sent to the server. The response is then parsed and broken down into a list of `ConVar` objects by the completion handler `parseOutput()`. Notice that in `parseOutput()`, an additional request `client.execute()` is sent for each entry followed by a call to `join()`, which blocks the current thread until a response is received. Avoid blocking the event-loop thread as this could cause the application to hang indefinitely.

```java
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class NotRecommededExample {

    private static final Pattern cvarlistParser = Pattern.compile("(?<name>\\S+)\\s*:\\s?(?<value>\\S+)?\\s*:(?<group>.*):(?<description>.*)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    private static class ConVar {

        private final String name;

        private final String value;

        private final String[] types;

        private final String description;

        private ConVar(String name, String value, String[] types, String description) {
            this.name = name;
            this.value = value;
            this.types = types;
            this.description = description;
        }

        private String getName() {
            return name;
        }

        private String getValue() {
            return value;
        }

        private String[] getTypes() {
            return types;
        }

        private String getDescription() {
            return description;
        }
    }

    public static void main(String[] args) throws Exception {
        new RconTest().run();
    }

    private void run() throws Exception {
        InetSocketAddress address = new InetSocketAddress("192.168.1.10", 27016);
        try (SourceRconClient client = new SourceRconClient()) {
            //authenticate
            SourceRconAuthResponse authResponse = client.authenticate(address, "<password_here>".getBytes()).join();
            if (!authResponse.isAuthenticated())
                throw new Exception(authResponse.getReason());

            //Example
            //1. execute 'cvarlist'
            //2. parse 'cvarlist' output with regex
            //3. transform each convar into ConVar class and add to list
            //4. return list
            List<ConVar> cvarList = client.execute(address, "cvarlist") //get cvarlist from server
                                          .thenApply(out -> parseOutput(client, out)) //parse the cvarlist output and transform it to a List of ConVar objects
                                          .join(); //block until the operation is complete

            System.out.printf("Processed a total of %d cvars%n", cvarList.size());
        }
    }

    private List<ConVar> parseOutput(SourceRconClient client, SourceRconCmdResponse response) {
        String cvarlistOutput = response.getResult();
        Matcher matcher = cvarlistParser.matcher(cvarlistOutput);
        List<ConVar> conVarList = new ArrayList<>();

        while (matcher.find()) {
            String name = matcher.group("name");
            String value = matcher.group("value");
            String[] types = StringUtils.splitPreserveAllTokens(matcher.group("group"), ",");
            String description = matcher.group("description");
            ConVar conVar = new ConVar(name, value, types, description);
            conVarList.add(conVar);
            Console.colorize(true).green("[%s]", Thread.currentThread().getName()).white(": %s = %s", conVar.getName(), conVar.getValue()).println();

            //Obtain additional information about the cvar via calling 'help <cvar name>' (Calling join() will block the current event loop thread) 
            SourceRconCmdResponse helpResponse = client.execute(response.getAddress(), String.format("help %s", name.trim())).join();

            //parse cvarHelpRes and update ConVar
            String helpOutput = helpResponse.getResult();
            //parse helpOutput here....
        }
        return conVarList;
    }
}
```

<span style="color:green; text-transform: uppercase;">**Recommended**</span>

**Solution 1:** The easiest solution would be to swap `thenApply` with `thenApplyAsync`. This will ensure that `parseOutput` will be executed on a different thread and most importantly will not block the current event-loop thread. You also can specify a [custom executor](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html#thenApplyAsync-java.util.function.Function-) if needed.

```java
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class RecommendedExampleOne {

    private static final Pattern cvarlistParser = Pattern.compile("(?<name>\\S+)\\s*:\\s?(?<value>\\S+)?\\s*:(?<group>.*):(?<description>.*)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    private static class ConVar {

        private final String name;

        private final String value;

        private final String[] types;

        private final String description;

        private ConVar(String name, String value, String[] types, String description) {
            this.name = name;
            this.value = value;
            this.types = types;
            this.description = description;
        }

        private String getName() {
            return name;
        }

        private String getValue() {
            return value;
        }

        private String[] getTypes() {
            return types;
        }

        private String getDescription() {
            return description;
        }
    }

    public static void main(String[] args) throws Exception {
        new RconTest().run();
    }

    private void run() throws Exception {
        InetSocketAddress address = new InetSocketAddress("192.168.1.10", 27016);
        try (SourceRconClient client = new SourceRconClient()) {
            //authenticate
            SourceRconAuthResponse authResponse = client.authenticate(address, "<password_here>".getBytes()).join();
            if (!authResponse.isAuthenticated())
                throw new Exception(authResponse.getReason());

            //Example
            //1. execute 'cvarlist'
            //2. parse 'cvarlist' output with regex
            //3. transform each convar into ConVar class and add to list
            //4. return list
            List<ConVar> cvarList = client.execute(address, "cvarlist")
                                          .thenApplyAsync(out -> parseOutput(client, out)) //THIS IS A LONG RUNNING TASK. Call thenApplyAsync() so it wont block the event-loop.
                                          .join(); //block until the operation is complete

            System.out.printf("Processed a total of %d cvars%n", cvarList.size());
        }
    }

    private List<ConVar> parseOutput(SourceRconClient client, SourceRconCmdResponse response) {
        String cvarlistOutput = response.getResult();
        Matcher matcher = cvarlistParser.matcher(cvarlistOutput);
        List<ConVar> conVarList = new ArrayList<>();

        while (matcher.find()) {
            String name = matcher.group("name");
            String value = matcher.group("value");
            String[] types = StringUtils.splitPreserveAllTokens(matcher.group("group"), ",");
            String description = matcher.group("description");
            ConVar conVar = new ConVar(name, value, types, description);
            conVarList.add(conVar);
            Console.colorize(true).green("[%s]", Thread.currentThread().getName()).white(": %s = %s", conVar.getName(), conVar.getValue()).println();

            //Obtain additional information about the cvar via calling 'help <cvar name>' (Calling join() will block the current event loop thread) 
            SourceRconCmdResponse helpResponse = client.execute(response.getAddress(), String.format("help %s", name.trim())).join();

            //parse cvarHelpRes and update ConVar
            String helpOutput = helpResponse.getResult();
            //parse helpOutput here....
        }
        return conVarList;
    }
}
```

The problem with this approach is that its not efficient. A closer look at the `parseOutput` implementation, shows that it is calling `client.execute(...).join()` synchronously. The call to `join()` blocks the current thread until a response is received. A slightly better approach would be to execute all these additional requests asynchronously then use a synchronization barrier (e.g. `CountDownLatch` or `Phaser`) to block until all requests have been fulfilled. In this example, we will
use `Phaser` since we do not yet know how many cvars we are going to process.

**Solution 2:** Update `parseOutput` implementation so that we requests for `help <cvar>` are all executed asynchronously then wait until all requests have been fulfilled.

```java
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class RecommendedExampleTwo {

    private static final Pattern cvarlistParser = Pattern.compile("(?<name>\\S+)\\s*:\\s?(?<value>\\S+)?\\s*:(?<group>.*):(?<description>.*)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    private static class ConVar {

        private final String name;

        private final String value;

        private final String[] types;

        private final String description;

        private ConVar(String name, String value, String[] types, String description) {
            this.name = name;
            this.value = value;
            this.types = types;
            this.description = description;
        }

        private String getName() {
            return name;
        }

        private String getValue() {
            return value;
        }

        private String[] getTypes() {
            return types;
        }

        private String getDescription() {
            return description;
        }
    }

    public static void main(String[] args) throws Exception {
        new RconTest().run();
    }

    private void run() throws Exception {
        InetSocketAddress address = new InetSocketAddress("192.168.50.6", 27016);
        try (SourceRconClient client = new SourceRconClient()) {
            //authenticate
            SourceRconAuthResponse authResponse = client.authenticate(address, "AzCgBF6E7ddY6wE5XFrfdv3Rsw9XcQoY".getBytes()).join();//<password_here>
            if (!authResponse.isAuthenticated())
                throw new Exception(authResponse.getReason());

            //Example
            //1. execute 'cvarlist'
            //2. parse 'cvarlist' output with regex
            //3. transform each convar into ConVar class and add to list
            //4. return list
            List<ConVar> cvarList = client.execute(address, "cvarlist").thenApplyAsync(out -> parseOutput(client, out)).join();
            System.out.printf("Processed a total of %d cvars%n", cvarList.size());
            System.out.println("Exiting application");
        }
    }

    //Here is an updated implementation using Phaser sync barrier
    private List<ConVar> parseOutput(SourceRconClient client, SourceRconCmdResponse response) {
        String cvarlistOutput = response.getResult();
        Matcher matcher = cvarlistParser.matcher(cvarlistOutput);
        List<ConVar> conVarList = new ArrayList<>();
        Phaser phaser = new Phaser();
        phaser.register();
        while (matcher.find()) {
            String name = matcher.group("name");
            String value = matcher.group("value");
            String[] types = StringUtils.splitPreserveAllTokens(matcher.group("group"), ",");
            String description = matcher.group("description");
            ConVar conVar = new ConVar(name, value, types, description);
            conVarList.add(conVar);
            phaser.register();
            //Obtain cvar information via 'help'
            client.execute(response.getAddress(), String.format("help %s", name.trim())).thenCombine(CompletableFuture.completedFuture(conVar), (res, cvar) -> {
                String helpOutput = res.getResult();
                //TODO: parse helpOutput and update cvar
                return cvar;
            }).whenComplete((conVar1, throwable) -> phaser.arriveAndDeregister());
        }
        phaser.arriveAndAwaitAdvance();
        return conVarList;
    }
}
```