Features
========

-   Everything is Asynchronous! Thanks to the powerful [Netty](http://netty.io/) library.
-   Java 8! Takes advantage of the new powerful language features. No longer
    will you have to deal with the painful `Future` class from the previous versions.
-   Designed to be capable of talking to a wide range of protocols.


Supported Games
---------------
-   Clash of Clans
-   All Source/Steam based games


Web API Implementations
-----------------------

A list of supported web service implementations

| **Vendor** | **Game/Platform** | **Supported Interfaces**                                         |
|------------|-------------------|------------------------------------------------------------------|
| Supercell  | Clash of Clans    | Clans, Leagues, Locations, Players                               |
| Valve      | Steam             | Apps, Community, Econ, Economy, Player Service, User, User Stats |
| Valve      | Dota 2            | Econ, Fantasy, Match, Stats, Stream, Teams                       |
| Valve      | CS:GO             | Servers                                                          |


Game Server Queries
-------------------

A list of supported game server protocol implementations

| **Vendor** | **Description**       |
|------------|-----------------------|
| Valve      | Source Query Protocol |
| Valve      | Source RCON Protocol  |


Others
------

Other supported protocols

| **Vendor** | **Description**              |
|------------|------------------------------|
| Valve      | Source Log Listener Service  |
| Valve      | Master Server Query Protocol |


Upcoming Implementations
------------------------

| **Game**          | **Type**             |
|-------------------|----------------------|
| League of Legends | Web API              |
| Minecraft         | Game Server Protocol |
