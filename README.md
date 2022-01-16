Asynchronous Game Query Library
===============================

[mavenImg]: https://img.shields.io/maven-central/v/com.ibasco.agql/async-gamequery-lib.svg

[mavenLink]: https://search.maven.org/search?q=com.ibasco.agql

[![Maven][mavenImg]][mavenLink] [![Donate](https://img.shields.io/badge/Donate-PayPal-green.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=29TX29ZSNXM64) [![Build Status](https://travis-ci.org/ribasco/async-gamequery-lib.svg?branch=master)](https://travis-ci.org/ribasco/async-gamequery-lib) [![Javadocs](https://www.javadoc.io/badge/com.ibasco.agql/async-gamequery-lib.svg)](https://www.javadoc.io/doc/com.ibasco.agql/async-gamequery-lib) [![Gitter](https://badges.gitter.im/gitterHQ/gitter.svg)](https://gitter.im/async-gamequery-lib/lobby?utm_source=share-link&utm_medium=link&utm_campaign=share-link) [![Project Stats](https://www.openhub.net/p/async-gamequery-lib/widgets/project_thin_badge?format=gif&ref=sample)](https://www.openhub.net/p/async-gamequery-lib)

An asynchronous java based game query library for Valve's source query, rcon and steam web api protocols. Built on top of [Netty](https://github.com/netty/netty)

Changelog
-------------

0.2.0 - BIG UPDATE. In-preparation for the next major release.

- **General Updates**
  - Re-licensed project to Apache-2.0
  - New aggregate project artifact: `agql-<version>.jar`
  - Completely re-worked on the core implementation from the ground up with improved performance and reliability.
  - Added configuration support. Clients can now be tweaked depending on the requirements of the developer (e.g. providing custom executor service, set max active connections, modify read/write timeout settings etc)
  - Failsafe Integration. Queries throwing a ReadTimeoutException or WriteTimeoutException will be re-attempted (max of 3 attempts by default)
  - Deprecated client constructors. Use the new configuration feature of the client.
  - Now using off-heap pooled direct buffers by default for the majority of the queries.
  - Cleaned up and standardized log statements for better readability.
  - Removed custom request/response state management facilities in the core module. Now using netty's channel attributes for managing each channel/connection state.
  - A default global EventLoopGroup (a special netty executor service) is now shared for all clients. A custom executor/EventLoopGroup can still be provided via configuration. (See TransportOptions.THREAD_EL_GROUP)
  - Updated/Enhanced interactive examples

- **Source RCON**
  - Improved support for connection pooling
    - Connection Pooling is now enabled by default
    - Only one thread per connection is maintained for each address/pool (this can be changed via configuration. see TransportOptions.POOL_MAX_CONNECTIONS)
  - Authentication management
    - An internal authentication manager is now utilized by the library to manage all existing connection and ensures that each connection remains in a valid state (connection is active and authenticated by the remote server). Dropped connections will automatically be re-authenticated assuming that the credentials remain valid.
  - Addressed a few special cases
    - **Case #1**: An update of the rcon password (via rcon_password), cause the remote server to close the connection. An exception is now thrown requiring the application to re-authenticate itself by calling authenticate()
    - **Case #2**: Some commands such as 'changelevel' cause the remote server to drop the connection (e.g. changelevel). Previously the client will throw a closed channel exception

- **Source Master Query**
  - Improved/re-worked implementation
  - Failsafe integration
    - Implemented retry and fallback policies. If the primary master server address fails, the client will attempt to connect using an alternative IP address until a connection is established. A maximum of 3 connection attempts will be made.
    - Requests can optionally be rate limited, waiting a specific amount of time before sending a new request for a batch of new addresses.

- **Source Query**
  - Source info query now compatible with the new implementation (challenge based) (See [RFC: Changes to the A2S_INFO protocol ](https://steamcommunity.com/discussions/forum/14/2989789048633291344/))
  - Queries that require a challenge number are now handled automatically by the library, this means that the developer no longer needs to obtain a challenge number manually.
  - **DEPRECATED** challenge caching facilities. This will be removed in the next major update.

- **Source Log Service**
  - Fixed: Callback set via setter method is not applied (Issue #44)
  - Introduced new constructor arguments that accepts a custom ExecutorService
  - listen() now returns a CompletableFuture which is notified once the underlying connection of the service has been closd.

- **Clash of Clans (Web API)**
  - Marked as deprecated and will be removed in the next major version. This module will be removed in the next major version.
  
Project Resources
-------------

* [Java API Docs](https://ribasco.github.io/async-gamequery-lib/apidocs)
* [Project Documentation](https://ribasco.github.io/async-gamequery-lib/)
* [Continuous Integration](https://travis-ci.org/ribasco/async-gamequery-lib)
* [Snapshot Builds](https://oss.sonatype.org/content/repositories/snapshots/com/ibasco/agql/)

Discussion Platforms
-----------------

If you have any inquiries,concerns or suggestions please use one of the official communication channels for this project

* [Project Issue Tracker](https://github.com/ribasco/async-gamequery-lib/issues/new) (For bug reports/issues please use this)
* [Gitter IM](https://gitter.im/async-gamequery-lib/lobby?utm_source=share-link&utm_medium=link&utm_campaign=share-link)

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

Installation
------------

Just add the following dependencies to your maven pom.xml. Only include the modules you need.

### Install from Maven Central

**Aggregate**

Contains all modules (rcon, query, web apis)

```xml

<dependency>
  <groupId>com.ibasco.agql</groupId>
  <artifactId>agql</artifactId>
  <version>0.2.0</version>
</dependency>
```

**Valve Master Server Query Protocol**

```xml

<dependency>
  <groupId>com.ibasco.agql</groupId>
  <artifactId>agql-steam-master</artifactId>
  <version>0.2.0</version>
</dependency>
```

**Valve Source Query Protocol**

```xml

<dependency>
    <groupId>com.ibasco.agql</groupId>
    <artifactId>agql-source-query</artifactId>
    <version>0.2.0</version>
</dependency>
```

**Valve Steam Web API**

```xml

<dependency>
    <groupId>com.ibasco.agql</groupId>
    <artifactId>agql-steam-webapi</artifactId>
    <version>0.2.0</version>
</dependency>
```

**Valve Dota 2 Web API**

```xml

<dependency>
    <groupId>com.ibasco.agql</groupId>
    <artifactId>agql-dota2-webapi</artifactId>
    <version>0.2.0</version>
</dependency>
```

**Valve CS:GO Web API**

```xml

<dependency>
  <groupId>com.ibasco.agql</groupId>
  <artifactId>agql-csgo-webapi</artifactId>
  <version>0.2.0</version>
</dependency>
```

**Supercell Clash of Clans Web API (Deprecated)**

> **NOTE**: As of 0.2.0, this has been marked as deprecated and will be removed in the next major release

```xml

<dependency>
  <groupId>com.ibasco.agql</groupId>
  <artifactId>agql-coc-webapi</artifactId>
  <version>0.2.0</version>
</dependency>
```

### Install from Source

Clone from remote repository then `mvn install`. All of the modules will be installed to your local maven repository.

~~~bash
git clone https://github.com/ribasco/async-gamequery-lib.git
cd async-gamequery-lib
mvn install
~~~

Usage
------------

For usage examples, please refer to the [site docs](http://ribasco.github.io/async-gamequery-lib/).

Interactive Examples
--------------------

To run the available examples, I have included a convenience script (`run-example.sh`) that will allow you to pick a specific example you want to run.

The script accepts a "key" that represents an example application. To get a list of keys, simply invoke the script without arguments, for example:

~~~bash
raffy@spinmetal:~/projects/async-gamequery-lib$ ./run-example.sh
Error: Missing Example Key. Please specify the example key. (e.g. source-query)

====================================================================
List of available examples
====================================================================
- Source Server Query Example      (key: source-query)
- Master Server Query Example      (key: master-query)
- Source Rcon Example              (key: source-rcon)
- Clash of Clans Web API Example   (key: coc-webapi)
- CS:GO Web API Example            (key: csgo-webapi)
- Steam Web API Example            (key: steam-webapi)
- Steam Storefront Web API Example (key: steam-store-webapi)
- Source Log Listener Example      (key: source-logger)
- Steam Econ Web API Example       (key: steam-econ-webapi)
- Dota2 Web API Example            (key: dota2-webapi)
~~~

If you are running a web service type example, you will be prompted with an API key. Simply copy and paste the key to the console.

~~~
raffy@spinmetal:~/projects/async-gamequery-lib$ ./run-example.sh coc-webapi
Running example for coc-webapi
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building AGQL - Examples 0.1.5
[INFO] ------------------------------------------------------------------------
[INFO]
[INFO] --- exec-maven-plugin:1.5.0:java (default-cli) @ agql-lib-examples ---
19:59:25.659 [com.ibasco.agql.examples.base.ExampleRunner.main()] INFO  com.ibasco.agql.examples.base.ExampleRunner - Running Example : coc-webapi
Please input your API Token:
~~~

**Note:**

* Don't forget to perform a `mvn clean install` before running an example
* The output can be reviewed from the `logs` directory under the project's directory.

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

Contributing
------------

Fork it and submit a pull request. Any type of contributions are welcome.

Special Thanks/Sponsors
------------------------

* ej Technologies - Developer of the award-winning JProfiler, a full-featured "All-in-one" Java Profiler. Click on the icon below to find out more.

  [![JProfiler](https://www.ej-technologies.com/images/product_banners/jprofiler_medium.png)](http://www.ej-technologies.com/products/jprofiler/overview.html)

* JetBrains - For providing the open-source license for their awesome Java IDE.

  [![IntelliJ IDEA](site/resources/images/intellij-icon.png)](https://www.jetbrains.com/idea)
