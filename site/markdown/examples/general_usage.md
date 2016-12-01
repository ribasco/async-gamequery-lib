Web API General Usage
=====================

**Web API Request Example**

~~~
//API Token
String token = "<replace with your token here>";

//Create and initialize client
CocWebApiClient client = new CocWebApiClient(token);

//Create and initialize the interface, passing the client as argument for the default constructor
CocClans clans = new CocClans(client);

//Invoke the request
CompletableFuture<List<CocClanDetailedInfo>> clanDetailsFuture = clans.searchClans(criteria);
~~~

**Retrieving the result (blocking)**

~~~
List<CocClanDetailedInfo> clanResults = clanDetailsFuture.get();
~~~

**Retrieving the result (non-blocking)**

~~~
clanDetailsFuture.thenAccept(resultList -> resultList.forEach(c -> log.info("Clan Info: {}", c)));
~~~

**Things to Remember**

* Close the client when no longer in use
* Creating client instances can be expensive, so it is wise to use only one instance as much as possible and share it across the interfaces.