Asynchronous Game Query Library
===============================

> **NOTE:** This project is still a work in-progress so there is no stable release available at the moment. Most of the features are already fully functional. If you want to try it out you can download the latest builds from the snapshots repository (See Project Resources section)

[![Build Status](https://travis-ci.org/ribasco/async-gamequery-lib.svg?branch=master)](https://travis-ci.org/ribasco/async-gamequery-lib) [![Dependency Status](https://www.versioneye.com/user/projects/5837c911e7cea00029198c9d/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/5837c911e7cea00029198c9d)

As the name suggests, it's a game query library which provides a convenient way for java programs to execute asynchronous requests to game servers/services. It's built on top of [Netty](https://github.com/netty/netty) as it's core transport engine and use [AsyncHttpClient](https://github.com/AsyncHttpClient/async-http-client) for web services.

![alt text](site/resources/images/agql-project-banner-big.png "Games supported by Source Query Protocol")

Project Resources
-------------

* [Java API Docs](https://ribasco.github.io/async-gamequery-lib/apidocs)
* [Project Documentation](https://ribasco.github.io/async-gamequery-lib/)
* [Continuous Integration](https://travis-ci.org/ribasco/async-gamequery-lib)
* [Nightly Builds (Snapshots)](https://oss.sonatype.org/content/repositories/snapshots/com/ibasco/agql/)

Implementations
----------------
 
Below is the list of what is currently implemented on the library

* Valve Master Server Query Protocol
* Valve Source Query Protocol
* Valve Steam Web API
* Valve Steam StoreFront Web API
* Valve Dota 2 Web API
* Valve CS:GO Web API 
* Valve Source Log Handler (a log monitor service)
* Supercell Clash of Clans Web API

Requirements
------------

* Java JDK 8
* Apache Commons Lang 3
* Netty 4.1.x
* AsyncHttpClient 2.1.x
* SLF4J 1.7.x
* Google Gson 2.8.x
* Google Guava 20.x
 
Installation
------------

Just add the following dependencies to your maven pom.xml. Only include the modules you need.

### Install from Maven Central

**Valve Master Server Query Protocol**

```
<dependency>
    <groupId>com.ibasco.agql</groupId>
    <artifactId>agql-steam-master</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```

**Valve Source Query Protocol**

```
<dependency>
    <groupId>com.ibasco.agql</groupId>
    <artifactId>agql-source-query</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```

**Valve Steam Web API**

```
<dependency>
    <groupId>com.ibasco.agql</groupId>
    <artifactId>agql-steam-webapi</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```

**Valve Dota 2 Web API**

```
<dependency>
    <groupId>com.ibasco.agql</groupId>
    <artifactId>agql-dota2-webapi</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```

**Valve CS:GO Web API**

```
<dependency>
    <groupId>com.ibasco.agql</groupId>
    <artifactId>agql-csgo-webapi</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```

**Supercell Clash of Clans Web API**

```
<dependency>
    <groupId>com.ibasco.agql</groupId>
    <artifactId>agql-coc-webapi</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```

### Install from Source

Clone from remote repository then `mvn install`. All of the modules will be installed to your local maven repository.

~~~
git clone https://github.com/ribasco/async-gamequery-lib.git
cd async-gamequery-lib
mvn install
~~~

Usage
------------

For usage examples, please refer to the [site docs](http://ribasco.github.io/async-gamequery-lib/).

Compatibility
-------------

Since this was initially built on top of Java 8. There are no plans on supporting the previous versions. Time to move on :)

Protocol Specifications
-----------------------

References you might find helpful regarding the implementations

* [Valve Source RCON Protocol](https://developer.valvesoftware.com/wiki/Source_RCON_Protocol)
* [Valve Master Server Query Protocol](https://developer.valvesoftware.com/wiki/Master_Server_Query_Protocol)
* [Valve Source Query Protocol](https://developer.valvesoftware.com/wiki/Server_queries)
* [Valve TF2 Web API Wiki](https://wiki.teamfortress.com/wiki/WebAPI)
* [Valve Steam Web API](https://developer.valvesoftware.com/wiki/Steam_Web_API)
* [Valve Steam Storefront API](https://wiki.teamfortress.com/wiki/User:RJackson/StorefrontAPI)
* [Clash of Clans Web API](https://developer.clashofclans.com/#/documentation)
* [xPaw Steam Web API Documentation](https://lab.xpaw.me/steam_api_documentation.html)


Future Integrations
--------------------

My planned integrations for future releases. Don't hesitate to [contact](mailto:raffy@ibasco.com) me if you have any other suggestions.

* Riot Games - League of Legends Web API
* Mojang - Minecraft server query

Contributing
------------

Fork it and submit a pull request. Any type of contributions are welcome.