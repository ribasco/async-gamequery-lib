## Source RCON Example

Examples of sending RCON commands to a Source Server.

### Authentication

You only need to authenticate once.

~~~
SourceRconAuthStatus authStatus = sourceRconClient.authenticate(serverAddress, password).join();
if (!authStatus.isAuthenticated()) {
    log.error("ERROR: Could not authenticate from server (Reason: {})", authStatus.getReason());
} 
//do something...
~~~

### Sending Commands

Example of sending a command to  a Source Server

> Note: An exception will be thrown if you are not yet authenticated

~~~
InetSocketAddress address = new InetSocketAddress("192.168.1.13", 27015);
String command = "sm plugins list";

try {
    CompletableFuture<String> resultFuture = sourceRconClient.execute(address, command).whenComplete(this::handleResponse);
} catch (RconNotYetAuthException ex) {
    log.error("You are not yet authenticated", ex);
}

//Callback handler
private void handleResponse(String response, Throwable error) {
    if (error != null) {
        log.error("Error occured while executing command: {}", error.getMessage());
        return;
    }
    log.info("Received Reply: \n{}", response);
}
~~~