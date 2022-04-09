Changelog
-------------

1.0.0 - A complete re-work of the core and source query/master modules.

- **General Updates**
    - Completely re-worked on the core implementation from the ground up for improved performance, reliability and data integrity.
        - Removed custom built request/response state management facilities from the previous version. Now fully taking advantage of netty's channel attributes for application state.
        - Ensure each transaction is run within a single thread obtained from the Event Loop (provided by netty)
        - Improved memory utilization
    - Removed custom request/response state management facilities in the core module. Now using netty's channel attributes for managing each channel/connection state.
    - Re-licensed project to Apache-2.0
    - Added aggregate project artifact: `agql-<version>.jar`
    - Added configuration support. Clients can now be tweaked depending on the requirements of the developer (e.g. providing custom executor service, set max active connections, modify read/write timeout settings etc)
    - Added Failsafe support for query modules. Implemented fallback, retry, rate limiter policies for the source/master query modules (Not yet available for steam web api)
    - **Deprecated** some client constructors in the query modules. Use the new configuration feature of the client.
    - Now using off-heap pooled direct buffers by default for the majority of the queries.
    - Cleaned up and standardized log statements for better readability.
    - A default global `EventLoopGroup` (a special netty executor service) is now shared with all clients by default. A custom executor/EventLoopGroup can still be provided via configuration. (See TransportOptions.THREAD_EL_GROUP)
    - Updated interactive examples

- **Source RCON**
    - Removed constructor SourceRconClient(boolean useTerminatorPackets)
    - Updated authenticate signature to 'authenticate(InetSocketAddress address, byte[] password)'
    - 
    - Failsafe Integration
        - Retry policy
            - Failed requests will be retried three times before it is completed exceptionally. This is mostly convenient for cases where a request fails due to the active connection being dropped by the remote server (password invalidated/change, changelevel was issued etc).
    - Improved support for connection pooling
        - Connection Pooling is now enabled by default
        - Only one thread per connection is maintained for each address/pool (this can be changed via configuration. see TransportOptions.POOL_MAX_CONNECTIONS)
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
    - Source info query now compatible with the new implementation (challenge based) (See [RFC: Changes to the A2S_INFO protocol ](https://steamcommunity.com/discussions/forum/14/2989789048633291344/))
    - Queries that require a challenge number are now handled automatically by the library by default, this means that the developer no longer needs to obtain a challenge number manually.
    - **Deprecated** built-in challenge caching facilities. This will be removed in the next major update.

- **Source Log Service**
    - Fixed: Callback set via setter method is not applied (Issue #44)
    - Introduced new constructor arguments that accepts a custom ExecutorService
    - `listen()` now returns a CompletableFuture which is notified once the underlying connection of the service has been closd.

- **Clash of Clans (Web API)**
    - Marked as deprecated and will be removed in the next major version. This module will be removed in the next major version.