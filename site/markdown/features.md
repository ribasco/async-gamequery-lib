Features
========

- Simple and easy to use API.
- All operations are asynchronous. Every request returns a [CompletableFuture](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)
- Capable of handling large volumes of transactions
- Efficient use of system resources
    - Uses off-heap [pooled direct buffers](https://netty.io/wiki/using-as-a-generic-library.html) (Reduces GC pressure)
    - Built-in thread and connection pooling support. Takes advantage of netty's [event loop](https://netty.io/4.1/api/io/netty/channel/EventLoop.html) model (each transaction is guaranteed to only run in one thread).
    - Makes use of native transports (if available) for increased performance (e.g. [epoll](https://man7.org/linux/man-pages/man7/epoll.7.html), [kqueue](https://developer.apple.com/library/archive/documentation/System/Conceptual/ManPages_iPhoneOS/man2/kqueue.2.html)). Java's NIO is used by default.
- Highly configurable. Clients can be easily configured to satisfy developer's requirements (e.g. providing a custom executor, adjusting rate limit parameters, selecting connection pool strategy etc)
- Queries are [Failsafe](https://failsafe.dev/) (excluding web api). Resilience [policies](https://failsafe.dev/policies/) have been implemented to guarantee the delivery and receipt of requests. Below are the policies available by default.
    - **Retry Policy:** A failed transaction is re-attempted until a response is either received or has reached the maximum number attempts defined by configuration.
    - **Rate Limiter Policy:** This prevents overloading the servers by sending requests too fast causing the requests to timeout due to rate limits being exceeded.

Web API Implementations
-----------------------

A list of supported web service implementations

| **Vendor** | **Game/Platform**           | **Supported Interfaces**                                                      |
|------------|-----------------------------|-------------------------------------------------------------------------------|
| Supercell  | Clash of Clans (Deprecated) | Clans, Leagues, Locations, Players                                            |
| Valve      | Steam                       | Apps, Community, Econ, Economy, Player Service, User, User Stats, Store Front |
| Valve      | Dota 2                      | Econ, Fantasy, Match, Stats, Stream, Teams                                    |
| Valve      | CS:GO                       | Servers                                                                       |

Game Server Queries
-------------------

A list of supported game server query protocols

| **Vendor** | **Description**       |
|------------|-----------------------|
| Valve      | Source Query Protocol |
| Valve      | Source RCON Protocol  |


Others
------

Other supported protocols

| **Vendor** | **Description**              |
|------------|------------------------------|
| Valve      | Source Log Listener          |
| Valve      | Master Server Query Protocol |       