Asynchronous Game Query Library
===============================

> **NOTE:** This project is still a work in-progress and no stable releases are available at the moment. Most of the features are fully functional and available. If you want to try it out you can download the latest builds from the snapshots repository (See Project Resources section)

[![Build Status](https://travis-ci.org/ribasco/async-gamequery-lib.svg?branch=master)](https://travis-ci.org/ribasco/async-gamequery-lib) [![Coverage Status](https://coveralls.io/repos/github/ribasco/async-gamequery-lib/badge.svg)](https://coveralls.io/github/ribasco/async-gamequery-lib) [![Dependency Status](https://www.versioneye.com/user/projects/5837c911e7cea00029198c9d/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/5837c911e7cea00029198c9d)

As the name suggests, it's a game query library which provides a convenient way for java clients to execute asynchronous requests to game servers/services. It's built on top of [Netty](https://github.com/netty/netty) as it's core transport engine and use [AsyncHttpClient](https://github.com/AsyncHttpClient/async-http-client) for web services.

Project Resources
-------------

* [Java Docs](https://ribasco.github.io/async-gamequery-lib/apidocs)
* [GitHub Site Page](https://ribasco.github.io/async-gamequery-lib/)
* [Continuous Integration](https://travis-ci.org/ribasco/async-gamequery-lib)
* [Nightly Builds (Snapshots)](https://oss.sonatype.org/content/repositories/snapshots/com/ibasco/agql/)

Implementations
----------------
 
Below is the list of what is currently implemented on the library

* Valve Master Server Query Protocol
* Valve Source Query Protocol
* Valve Steam Web API
* Valve Dota 2 Web API
* Valve CS:GO Web API 
* Valve Source Log Handler (a log monitor service)
* Supercell Clash of Clans Web API

Requirements
------------

* Java JDK 8
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
    <version>LATEST_VERSION</version>
</dependency>
```

**Valve Source Query Protocol**

```
<dependency>
    <groupId>com.ibasco.agql</groupId>
    <artifactId>agql-source-query</artifactId>
    <version>LATEST_VERSION</version>
</dependency>
```

**Valve Steam Web API**

```
<dependency>
    <groupId>com.ibasco.agql</groupId>
    <artifactId>agql-steam-webapi</artifactId>
    <version>LATEST_VERSION</version>
</dependency>
```

**Valve Dota 2 Web API**

```
<dependency>
    <groupId>com.ibasco.agql</groupId>
    <artifactId>agql-dota2-webapi</artifactId>
    <version>LATEST_VERSION</version>
</dependency>
```

**Valve CS:GO Web API**

```
<dependency>
    <groupId>com.ibasco.agql</groupId>
    <artifactId>agql-csgo-webapi</artifactId>
    <version>LATEST_VERSION</version>
</dependency>
```

**Supercell Clash of Clans Web API**

```
<dependency>
    <groupId>com.ibasco.agql</groupId>
    <artifactId>agql-coc-webapi</artifactId>
    <version>LATEST_VERSION</version>
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

Future Integrations
--------------------

My planned integrations for future releases. Don't hesitate to [contact](mailto:raffy@ibasco.com) me if you have any other suggestions.

* Riot Games - League of Legends Web API
* Mojang - Minecraft server query  

Contributing
------------

Fork it and submit a pull request. Any type of contributions are welcome.