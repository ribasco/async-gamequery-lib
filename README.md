Asynchronous Game Query Library
===============================

[![Build Status](https://travis-ci.org/ribasco/async-gamequery-lib.svg?branch=master)](https://travis-ci.org/ribasco/async-gamequery-lib) [![Coverage Status](https://coveralls.io/repos/github/ribasco/async-gamequery-lib/badge.svg)](https://coveralls.io/github/ribasco/async-gamequery-lib)

As the name suggests, it's a game query library which provides a convenient way for java clients to execute asynchronous requests to game servers. It's built on top of [Netty](https://github.com/netty/netty) as it's core transport engine and use [AsyncHttpClient](https://github.com/AsyncHttpClient/async-http-client) for web services.

Project Resources
-------------

* Java Docs
* GitHub Site Page
* Continuous Integration

Implementations
----------------
 
Below are the list of what is currently implemented by the library.

* Valve Master Server Query Protocol
* Valve Source Query Protocol
* Valve Steam Web API
* Valve Dota 2 Web API
* Valve CS:GO Web API 
* Supercell Clash of Clans Web API
 
Installation
------------

Just add the following dependencies to your maven pom.xml. Only include the modules you need. 

**Valve Master Server Query Protocol**

```
<dependency>
    <groupId>org.ribasco.agql</groupId>
    <artifactId>agql-steam-master</artifactId>
    <version>LATEST_VERSION</version>
</dependency>
```

**Valve Source Query Protocol**

```
<dependency>
    <groupId>org.ribasco.agql</groupId>
    <artifactId>agql-source-query</artifactId>
    <version>LATEST_VERSION</version>
</dependency>
```

**Valve Steam Web API**

```
<dependency>
    <groupId>org.ribasco.agql</groupId>
    <artifactId>agql-steam-webapi</artifactId>
    <version>LATEST_VERSION</version>
</dependency>
```

**Valve Dota 2 Web API**

```
<dependency>
    <groupId>org.ribasco.agql</groupId>
    <artifactId>agql-dota2-webapi</artifactId>
    <version>LATEST_VERSION</version>
</dependency>
```

**Valve CS:GO Web API**

```
<dependency>
    <groupId>org.ribasco.agql</groupId>
    <artifactId>agql-csgo-webapi</artifactId>
    <version>LATEST_VERSION</version>
</dependency>
```

**Supercell Clash of Clans Web API**

```
<dependency>
    <groupId>org.ribasco.agql</groupId>
    <artifactId>agql-coc-webapi</artifactId>
    <version>LATEST_VERSION</version>
</dependency>
```

Usage
------------

For usage examples, please refer to the site docs.

Compatibility
-------------

Since this was initially built on top of Java 8. There are no plans on supporting previous versions. Time to move on :)

Future Integrations
--------------------

* Riot Games - League of Legends Web API
* Mojang - Minecraft server query  

Contributing
------------

Feel free to submit pull requests! Any type of contributions are welcome. 