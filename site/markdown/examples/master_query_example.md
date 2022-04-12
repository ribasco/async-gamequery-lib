## Steam Master Server Query Example

### Options

### Usage Examples

~~~java
import java.util.concurrent.ExecutionException;

class MasterServerQueryExample {

    public static void main(String[] args) throws Exception {
        try (MasterServerQueryClient client = new MasterServerQueryClient()) {
            MasterServerFilter filter = MasterServerFilter.create().dedicated(true).isEmpty(false);
            client.getServers(MasterServerType.SOURCE, MasterServerRegion.REGION_ALL, filter, this::displayIp).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace(System.err);
        }
    }

    public void displayIp(InetSocketAddress address, InetSocketAddress sender, Throwable error) {
        log.info("Server : {}", address);
    }
}
~~~

You can also pass a `TriConsumer<InetSocketAddress, InetSocketAddress, Throwable>` callback to receive addresses while

~~~java

~~~
