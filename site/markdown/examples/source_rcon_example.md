## Source RCON

1. [Failsafe Features](#failsafe-features)
2. [Examples](#examples)
    1. [Authentication](#authentication)
    2. [Commands](#commands)
    3. [Invalidating credentials](#invalidating-credentials)
    4. [Cleanup Connections](#cleaning-up-active-connections)
    5. [Display connection statistics](#display-connection-statistics)
    6. [Providing your own custom CredentialsStore implementation](#providing-your-own-custom-credentialsstore-implementation)
    7. [Compatibility with non-source games](#compatibility-with-non-source-games)

#### About this module

- The rcon module maintains a pool of re-usable connections by default.
- Connections are made on-demand. The maximum number of connections can be set via configuration.
- Connections are guaranteed to be always authenticated, assuming that the registered credentials remain valid.
- A connection that has not been used after a certain period of time will be considered as inactive. Inactive connections are dropped automatically by the library. You can change this behavior via configuration.
- You only need to authenticate an address once via `authenticate(InetSocketAddress, byte[])`. The library will automatically authenticate new connections if needed.
- Credentials are stored in the `CredentialsStore` (Default: `InMemoryCredentialsStore`). You can provide your own implementation via configuration.

#### Failsafe Features

List of failsafe features implemented on this module

| Policy                 | Options Class   | Enabled by Default | Description                                                               |
|------------------------|-----------------|--------------------|---------------------------------------------------------------------------|
| Retry Policy           | FailsafeOptions | Yes                | A request is re-sent when a TimeoutException is thrown                    |
| Circuit Breaker Policy | FailsafeOptions | Yes                | Fail-fast when a certain number of failures reach the specified threshold |

#### Examples

<span style="color:red; text-transform: uppercase;">**REMEMBER**</span> Avoid long running tasks within your completion handlers that will block the event-loop thread, doing so will potentially cause the application to hang indefinitely. Read more about coding best practices [here](/introduction.html#best-practices).

- `SourceRconAuthResponse` is the response returned for authentication requests. Receiving this response object indicates that the server has responded normally. Use the `isAuthenticated()` getter method to check if the authentication was successful. If it returns `false`, then a reason code/message will be included in the response object.
- A `SourceRconCmdResponse` is the response returned for command requests. Use the `getResult()` method to retrieve the result.
- There are cases when the authentication request fails and the resulting future completes in error. If this happens, check the type of exception to determine the cause of the authentication failure. Below is the list of possible exceptions that are thrown in the event of an authentication failure.

| Type                            | Description                                                                                                                    |
|---------------------------------|--------------------------------------------------------------------------------------------------------------------------------|
| RconMaxLoginAttemptsException   | Thrown when the server has dropped the connection without responding and the maximum number of login attempts has been reached |
| RconInvalidCredentialsException | Thrown if the credentials is no longer valid. Reauthentication required.                                                       |
| RconNotYetAuthException         | Thrown when a command request was attempted but the address has not yet been authenticated by the server                       |

##### Authentication

Basic example of authenticating with the source server

~~~java
class RconAuthExample {

    public static void main(String[] args) throws Exception {
        try (SourceRconClient client = new SourceRconClient()) {
            InetSocketAddress address = new InetSocketAddress("192.168.1.10", 27015);
            SourceRconAuthResponse response = client.authenticate(address, password.getBytes()).join();
            //Check authenticated flag
            if (!response.isAuthenticated()) {
                System.err.printf("Failed to authenticate with server (Reason: %s, Code: %s)%n", response.getReason(), response.getReasonCode().name());
                return;
            }
            //do something here...
            System.out.println("Successfully authenticated with server");
        } catch (RconAuthException ex) {
            System.err.printf("Failed to authenticate with server (Reason: %s)%n", ex.getReason());
            ex.printStackTrace(System.err);
        }
    }
}
~~~

##### Commands

Basic example of sending a command to a source server.

~~~java
import java.net.InetSocketAddress;

class RconCmdExample {

    public static void main(String[] args) throws Exception {
        try (SourceRconClient client = new SourceRconClient()) {
            InetSocketAddress address = new InetSocketAddress("192.168.1.10", 27015);
            //1. authenticate....
            //....
            //2. send command
            SourceRconCmdResponse response = client.execute(address, "status").join();
            System.out.printf("Got response from server '%s': %s%n", response.getAddress(), response.getResult());
        } catch (RconException ex) {
            System.err.printf("Failed to authenticate with server (Reason: %s)%n", ex.getReason());
            ex.printStackTrace(System.err);
        }
    }
}
~~~

##### Invalidating Credentials

```java
import java.net.InetSocketAddress;

class RconCmdExample {

    public static void main(String[] args) throws Exception {
        try (SourceRconClient client = new SourceRconClient()) {
            InetSocketAddress address = new InetSocketAddress("192.168.1.10", 27015);
            //1. authenticate....
            //....
            //2. send command
            SourceRconCmdResponse response = client.execute(address, "status").join();
            System.out.printf("Got response from server '%s': %s%n", response.getAddress(), response.getResult());

            //invalidate only connections
            client.invalidate();

            //invalidate everything + credentials (this will require re-authentication)
            client.invalidate(false);
        } catch (RconException ex) {
            System.err.printf("Failed to authenticate with server (Reason: %s)%n", ex.getReason());
            ex.printStackTrace(System.err);
        }
    }
}
```

##### Cleaning up active connections

- Use `cleanup()` to only close inactive/unused connections (this is the same as `cleanup(false)`.
- Use `cleanup(true)` to force close all connections.

```java
import java.net.InetSocketAddress;

class RconCmdExample {

    public static void main(String[] args) throws Exception {
        try (SourceRconClient client = new SourceRconClient()) {
            InetSocketAddress address = new InetSocketAddress("192.168.1.10", 27015);
            //1. authenticate....
            //....
            //2. send command
            SourceRconCmdResponse response = client.execute(address, "status").join();
            System.out.printf("Got response from server '%s': %s%n", response.getAddress(), response.getResult());

            //Cleanup only inactive connections
            client.cleanup();

            //force close all active connections
            client.cleanup(true);

            //calling execute will trigger a new connection and authentication request
            SourceRconCmdResponse res = client.execute(address, "status").join();
            System.out.printf("Response: %s%n", res.getResult());
        } catch (RconException ex) {
            System.err.printf("Failed to authenticate with server (Reason: %s)%n", ex.getReason());
            ex.printStackTrace(System.err);
        }
    }
}
```

##### Display connection statistics

- `getStatistics()` generates metadata information on the active connections of the session.

```java
class ConnectionStatistics {

    public static void main(String[] args) throws Exception {
        try (SourceRconClient rconClient = new SourceRconClient()) {
            SetMultimap<InetSocketAddress, SourceRconMessenger.ConnectionStats> stats = rconClient.getStatistics();
            final Supplier<Console.Colorize> console = () -> Console.colorize(true);
            int ctr = 1;
            for (InetSocketAddress address : stats.keySet()) {
                console.get().blue().text("-----------------------------------------------------------------------------------------------------------------------------------------------------------").println();
                console.get().blue().text("%02d)", ctr++).white().text(" Address ").reset().yellow().text("'%s'", address).println();
                console.get().blue().text("-----------------------------------------------------------------------------------------------------------------------------------------------------------").println();
                int connCtr = 1;
                for (SourceRconMessenger.ConnectionStats connStats : stats.get(address)) {
                    Console.Colorize con = console.get();
                    con
                            .purple("\t(%02d)", connCtr++).reset()
                            .cyan("\t[Connection Id]: ").white(connStats.getConnectionId()).reset()
                            .cyan("\t[Local Address]: ").white(connStats.getLocalAddress().toString()).reset()
                            .cyan("\t[Acquire Count]: ").white("%03d", connStats.getAcquireCount()).reset()
                            .cyan("\t[Last Acquired]: ").white("%s sec(s) ago", Time.getTimeDesc(connStats.getLastAcquiredMs(), true)).reset();

                    if (connStats.isAcquired()) {
                        con.cyan().text("\t[Is Acquired]: ").reset().yellow().text("%s", connStats.isAcquired()).reset();
                    } else {
                        con.cyan().text("\t[Is Acquired]: ").reset().green().text("%s", connStats.isAcquired()).reset();
                    }

                    if (connStats.isAuthenticated()) {
                        con.cyan("\t[Is Authenticated]: ").reset().green("%s", connStats.isAuthenticated()).reset();
                    } else {
                        con.cyan("\t[Is Authenticated]: ").reset().red("%s", connStats.isAuthenticated()).reset();
                    }

                    con.cyan("\t[Thread Name]: ").reset()
                       .white("%s", connStats.getThreadName()).reset()
                       .println();
                }
            }
        }
    }
}
```

##### Providing your own custom CredentialsStore implementation

- Simply implement the `CredentialsStore` interface then pass it to the `SourceRconOptions.CREDENTIALS_STORE` option.

```java
class CustomCredentialsStoree {

    private final CredentialsStore credentialsStore = new CredentialsStore() {
        private final Map<InetSocketAddress, Credentials> credentials = new ConcurrentHashMap<>();

        @Override
        public Credentials get(InetSocketAddress address) {
            return credentials.get(address);
        }

        @Override
        public Credentials add(InetSocketAddress address, byte[] passphrase) {
            if (passphrase == null || passphrase.length == 0)
                throw new IllegalArgumentException("Passphrase cannot be null or empty");
            final Credentials newCredentials = new SourceRconCredentials(passphrase);
            final Credentials oldCredentials = credentials.put(address, newCredentials);
            if (oldCredentials != null) {
                oldCredentials.invalidate();
            }
            return newCredentials;
        }

        @Override
        public void remove(InetSocketAddress address) {
            credentials.remove(address);
        }

        @Override
        public void clear() {
            int size = credentials.size();
            credentials.clear();
        }

        @Override
        public boolean exists(InetSocketAddress address) {
            return credentials.containsKey(address);
        }
    };

    public static void main(String[] args) throws Exception {
        final SourceRconOptions options = SourceRconOptions.builder()
                                                           .option(SourceRconOptions.CREDENTIALS_STORE, credentialsStore)
                                                           .build();
        //Test authentication
        try (SourceRconClient rconClient = new SourceRconClient(options)) {
            InetSocketAddress address = new InetSocketAddress("192.168.1.10", 27015);
            SourceRconAuthResponse response = rconClient.authenticate(address, password.getBytes()).join();
            //Check authenticated flag
            if (!response.isAuthenticated()) {
                System.err.printf("Failed to authenticate with server (Reason: %s, Code: %s)%n", response.getReason(), response.getReasonCode().name());
                return;
            }
            System.out.printf("Successfully authenticated with server: %s%n", address);
        }
    }
}
```

##### Compatibility with non-source games

In case of issues with the rcon module on non-source based games (e.g. Minecraft), you can try and set `SourceRconOptions.USE_TERMINATOR_PACKET` option to `false`.

Games such as <a href="https://wiki.vg/RCON">Minecraft</a> does not support "terminator packets" as it does not echo back the <a href="https://developer.valvesoftware.com/wiki/Source_RCON_Protocol#Multiple-packet_Responses">response packets</a> receives. So if the game server does not support this, then this configuration option should be <strong>disabled</strong>. A special heuristics will be used instead to determine the end of the response.

```java
class NonSourceBasedGames {

    public static void main(String[] args) throws Exception {
        //in case 
        final SourceRconOptions options = SourceRconOptions.builder()
                                                           .option(SourceRconOptions.USE_TERMINATOR_PACKET, false)
                                                           .build();
        //Test authentication
        try (SourceRconClient rconClient = new SourceRconClient(options)) {
            InetSocketAddress address = new InetSocketAddress("192.168.1.10", 27015);
            SourceRconAuthResponse response = rconClient.authenticate(address, password.getBytes()).join();
            //Check authenticated flag
            if (!response.isAuthenticated()) {
                System.err.printf("Failed to authenticate with server (Reason: %s, Code: %s)%n", response.getReason(), response.getReasonCode().name());
                return;
            }
            System.out.printf("Successfully authenticated with server: %s%n", address);

            SourceRconCmdResponse res = rconClient.execute(address, "echo Hi").join();
            System.out.printf("Response: %s%n", res.getResult());
        }
    }

}
```