Changelog
-------------
1.0.5 - Bug fixes/enhancements

- Fixed #87: Fixed invalid packet type exception thrown when executing commands on a Rust server

1.0.4 - Bug fixes/enhancements

- Enhancement #80: Use daemon threads for the default executor

1.0.3 - Bug fixes/enhancements

- Enhancement #64: New `sourceTvProxy` flag in `SourceServer`
- Defect #63: Map network protocol version in Source `A2S_INFO` decoder
- Defect #62: Add game server port to `A2S_INFO` response

1.0.2 - Bug fixes/enhancements

- Fixed #59: Close previous connection on retry
- Fixed #60: Added missing overloaded constructor for Web API clients.
- Fixed #61: Add new Http Option for web api key

1.0.1 - Bug fixes/enhancements

- Fixed #58: Use a single EventLoopGroup instance for clients that share the same ExecutorService
- Fixed RejectedExecutionException that is thrown after an attempt to initialize and instantiate a new client more than once in the same process
- Downgraded some error log statements to debug

1.0.0 - A complete re-work from the ground up with backwards incompatible changes.

- **General Updates**
    - Completely re-worked on the core implementation from the ground up for improved performance, reliability and data integrity.
        - Removed custom built request/response state management facilities from the previous version. Now fully taking advantage of netty's channel attributes for application state.
        - Ensure each transaction is run within a single thread obtained from the Event Loop (provided by netty)
        - Improved memory utilization
    - In source rcon/master and query modules, all response types are now wrapped in an `AbstractResponse` type. This allows developers to retrieve additional information about the response (e.g. sender address, originating request etc)
    - Removed custom request/response state management facilities in the core module. Now using netty's channel attributes for managing each channel/connection state.
    - Re-licensed project to Apache-2.0
    - Added aggregate project artifact: `agql-<version>.jar`
    - Added configuration support. Clients can now be tweaked depending on the requirements of the developer (e.g. providing custom executor service, set max active connections, modify read/write timeout settings etc)
    - Added Failsafe support for query modules. Implemented fallback, retry, rate limiter policies for the source/master query modules (Not yet available for steam web api)
    - **Deprecated** some client constructors in the query modules. Use the new configuration feature of the client.
    - Now using off-heap pooled direct buffers by default for the majority of the queries.
    - Cleaned up and standardized log statements for better readability.
    - A default global `EventLoopGroup` (a special netty executor service) is now shared with all clients by default. A custom executor/EventLoopGroup can still be provided via configuration. (See GeneralOptions.THREAD_EL_GROUP)
    - Updated interactive examples

- **Source RCON**
    - New artifact for this module `agql-source-rcon`
    - Removed constructor SourceRconClient(boolean useTerminatorPackets)
    - Updated authenticate signature to 'authenticate(InetSocketAddress address, byte[] password)'
    - Failsafe Integration
        - Retry policy
            - Failed requests will be retried three times before it is completed exceptionally. This is mostly convenient for cases where a request fails due to the active connection being dropped by the remote server (password invalidated/change, changelevel was issued etc).
        - Circuit Breaker (fail-fast)
    - Improved support for connection pooling
        - Connection Pooling is now enabled by default
        - Only one thread per connection is maintained for each address/pool (this can be changed via configuration. see GeneralOptions.POOL_MAX_CONNECTIONS)
    - Built-in Authentication management
        - An authentication manager is now provided and enabled by default to manage all active connections, ensuring that each connection remains in a valid state (connection is active and authenticated by the remote server).
    - Added RCON console in example module

- **Source Master Query**
    - Improved/re-worked implementation
    - Failsafe Integration
        - Fallback Policy
            - If a connection is dropped by the master server (e.g. timeout or rate limit exceeded), the resulting future will still be marked as completed returning the list of addresses collected from the master server.
        - Retry Policy
            - If a timeout is encountered during a query (there are usually 3 ip available for each master server), the request will automatically be retried cycling through all available master server IPs.
        - Rate Limiting Policy
            - Requests sent to the master servers are rate limited by default to prevent timeouts or dropped connections.

- **Source Query (Info/Players/Rules)**
    - Failsafe Integration
        - Retry Policy
            - Failed requests will be re-attempted 3 times by default.
        - Rate Limiting Policy
    - All futures now return a type of `SourceQueryResponse`. Result can be retrieved via `SourceQueryResponse.getResult()`
    - Removed caching facilities. This was a bad idea from the start.
    - Renamed methods
        - From `getServerChallenge` to `getChallenge`
        - From `getServerInfo` to `getInfo`
        - From `getServerRules` to `getRules`
    - Source info query now compatible with the new implementation (challenge based) (See [RFC: Changes to the A2S_INFO protocol ](https://steamcommunity.com/discussions/forum/14/2989789048633291344/))
    - Queries that require a challenge number are now handled automatically by the library by default, this means that the developer no longer needs to obtain a challenge number manually.
    - **Deprecated** built-in challenge caching facilities. This will be removed in the next major update.

- **Source Log Service**
    - New artifact for this module `agql-source-log`
    - Fixed: Callback set via setter method is not applied (Issue #44)
    - Introduced new constructor arguments that accepts a custom ExecutorService
    - `listen()` now returns a CompletableFuture which is notified once the underlying connection of the service has been closd.

- **Clash of Clans (Web API)**
    - Marked as deprecated and will be removed in the next major version. This module will be removed in the next major version.

- **Steam Web API**
    - Added `GameServersService`. Allows query the master server via an web api call. 
