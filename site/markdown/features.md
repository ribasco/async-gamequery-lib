Features
========

- Implments Valve's RCON, Query and Steam Web API Protocols. Compatible with most steam based games.
- All operations are asynchronous (non-blocking i/o)
- Takes advantage of netty's event loop model and pooled off-heap buffers allowing for high-performance, high throughput concurrent transactions.
- Built-in connection and thread pooling facilities.
- Configurable clients. Provides a fluent API for defining user specific configuration options (e.g. providing custom executor, setting connection pooling strategy, setting rate limit parameters etc)

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