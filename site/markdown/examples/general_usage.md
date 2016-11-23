General Usage
=============

All implementations consists of two things, a `client` and an `interface`.

**API Request Example**

Create the client passing the authentication token provided (if applicable)

~~~
String token = "<replace with your token here>";
CocWebApiClient client = new CocWebApiClient(token);
~~~

Create the interface(s), then pass the client to it's default constructor. For example:

~~~
//Create client
CocWebApiClient client = new CocWebApiClient(token);

//Pass to the CocClans interface
CocClans clans = new CocClans(client);
~~~

**Things to Remember**

* Close the client when no longer in use
* Creating client instances can be expensive, so it is wise to use only one instance as much as possible and share it across the interfaces.