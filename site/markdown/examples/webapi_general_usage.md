Web API General Usage
=====================

**Web API Request Example**

All Web API clients requires an API token `String` argument to be passed to it's constructor. This will be used for authenticating your requests with the server.

~~~java
class WebApiGeneralUsage {

    public static void main(String[] args) throws Exception {
        //API Token
        String token = "<replace with your token here>";

        //Create and initialize client
        try (CocWebApiClient client = new CocWebApiClient(token)) {

            //Create and initialize the interface, passing the client as argument for the default constructor
            CocClans clans = new CocClans(client);

            //Invoke the request
            CompletableFuture<List<CocClanDetailedInfo>> clanDetailsFuture = clans.searchClans(criteria);

            //Do something with clanDetailsFuture...
        }
    }
}
~~~

**Retrieving the result (blocking)**

Here we use the `get()` method of the returned [CompletableFuture](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html), which will block until a response is received from the server. If the request fails, an [ExecutionException](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ExecutionException.html) (checked) will be thrown. Alternatively, you can use `join()` which is the same as `get()` but throws
a [CompletionException](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletionException.html) (unchecked) instead.

~~~java
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

class WebApiGeneralUsageBlocking {

    public static void main(String[] args) throws Exception {
        //API Token
        String token = "<replace with your token here>";

        //Create and initialize client
        try (CocWebApiClient client = new CocWebApiClient(token)) {

            //Create and initialize the interface, passing the client as argument for the default constructor
            CocClans clans = new CocClans(client);

            //Invoke the request
            List<CocClanDetailedInfo> result = clans.searchClans(criteria).get();
            System.out.println("RESPONSE: " + result);
        }
    }
}
~~~

**Retrieving the result (non-blocking)**

~~~java
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

class WebApiGeneralUsageNonBlocking {

    public static void main(String[] args) throws Exception {
        //API Token
        String token = "<replace with your token here>";

        //Create and initialize client
        try (CocWebApiClient client = new CocWebApiClient(token)) {

            //Create and initialize the interface, passing the client as argument for the default constructor
            CocClans clans = new CocClans(client);

            //Invoke the request
            CompletableFuture<List<CocClanDetailedInfo>> clanDetailsFuture = clans.searchClans(criteria);

            //did we receive a response?
            if (clanDetailsFuture.isDone()) {
                System.out.println("RESPONSE: " + clanDetailsFuture.get());
            } else {
                clanDetailsFuture.thenAccept(response -> System.out.println("RESPONSE: " + response));
            }
        }
    }
}
~~~

**Remember**

* Close the client when no longer in use
* Creating client instances can be expensive, use only one instance as much as possible and share it across the interfaces.
* Multiple clients will share the same executor service by default to minimize thread creation