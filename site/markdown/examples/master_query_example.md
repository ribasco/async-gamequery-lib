## Steam Master Server Query Example

Retrieve a list of non-empty dedicated servers. As you can see from the example below, you can easily chain the criterias. Also, in this example we pass a callback so that we could immediately retrieve and process the IP we receive from the master server. 
    
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
try (MasterServerQueryClient client = new MasterServerQueryClient()) {
    MasterServerFilter filter = MasterServerFilter.create().dedicated(true).isEmpty(false);
    client.getServerList(MasterServerType.SOURCE, MasterServerRegion.REGION_ALL, filter, this::displayIp).join();
}
catch (Exception e) {
    log.error("Error", e);
}

public void displayIp(InetSocketAddress address, InetSocketAddress sender, Throwable error) {
    log.info("Server : {}", address);
}
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   
If you don't mind waiting for list to complete, you don't have to pass a callback parameter. Simple invoke get() or join() to retrieve the result. 

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
try (MasterServerQueryClient client = new MasterServerQueryClient()) {
    MasterServerFilter filter = MasterServerFilter.create().dedicated(true);
    Vector<InetSocketAddress> addressList = masterServerQueryClient.getServerList(MasterServerType.SOURCE, MasterServerRegion.REGION_ALL, filter).join();
    addressList.forEach(this::displayIp);
}
catch (Exception e) {
   log.error("Error", e);
}

public void displayIp(InetSocketAddress address) {
    log.info("Server : {}", address);
}
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
