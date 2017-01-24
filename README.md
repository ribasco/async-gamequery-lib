Asynchronous Game Query Library
===============================

[![Build Status](https://travis-ci.org/ribasco/async-gamequery-lib.svg?branch=master)](https://travis-ci.org/ribasco/async-gamequery-lib) [![Dependency Status](https://www.versioneye.com/user/projects/5837c911e7cea00029198c9d/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/5837c911e7cea00029198c9d) [![Gitter](https://badges.gitter.im/gitterHQ/gitter.svg)](https://gitter.im/async-gamequery-lib/lobby?utm_source=share-link&utm_medium=link&utm_campaign=share-link) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/2f5f445a366a4692ab8aa49b0cf4f477)](https://www.codacy.com/app/raffy/async-gamequery-lib?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=ribasco/async-gamequery-lib&amp;utm_campaign=Badge_Grade) [![Project Stats](https://www.openhub.net/p/async-gamequery-lib/widgets/project_thin_badge?format=gif&ref=sample)](https://www.openhub.net/p/async-gamequery-lib)
 
It's a game query library which provides a convenient way for java programs to execute asynchronous requests to game servers/services. It's built on top of [Netty](https://github.com/netty/netty) as it's core transport engine and use [AsyncHttpClient](https://github.com/AsyncHttpClient/async-http-client) for web services.

![alt text](site/resources/images/agql-project-banner-big.png "Games supported by Source Query Protocol")

Project Resources
-------------

* [Java API Docs](https://ribasco.github.io/async-gamequery-lib/apidocs)
* [Project Documentation](https://ribasco.github.io/async-gamequery-lib/)
* [Continuous Integration](https://travis-ci.org/ribasco/async-gamequery-lib)
* [Snapshot Builds](https://oss.sonatype.org/content/repositories/snapshots/com/ibasco/agql/)

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
* Apache Commons Math 3
* Netty 4.1.x
* AsyncHttpClient 2.0.x
* SLF4J 1.7.x
* Google Gson 2.8.x
* Google Guava 21.x
 
Installation
------------

Just add the following dependencies to your maven pom.xml. Only include the modules you need.

### Install from Maven Central

**Valve Master Server Query Protocol**

```xml
<dependency>
    <groupId>com.ibasco.agql</groupId>
    <artifactId>agql-steam-master</artifactId>
    <version>0.1.2</version>
</dependency>
```

**Valve Source Query Protocol**

```xml
<dependency>
    <groupId>com.ibasco.agql</groupId>
    <artifactId>agql-source-query</artifactId>
    <version>0.1.2</version>
</dependency>
```

**Valve Steam Web API**

```xml
<dependency>
    <groupId>com.ibasco.agql</groupId>
    <artifactId>agql-steam-webapi</artifactId>
    <version>0.1.2</version>
</dependency>
```

**Valve Dota 2 Web API**

```xml
<dependency>
    <groupId>com.ibasco.agql</groupId>
    <artifactId>agql-dota2-webapi</artifactId>
    <version>0.1.2</version>
</dependency>
```

**Valve CS:GO Web API**

```xml
<dependency>
    <groupId>com.ibasco.agql</groupId>
    <artifactId>agql-csgo-webapi</artifactId>
    <version>0.1.2</version>
</dependency>
```

**Supercell Clash of Clans Web API**

```xml
<dependency>
    <groupId>com.ibasco.agql</groupId>
    <artifactId>agql-coc-webapi</artifactId>
    <version>0.1.2</version>
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
~~~

If you are running a web service type example, you  will be prompted with an API key. Simply copy and paste the key to the console.

~~~
raffy@spinmetal:~/projects/async-gamequery-lib$ ./run-example.sh coc-webapi
Running example for coc-webapi
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building AGQL - Examples 0.1.0
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


Future Integrations
--------------------

My planned integrations for future releases. Don't hesitate to [contact](mailto:raffy@ibasco.com) me if you have any other suggestions.

* Riot Games - League of Legends Web API

Contributing
------------

Fork it and submit a pull request. Any type of contributions are welcome.


Sponsors
---------

* ej Technologies - Developer of the award-winning JProfiler, a full-featured "All-in-one" Java Profiler. Click on the icon below to find out more. 

  [![JProfiler](https://www.ej-technologies.com/images/product_banners/jprofiler_medium.png)](http://www.ej-technologies.com/products/jprofiler/overview.html)